package com.wanted.wantedpreonboardingbackend.domain.matching.application;

import com.wanted.wantedpreonboardingbackend.domain.matching.dao.MatchingRepository;
import com.wanted.wantedpreonboardingbackend.domain.matching.entity.Matching;
import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;
import com.wanted.wantedpreonboardingbackend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingService {

  private final MatchingRepository matchingRepository;

  /**
   * 지원 정보를 생성한다
   *
   * @param user    지원자
   * @param recruit 채용공고
   * @return 지원 정보
   */
  @Transactional
  public Long CreateMatching(User user, Recruit recruit) {
    Matching matching = matchingRepository.save(Matching.builder()
        .user(user)
        .recruit(recruit)
        .build());

    return matching.getId();
  }
}
