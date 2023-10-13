package com.wanted.wantedpreonboardingbackend.domain.company.application;


import com.wanted.wantedpreonboardingbackend.domain.company.dao.CompanyRepository;
import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import com.wanted.wantedpreonboardingbackend.global.error.BusinessException;
import com.wanted.wantedpreonboardingbackend.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

  private final CompanyRepository companyRepository;

  /**
   * id로 회사 조회
   *
   * @param companyId 회사 id
   * @return 조회된 회사 Entity
   */
  public Company getCompanyById(Long companyId) {
    Company company = companyRepository.findById(companyId).orElseThrow(
        () -> new BusinessException(companyId, "companyId", ErrorCode.COMPANY_NOT_FOUND)
    );

    return company;
  }
}
