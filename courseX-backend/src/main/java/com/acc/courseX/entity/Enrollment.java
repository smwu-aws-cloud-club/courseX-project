package com.acc.courseX.entity;

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
import jakarta.persistence.Table;

import com.acc.courseX.entity.enums.EnrollmentStatus;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "enrollments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Enrollment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "course_id", nullable = false)
  private Course course;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EnrollmentStatus status = EnrollmentStatus.ENROLLED;
}
