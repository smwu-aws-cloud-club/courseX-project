package com.acc.courseX.user.entity;

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

import com.acc.courseX.common.entity.BaseTime;
import com.acc.courseX.major.entity.Major;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseTime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String affiliationId;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private Role role = Role.STUDENT;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "major_id", nullable = false)
  private Major major;

  // 테스트용 정적 팩토리 메서드
  public static User createTestUser(
      String affiliationId, String password, String name, Role role, Major major) {
    User user = new User();
    try {
      java.lang.reflect.Field affiliationIdField = User.class.getDeclaredField("affiliationId");
      java.lang.reflect.Field passwordField = User.class.getDeclaredField("password");
      java.lang.reflect.Field nameField = User.class.getDeclaredField("name");
      java.lang.reflect.Field roleField = User.class.getDeclaredField("role");
      java.lang.reflect.Field majorField = User.class.getDeclaredField("major");

      affiliationIdField.setAccessible(true);
      passwordField.setAccessible(true);
      nameField.setAccessible(true);
      roleField.setAccessible(true);
      majorField.setAccessible(true);

      affiliationIdField.set(user, affiliationId);
      passwordField.set(user, password);
      nameField.set(user, name);
      roleField.set(user, role);
      majorField.set(user, major);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create test user", e);
    }
    return user;
  }
}
