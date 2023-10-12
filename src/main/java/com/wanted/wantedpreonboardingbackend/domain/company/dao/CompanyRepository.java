package com.wanted.wantedpreonboardingbackend.domain.company.dao;

import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
