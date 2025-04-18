package com.acc.courseX.course.service;

import java.util.List;

import com.acc.courseX.course.dto.CourseResponse;

public interface CourseService {
  List<CourseResponse> getCourses(String code);

  void enroll(Long courseId, Long userId);
}
