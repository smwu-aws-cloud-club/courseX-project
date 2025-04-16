package com.acc.courseX.service;

import java.util.List;

import com.acc.courseX.dto.CourseResponse;

public interface CourseService {
  List<CourseResponse> getCourses(String code);
}
