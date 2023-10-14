package com.wanted.wantedpreonboardingbackend.domain.user.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "t_user")
public class User {

  public static final int MAX_NAME_LENGTH = 15;
  //사용자 ID
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  //사용자 이름
  @Column(name = "name", nullable = false, length = MAX_NAME_LENGTH)
  private String name;

  @Builder
  public User(Long id, String name) {
    
    this.id = id;
    this.name = name;
  }
}
