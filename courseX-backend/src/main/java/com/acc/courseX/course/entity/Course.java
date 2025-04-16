package com.acc.courseX.course.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.acc.courseX.user.entity.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String code;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "professor_id", nullable = false)
  private User professor;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private CourseType courseType;

  private int credit;
  private int maxStudent;
  private int currentStudents;

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CourseSchedule> schedules = new ArrayList<>();

  public String getProfessorName() {
    return professor.getName();
  }

  public int getRemainingSeats() {
    return maxStudent - currentStudents;
  }

  public String getCourseSchedule() {
    return schedules.stream().map(CourseSchedule::toString).collect(Collectors.joining(", "));
  }
}
