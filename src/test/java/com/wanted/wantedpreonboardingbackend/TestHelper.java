package com.wanted.wantedpreonboardingbackend;

import com.wanted.wantedpreonboardingbackend.domain.company.CompanyTestHelper;
import com.wanted.wantedpreonboardingbackend.domain.recruit.RecruitTestHelper;
import jakarta.annotation.PostConstruct;

/**
 * 모든 테스트 헬퍼를 가진 부모 클래스
 */
public class TestHelper {

  protected CompanyTestHelper companyTestHelper;
  protected RecruitTestHelper recruitTestHelper;

  @PostConstruct
  public void init() {
    companyTestHelper = new CompanyTestHelper();
    recruitTestHelper = new RecruitTestHelper(companyTestHelper);
  }
}
