package com.wanted.wantedpreonboardingbackend.domain.company.entity;

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
@Table(name = "t_company")
public class Company {

  public static final int MAX_NAME_LENGTH = 100;
  public static final int MAX_COUNTRY_LENGTH = 40;
  public static final int MAX_REGION_LENGTH = 100;

  //회사 ID
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  //회사 이름
  @Column(name = "name", nullable = false, length = MAX_NAME_LENGTH)
  private String name;

  //회사 국가
  @Column(name = "country", nullable = false, length = MAX_COUNTRY_LENGTH)
  private String country;

  //회사 지역
  @Column(name = "region", nullable = false, length = MAX_REGION_LENGTH)
  private String region;

  // 단위 테스트용 빌더
  @Builder(builderMethodName = "allBuilder")
  public Company(Long id, String name, String country, String region) {
    this.id = id;
    this.name = name;
    this.country = country;
    this.region = region;
  }

  @Builder
  public Company(String name, String country, String region) {
    this.name = name;
    this.country = country;
    this.region = region;
  }
}
