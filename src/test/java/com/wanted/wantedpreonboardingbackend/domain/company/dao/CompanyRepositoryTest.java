package com.wanted.wantedpreonboardingbackend.domain.company.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wanted.wantedpreonboardingbackend.TestHelper;
import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CompanyRepositoryTest extends TestHelper {

  @Autowired
  private CompanyRepository companyRepository;

  private Company company;

  @BeforeEach
  void setUp() {
    // db에 테스트용 회사 저장
    company = companyRepository.save(companyTestHelper.generate());
  }

  @Nested
  @DisplayName("회사 조회 테스트")
  class CompanyFindTest {

    @Test
    @DisplayName("id로 회사 조회를 할 수 있어야 한다")
    void id로_회사_조회를_할_수_있어야_한다() {
      Company findCompany = companyRepository.findById(company.getId()).orElseThrow();

      assertThat(company.getId()).isEqualTo(findCompany.getId());
    }

    @Test
    @DisplayName("없는 회사 id로 조회 할 수 없다.")
    void 없는_회사_id로_조회_할_수_없다() {
      assertThatThrownBy(
          () -> companyRepository.findById(100L).orElseThrow()
      );

    }
  }
}