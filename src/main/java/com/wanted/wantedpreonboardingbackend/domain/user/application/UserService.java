package com.wanted.wantedpreonboardingbackend.domain.user.application;


import com.wanted.wantedpreonboardingbackend.domain.user.dao.UserRepository;
import com.wanted.wantedpreonboardingbackend.domain.user.entity.User;
import com.wanted.wantedpreonboardingbackend.global.error.BusinessException;
import com.wanted.wantedpreonboardingbackend.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

  /**
   * id로 유저 조회
   *
   * @param userId 유저 id
   * @return 조회된 유저 Entity
   */
  public User getUserById(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new BusinessException(userId, "userId", ErrorCode.USER_NOT_FOUND)
    );

    return user;
  }
}
