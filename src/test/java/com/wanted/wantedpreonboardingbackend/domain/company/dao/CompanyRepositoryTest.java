package com.wanted.wantedpreonboardingbackend.domain.company.dao;

import com.wanted.wantedpreonboardingbackend.TestHelper;
import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CompanyRepositoryTest extends TestHelper {

  @Autowired
  private CompanyRepository companyRepository;

  private Company company;

  @BeforeEach
  void setUp() {
    company = companyRepository.save(companyTestHelper.generate());
  }

  @Nested
  @DisplayName("회사 조회 테스트")
  class CompanyFindTest {

    @Test
    @DisplayName("id로 회사 조회를 할 수 있어야 한다")
    void id로_회사_조회를_할_수_있어야_한다() {
      Company findCompany = companyRepository.findById(company.getId()).orElseThrow();

      Assertions.assertThat(company.getId()).isEqualTo(findCompany.getId());
    }
  }
}