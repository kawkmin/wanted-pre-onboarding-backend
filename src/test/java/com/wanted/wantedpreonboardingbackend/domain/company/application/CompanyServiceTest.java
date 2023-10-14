package com.wanted.wantedpreonboardingbackend.domain.company.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.wanted.wantedpreonboardingbackend.TestHelper;
import com.wanted.wantedpreonboardingbackend.domain.company.dao.CompanyRepository;
import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest extends TestHelper {

  @InjectMocks
  private CompanyService companyService;
  @Mock
  private CompanyRepository companyRepository;

  private Company company;

  @BeforeEach
  void setUp() {
    super.init();
    company = companyTestHelper.generate();
  }

  @Nested
  @DisplayName("회사 조회 테스트")
  class FindCompany {

    @Test
    @DisplayName("정상적으로 회사 조회에 성공한다.")
    void 정상적으로_회사_조회에_성공한다() {
      given(companyRepository.findById(anyLong())).willReturn(Optional.of(company));

      Company companyById = companyService.getCompanyById(1L);
      assertThat(companyById).isEqualTo(company);
    }
  }
}