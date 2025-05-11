package com.acc.courseX.log.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import com.acc.courseX.user.entity.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Log {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false)
  private String actionType;

  @Column(nullable = false)
  private String targetTable;

  private Long targetId;

  @Column(columnDefinition = "JSON")
  private String metadata;

  private String ipAddress;

  private String userAgent;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Builder
  public Log(
      User user,
      String actionType,
      String targetTable,
      Long targetId,
      String metadata,
      String ipAddress,
      String userAgent) {
    this.user = user;
    this.actionType = actionType;
    this.targetTable = targetTable;
    this.targetId = targetId;
    this.metadata = metadata;
    this.ipAddress = ipAddress;
    this.userAgent = userAgent;
    this.createdAt = LocalDateTime.now();
  }

  @PrePersist
  public void prePersist() {
    if (this.createdAt == null) {
      this.createdAt = LocalDateTime.now();
    }
  }
}
