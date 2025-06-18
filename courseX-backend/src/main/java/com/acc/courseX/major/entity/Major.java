package com.acc.courseX.major.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "majors")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Major {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  public String getName() {
    return name;
  }

  public static Major createTestMajor(String name) {
    Major major = new Major();
    try {
      java.lang.reflect.Field nameField = Major.class.getDeclaredField("name");
      nameField.setAccessible(true);
      nameField.set(major, name);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create test major", e);
    }
    return major;
  }
}
