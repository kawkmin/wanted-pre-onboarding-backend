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

    private String name;

    public CompanyBuilder() {
    }

    public CompanyBuilder name(String name) {
      this.name = name;
      return this;
    }

    public Company build() {
      return Company.builder()
          .name(name != null ? name : "테스트 회사 이름")
          .build();
    }
  }
}
