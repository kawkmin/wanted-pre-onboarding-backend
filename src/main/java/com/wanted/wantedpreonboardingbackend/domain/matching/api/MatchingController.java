package com.wanted.wantedpreonboardingbackend.domain.matching.api;

import com.wanted.wantedpreonboardingbackend.domain.matching.application.MatchingService;
import com.wanted.wantedpreonboardingbackend.domain.recruit.application.RecruitService;
import com.wanted.wantedpreonboardingbackend.domain.user.application.UserService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/matching")
public class MatchingController {

  private final MatchingService matchingService;
  private final RecruitService recruitService;
  private final UserService userService;

  /**
   * 지원 정보 생성
   *
   * @param recruitId 채용공고
   * @param userId    지원자
   * @return 201, 생성된 지원 정보 조회 url
   */
  @PostMapping("")
  public ResponseEntity<Void> createMatching(
      @RequestParam Long recruitId,
      @RequestParam Long userId
  ) {
    Long createdMatchingId = matchingService.CreateMatching(userService.getUserById(userId),
        recruitService.getRecruitById(recruitId));

    return ResponseEntity.status(HttpStatus.CREATED)
        .location(URI.create("/matching/" + createdMatchingId)) //생성된 지원 정보 조회 url 담기
        .build();
  }

}
