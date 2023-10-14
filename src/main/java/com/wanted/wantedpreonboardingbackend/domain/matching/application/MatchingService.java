package com.wanted.wantedpreonboardingbackend.domain.matching.application;

import com.wanted.wantedpreonboardingbackend.domain.matching.dao.MatchingRepository;
import com.wanted.wantedpreonboardingbackend.domain.matching.entity.Matching;
import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;
import com.wanted.wantedpreonboardingbackend.domain.user.entity.User;
import com.wanted.wantedpreonboardingbackend.global.error.BusinessException;
import com.wanted.wantedpreonboardingbackend.global.error.ErrorCode;
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

    //사용자가 이전에 지원하면 예외 처리
    isAlreadyMatch(user, recruit);

    Matching matching = matchingRepository.save(Matching.builder()
        .user(user)
        .recruit(recruit)
        .build());

    return matching.getId();
  }

  /**
   * 사용자가 이전에 지원했는지 확인 후, 예외 처리
   *
   * @param user    지원자
   * @param recruit 채용공고
   */
  private static void isAlreadyMatch(User user, Recruit recruit) {
    boolean anyMatch = recruit.getMatchings().stream()
        .map(Matching::getUser)
        .map(User::getId)
        .anyMatch(id -> id.equals(user.getId()));

    if (anyMatch) {
      throw new BusinessException(user.getId(), "user", ErrorCode.ALREADY_MATCH);
    }
  }
}
