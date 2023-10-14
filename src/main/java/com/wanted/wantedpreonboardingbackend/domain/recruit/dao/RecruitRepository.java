package com.wanted.wantedpreonboardingbackend.domain.recruit.dao;

import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitRepository extends JpaRepository<Recruit, Long> {

}
