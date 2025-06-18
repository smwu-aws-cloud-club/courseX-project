package com.acc.courseX.course.entity;

import static com.acc.courseX.course.code.CourseFailure.COURSE_FULL;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.acc.courseX.course.exception.CourseException;
import com.acc.courseX.user.entity.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "professor_id", nullable = false)
  private User professor;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private CourseType courseType;

  private int credit;
  private int maxStudents;
  private int currentStudents;

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CourseSchedule> schedules = new ArrayList<>();

  public String getProfessorName() {
    return professor.getName();
  }

  public int getRemainingSeats() {
    return maxStudents - currentStudents;
  }

  public String getCourseSchedule() {
    return schedules.stream().map(CourseSchedule::toString).collect(Collectors.joining(", "));
  }

  public void increaseCurrentStudents() {
    if (this.currentStudents >= maxStudents) {
      throw new CourseException(COURSE_FULL);
    }
    this.currentStudents++;
  }

  public void decreaseCurrentStudents() {
    if (this.currentStudents > 0) {
      this.currentStudents--;
    }
  }

  // 테스트용 정적 팩토리 메서드
  public static Course createTestCourse(
      String code,
      String name,
      User professor,
      CourseType courseType,
      int credit,
      int maxStudents) {
    Course course = new Course();
    try {
      java.lang.reflect.Field codeField = Course.class.getDeclaredField("code");
      java.lang.reflect.Field nameField = Course.class.getDeclaredField("name");
      java.lang.reflect.Field professorField = Course.class.getDeclaredField("professor");
      java.lang.reflect.Field courseTypeField = Course.class.getDeclaredField("courseType");
      java.lang.reflect.Field creditField = Course.class.getDeclaredField("credit");
      java.lang.reflect.Field maxStudentsField = Course.class.getDeclaredField("maxStudents");
      java.lang.reflect.Field currentStudentsField =
          Course.class.getDeclaredField("currentStudents");

      codeField.setAccessible(true);
      nameField.setAccessible(true);
      professorField.setAccessible(true);
      courseTypeField.setAccessible(true);
      creditField.setAccessible(true);
      maxStudentsField.setAccessible(true);
      currentStudentsField.setAccessible(true);

      codeField.set(course, code);
      nameField.set(course, name);
      professorField.set(course, professor);
      courseTypeField.set(course, courseType);
      creditField.set(course, credit);
      maxStudentsField.set(course, maxStudents);
      currentStudentsField.set(course, 0);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create test course", e);
    }
    return course;
  }
}
