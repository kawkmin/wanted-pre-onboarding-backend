package com.wanted.wantedpreonboardingbackend.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 오류 메시지와 상태를 쉽게 추가하기 위한 Enum
 */
@Getter
public enum ErrorCode {
  //회사
  COMPANY_NOT_FOUND("해당 회사를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  //채용공고
  RECRUIT_NOT_FOUND("해당 채용공고를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  RECRUIT_INACCESSIBLE("해당 채용공에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),

  //유저
  USER_NOT_FOUND("해당 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  //지원 정보
  ALREADY_MATCH("이미 지원한 사용자 입니다.", HttpStatus.BAD_REQUEST);

  //오류 메시지
  private final String message;
  //오류 상태코드
  private final HttpStatus httpStatus;

  ErrorCode(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
