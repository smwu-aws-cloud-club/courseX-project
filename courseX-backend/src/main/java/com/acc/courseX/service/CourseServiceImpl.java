package com.acc.courseX.service;

import java.util.List;

import com.acc.courseX.dto.CourseResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
  @Override
  public List<CourseResponse> getCourses(final String code) {
    return null;
  }
}
