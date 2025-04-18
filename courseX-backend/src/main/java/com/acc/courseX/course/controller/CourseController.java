package com.acc.courseX.course.controller;

import static com.acc.courseX.course.code.CourseSuccess.GET_COURSE_LIST;

import java.util.List;

import com.acc.courseX.common.response.ResponseUtil;
import com.acc.courseX.course.dto.CourseResponse;
import com.acc.courseX.course.service.CourseService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController implements CourseApi {
  private final CourseService courseService;

  @GetMapping
  @Override
  public ResponseEntity<?> getCourses(@RequestParam(required = false) final String code) {
    List<CourseResponse> response = courseService.getCourses(code);
    return ResponseUtil.success(GET_COURSE_LIST, response);
  }

  @PostMapping("/{courseId}/enroll")
  @Override
  public ResponseEntity<?> enroll(@PathVariable Long courseId, Long userId) {
    return null;
  }
}
