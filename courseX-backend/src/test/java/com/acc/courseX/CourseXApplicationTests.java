package com.acc.courseX;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = {"classpath:env/test.env"})
class CourseXApplicationTests {

  @Test
  void contextLoads() {}
}
