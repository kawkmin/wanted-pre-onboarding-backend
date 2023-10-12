package com.wanted.wantedpreonboardingbackend.domain.matching.entity;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;
import com.wanted.wantedpreonboardingbackend.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자가 지원한 채용공고에 관한 정보
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "t_matching")
public class Matching {

  //매칭 ID
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  //유저 (N:1)
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  //채용공고 (N:1)
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "recruit_id", nullable = false)
  private Recruit recruit;

  //지원 상태
  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
  private StateEnum state;

  /**
   * 지원 상태를 관리하는 Eum
   */
  public enum StateEnum {
    대기중, 불합격, 합격
  }
}
