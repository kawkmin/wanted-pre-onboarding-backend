package com.wanted.wantedpreonboardingbackend.domain.recruit;

import com.wanted.wantedpreonboardingbackend.domain.company.CompanyTestHelper;
import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;

/**
 * 채용공고 테스트 헬퍼 (다른 helper 사전 저장 요구)
 */

public class RecruitTestHelper {

  private final CompanyTestHelper companyTestHelper;

  public RecruitTestHelper(CompanyTestHelper companyTestHelper) {
    this.companyTestHelper = companyTestHelper;
  }

  public Recruit generate() {
    return this.builder().build();
  }

  public RecruitBuilder builder() {
    return new RecruitBuilder();
  }

  public final class RecruitBuilder {

    //채용공고 Id
    private Long id;
    //회사
    private Company company;
    //채용 포지션명
    private String position;
    //보상금
    private Integer reward;
    //채용 내용
    private String content;
    //사용 기술명
    private String skill;

    public RecruitBuilder() {
    }

    public RecruitBuilder id(Long id) {
      this.id = id;
      return this;
    }

    public RecruitBuilder company(Company company) {
      this.company = company;
      return this;
    }

    public RecruitBuilder position(String position) {
      this.position = position;
      return this;
    }

    public RecruitBuilder reward(Integer reward) {
      this.reward = reward;
      return this;
    }

    public RecruitBuilder content(String content) {
      this.content = content;
      return this;
    }

    public RecruitBuilder skill(String skill) {
      this.skill = skill;
      return this;
    }

    public Recruit build() {
      return Recruit.allBuilder()
          .id(id != null ? id : 1L)
          .company(company != null ? company : companyTestHelper.generate())
          .position(position != null ? position : "테스트 채용 포지션명")
          .reward(reward != null ? reward : 1000)
          .content(content != null ? content : "테스트 채용 내용")
          .skill(skill != null ? skill : "테스트 사용 기술명")
          .build();
    }
  }
}
