package com.wanted.wantedpreonboardingbackend.domain.matching.dao;

import com.wanted.wantedpreonboardingbackend.domain.matching.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingRepository extends JpaRepository<Matching, Long> {

}
