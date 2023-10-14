package com.wanted.wantedpreonboardingbackend.domain.company;

import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;

/**
 * 회사 테스트 헬퍼
 */
public class CompanyTestHelper {

  public Company generate() {
    return this.builder().build();
  }

  public CompanyBuilder builder() {
    return new CompanyBuilder();
  }

  public final class CompanyBuilder {

    private Long id;
    private String name;

    private String country;

    private String region;

    public CompanyBuilder() {
    }

    public CompanyBuilder id(Long id) {
      this.id = id;
      return this;
    }

    public CompanyBuilder name(String name) {
      this.name = name;
      return this;
    }

    public CompanyBuilder country(String country) {
      this.country = country;
      return this;
    }

    public CompanyBuilder region(String region) {
      this.region = region;
      return this;
    }

    public Company build() {
      return Company.allBuilder()
          .id(id != null ? id : 1L)
          .name(name != null ? name : "테스트 회사 이름")
          .country(country != null ? country : "테스트 나라 이름")
          .region(region != null ? region : "테스트 지역 이름")
          .build();
    }
  }
}
