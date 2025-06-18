package com.acc.courseX.course.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.acc.courseX.course.entity.Course;
import com.acc.courseX.course.entity.CourseType;
import com.acc.courseX.course.repository.CourseRepository;
import com.acc.courseX.enrollment.entity.EnrollmentStatus;
import com.acc.courseX.enrollment.exception.EnrollmentException;
import com.acc.courseX.enrollment.repository.EnrollmentRepository;
import com.acc.courseX.major.entity.Major;
import com.acc.courseX.major.repository.MajorRepository;
import com.acc.courseX.user.entity.Role;
import com.acc.courseX.user.entity.User;
import com.acc.courseX.user.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = {"classpath:env/test.env"})
class CourseServiceConcurrencyTest {

  @Autowired private CourseService courseService;

  @Autowired private CourseRepository courseRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private EnrollmentRepository enrollmentRepository;

  @Autowired private MajorRepository majorRepository;

  private Course testCourse;
  private List<User> testUsers;
  private Major testMajor;

  @BeforeEach
  void setUp() {
    testMajor = majorRepository.save(Major.createTestMajor("컴퓨터공학과"));
    User professor =
        userRepository.save(
            User.createTestUser("P001", "password", "테스트 교수", Role.PROFESSOR, testMajor));
    testCourse =
        courseRepository.save(
            Course.createTestCourse("TEST001", "동시성 테스트 과목", professor, CourseType.MAJOR, 3, 3));

    testUsers = new ArrayList<>();
    for (int i = 1; i <= 5; i++) {
      User student =
          userRepository.save(
              User.createTestUser("S00" + i, "password", "학생" + i, Role.STUDENT, testMajor));
      testUsers.add(student);
    }
  }

  @Test
  @DisplayName("동시 수강신청 시 수강인원 초과 방지 테스트")
  void concurrentEnrollment_ShouldNotExceedMaxStudents() throws InterruptedException {
    // given
    int maxStudents = testCourse.getMaxStudents();
    int concurrentUsers = 5; // 최대 인원보다 많은 동시 요청
    ExecutorService executor = Executors.newFixedThreadPool(concurrentUsers);
    AtomicInteger successCount = new AtomicInteger(0);
    AtomicInteger failureCount = new AtomicInteger(0);

    // when
    List<CompletableFuture<Void>> futures = new ArrayList<>();

    for (int i = 0; i < concurrentUsers; i++) {
      final int userIndex = i;
      CompletableFuture<Void> future =
          CompletableFuture.runAsync(
              () -> {
                try {
                  courseService.enroll(testCourse.getId(), testUsers.get(userIndex).getId());
                  successCount.incrementAndGet();
                } catch (Exception e) {
                  failureCount.incrementAndGet();
                }
              },
              executor);
      futures.add(future);
    }

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    // then
    assertThat(successCount.get()).isEqualTo(maxStudents);
    assertThat(failureCount.get()).isEqualTo(concurrentUsers - maxStudents);

    Course updatedCourse = courseRepository.findById(testCourse.getId()).orElseThrow();
    assertThat(updatedCourse.getCurrentStudents()).isEqualTo(maxStudents);

    long enrollmentCount =
        enrollmentRepository.countByCourseAndStatus(testCourse, EnrollmentStatus.ENROLLED);
    assertThat(enrollmentCount).isEqualTo(maxStudents);
  }

  @Test
  @DisplayName("동일 사용자의 중복 수강신청 방지 테스트")
  void duplicateEnrollment_ShouldBePrevented() {
    // given
    User student = testUsers.get(0);

    // when & then
    courseService.enroll(testCourse.getId(), student.getId());
    assertThatThrownBy(() -> courseService.enroll(testCourse.getId(), student.getId()))
        .isInstanceOf(EnrollmentException.class)
        .hasMessageContaining("이미 수강 신청이 완료된 강의입니다");
    long enrollmentCount =
        enrollmentRepository.countByCourseAndStatus(testCourse, EnrollmentStatus.ENROLLED);
    assertThat(enrollmentCount).isEqualTo(1);
  }

  @Test
  @DisplayName("수강인원 초과 시 적절한 예외 발생 테스트")
  void enrollmentExceedingMaxStudents_ShouldThrowException() {
    // given
    int maxStudents = testCourse.getMaxStudents();
    for (int i = 0; i < maxStudents; i++) {
      courseService.enroll(testCourse.getId(), testUsers.get(i).getId());
    }

    // when & then
    assertThatThrownBy(
            () -> courseService.enroll(testCourse.getId(), testUsers.get(maxStudents).getId()))
        .isInstanceOf(Exception.class); // CourseException 또는 관련 예외
    Course updatedCourse = courseRepository.findById(testCourse.getId()).orElseThrow();
    assertThat(updatedCourse.getCurrentStudents()).isEqualTo(maxStudents);
  }

  @Test
  @DisplayName("데드락 재시도 로직 동작 확인 테스트")
  void deadlockRetryLogic_ShouldWork() throws InterruptedException {
    // given
    int maxStudents = testCourse.getMaxStudents();
    int concurrentUsers = maxStudents;
    ExecutorService executor = Executors.newFixedThreadPool(concurrentUsers);
    AtomicInteger successCount = new AtomicInteger(0);

    // when
    List<CompletableFuture<Void>> futures = new ArrayList<>();

    for (int i = 0; i < concurrentUsers; i++) {
      final int userIndex = i;
      CompletableFuture<Void> future =
          CompletableFuture.runAsync(
              () -> {
                try {
                  courseService.enroll(testCourse.getId(), testUsers.get(userIndex).getId());
                  successCount.incrementAndGet();
                } catch (Exception e) {
                  // 재시도 로직으로 인해 예외가 발생하지 않아야 함
                }
              },
              executor);
      futures.add(future);
    }

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    // then
    assertThat(successCount.get()).isEqualTo(maxStudents);
    Course updatedCourse = courseRepository.findById(testCourse.getId()).orElseThrow();
    assertThat(updatedCourse.getCurrentStudents()).isEqualTo(maxStudents);
  }

  @AfterEach
  void tearDown() {
    enrollmentRepository.deleteAllInBatch();
    courseRepository.deleteAllInBatch();
    userRepository.deleteAllInBatch();
    majorRepository.deleteAllInBatch();
  }
}
