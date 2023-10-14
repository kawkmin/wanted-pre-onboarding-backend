package com.wanted.wantedpreonboardingbackend.domain.recruit.dao;

import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecruitRepository extends JpaRepository<Recruit, Long> {

  /**
   * 검색명에 하나라도 일치
   *
   * @param search 검색명
   * @return 조건에 맞는 채용공고 리스트
   */
  @Query(
      "SELECT r " +
          "FROM Recruit r " +
          "join fetch r.company " +
          "WHERE lower(r.content) LIKE lower(concat('%',:search,'%') )" +
          "OR lower(r.position) LIKE lower(concat('%',:search,'%') )" +
          "OR lower(r.skill) LIKE lower(concat('%',:search,'%') )" +
          "OR lower(r.company.name) LIKE lower(concat('%',:search,'%') )" +
          "OR lower(r.company.region) LIKE lower(concat('%',:search,'%') )" +
          "OR lower(r.company.country) LIKE lower(concat('%',:search,'%') )"
  )
  List<Recruit> searchRecruits(@Param("search") String search);

}
