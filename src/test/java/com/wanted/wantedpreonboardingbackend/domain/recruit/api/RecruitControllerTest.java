package com.wanted.wantedpreonboardingbackend.domain.recruit.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.wantedpreonboardingbackend.TestHelper;
import com.wanted.wantedpreonboardingbackend.domain.company.application.CompanyService;
import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import com.wanted.wantedpreonboardingbackend.domain.recruit.application.RecruitService;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dto.request.RecruitCreateReqDto;
import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class RecruitControllerTest extends TestHelper {

  @InjectMocks
  private RecruitController recruitController;
  @Mock
  private RecruitService recruitService;
  @Mock
  private CompanyService companyService;
  private MockMvc mockMvc;
  private ObjectMapper objectMapper = new ObjectMapper();

  private Recruit recruit;
  private Company company;

  @BeforeEach
  void setUp() {
    super.init();
    recruit = recruitTestHelper.generate();
    company = recruitTestHelper.generate().getCompany();

    mockMvc = MockMvcBuilders
        .standaloneSetup(recruitController)
        .build();
  }

  @Nested
  @DisplayName("채용공고 생성 관련 테스트")
  class createRecruit {

    private RecruitCreateReqDto reqDto;

    @BeforeEach
    void setUp() {
      reqDto = RecruitCreateReqDto.builder()
          .companyId(1L)
          .position(recruit.getPosition())
          .content(recruit.getContent())
          .reward(recruit.getReward())
          .skill(recruit.getSkill())
          .build();
    }

    @Test
    @DisplayName("채용공고 생성에 성공한다.")
    void 채용공고_생성에_성공한다() throws Exception {
      given(recruitService.createRecruit(any(), any())).willReturn(1L); //service에서 통과한 테스트. any
      given(companyService.getCompanyById(anyLong())).willReturn(company);

      mockMvc.perform(post("/api/v1/recruit")
              .content(objectMapper.writeValueAsString(reqDto))
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isCreated());
    }
  }
}