package com.acc.courseX.course.service;

import java.util.List;
import java.util.stream.Collectors;

import com.acc.courseX.course.dto.CourseResponse;
import com.acc.courseX.course.entity.Course;
import com.acc.courseX.course.repository.CourseRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
  private final CourseRepository courseRepository;

  @Transactional(readOnly = true)
  @Override
  public List<CourseResponse> getCourses(final String code) {
    boolean hasCodeFilter = code != null && !code.isBlank();

    List<Course> courses =
        hasCodeFilter ? courseRepository.findByCode(code) : courseRepository.findAll();

    return courses.stream()
        .map(
            course ->
                CourseResponse.of(
                    course.getId(),
                    course.getCode(),
                    course.getName(),
                    course.getCredit(),
                    course.getProfessorName(),
                    course.getCourseSchedule(),
                    course.getMaxStudents(),
                    course.getRemainingSeats()))
        .collect(Collectors.toList());
  }
}
