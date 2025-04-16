package com.acc.courseX.service;

import java.util.List;
import java.util.stream.Collectors;

import com.acc.courseX.dto.CourseResponse;
import com.acc.courseX.entity.Course;
import com.acc.courseX.repository.CourseRepository;

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
                    course.getCode(),
                    course.getName(),
                    course.getCredit(),
                    course.getProfessorName(),
                    course.getCourseSchedule(),
                    course.getMaxStudent(),
                    course.getRemainingSeats()))
        .collect(Collectors.toList());
  }
}
