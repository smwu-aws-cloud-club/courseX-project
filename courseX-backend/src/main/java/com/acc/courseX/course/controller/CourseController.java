package com.acc.courseX.course.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController implements CourseApi {

  @GetMapping
  @Override
  public ResponseEntity<?> getCourses(@RequestParam(required = false) String code) {
    return null;
  }
}
