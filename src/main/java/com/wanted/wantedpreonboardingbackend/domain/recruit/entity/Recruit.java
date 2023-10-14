package com.wanted.wantedpreonboardingbackend.domain.recruit.entity;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import com.wanted.wantedpreonboardingbackend.domain.matching.entity.Matching;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "t_recruit")
public class Recruit {

  public static final int MAX_POSITION_LENGTH = 50;
  public static final int MAX_SKILL_LENGTH = 50;

  //채용공고 ID
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  //회사 (N:1)
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "company_id", nullable = false)
  private Company company;

  //채용 포지션명
  @Column(name = "position", nullable = false, length = MAX_POSITION_LENGTH)
  private String position;

  //보상금
  @Column(name = "reward", nullable = false)
  private Integer reward;

  //채용 내용
  @Column(name = "content", nullable = false, columnDefinition = "TEXT")
  private String content;

  //사용 기술명
  @Column(name = "skill", nullable = false, length = MAX_SKILL_LENGTH)
  private String skill;

  //지원 현황
  @OneToMany(mappedBy = "recruit", cascade = ALL)
  private final List<Matching> matchings = new ArrayList<>();

  // 테스트용 빌더
  @Builder(builderMethodName = "allBuilder")
  public Recruit(Long id, Company company, String position, Integer reward, String content,
      String skill) {
    this.id = id;
    this.company = company;
    this.position = position;
    this.reward = reward;
    this.content = content;
    this.skill = skill;
  }

  @Builder
  public Recruit(Company company, String position, Integer reward, String content, String skill) {
    this.company = company;
    this.position = position;
    this.reward = reward;
    this.content = content;
    this.skill = skill;
  }


  public void update(Recruit recruit) {
    this.position = recruit.position != null ? recruit.position : this.position;
    this.reward = recruit.reward != null ? recruit.reward : this.reward;
    this.content = recruit.content != null ? recruit.content : this.content;
    this.skill = recruit.skill != null ? recruit.skill : this.skill;
  }
}
