package com.wanted.wantedpreonboardingbackend.domain.company.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "t_company")
public class Company {

  public static final int MAX_NAME_LENGTH = 100;

  //회사 ID
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  //회사 이름
  @Column(name = "name", nullable = false, length = MAX_NAME_LENGTH)
  private String name;
}
