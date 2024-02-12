package com.kakao.techcampus.wekiki.page.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.techcampus.wekiki.pageInfo.controller.PageInfoController;
import com.kakao.techcampus.wekiki.pageInfo.controller.request.PageInfoRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql("/db/h2/import.sql")
public class PageInfoIntegrationTest {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PageInfoController pageInfoController;

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 사용자는_올바른_요청에_대해_pageId로_pageInfo와_Post_조회가_가능하다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/"+pageId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pageId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pageName").value("Test Page1"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[0].postId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[0].index").value("1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[0].postTitle").value("Test Title 1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[0].content").value("Test Content 1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[0].order").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[0].parentPostId").value(0));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[1].postId").value(2));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[1].index").value("2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[1].postTitle").value("Test Title 2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[1].content").value("Test Content 2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[1].order").value(2));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[1].parentPostId").value(0));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[2].postId").value(3));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[2].index").value("2.1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[2].postTitle").value("Test Title 2-1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[2].content").value("Test Content 2-1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[2].order").value(3));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[2].parentPostId").value(2));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[3].postId").value(4));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[3].index").value("2.2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[3].postTitle").value("Test Title 2-2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[3].content").value("Test Content 2-2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[3].order").value(4));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[3].parentPostId").value(2));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[4].postId").value(5));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[4].index").value("3"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[4].postTitle").value("Test Title 3"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[4].content").value("Test Content 3"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[4].order").value(5));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[4].parentPostId").value(0));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.goodCount").value(0));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.badCount").value(0));

    }

    @Test
    @WithUserDetails(value = "test2@test.com")
    void 해당_그룹을_이미_탈퇴한_회원이_pageId로_pageInfo와_Post_조회요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/"+pageId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 그룹에 속한 회원이 아닙니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test3@test.com")
    void 가입한적없는_회원이_pageId로_pageInfo와_Post_조회요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/"+pageId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 그룹에 속한 회원이 아닙니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 존재하지않는_페이지에_대해_pageId로_pageInfo와_Post_조회요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 100L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/" + groupId + "/page/" + pageId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("존재하지 않는 페이지 입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 사용자는_올바른_요청에_대해_pageInfo_생성이_가능하다() throws Exception {
        // given
        PageInfoRequest.createPageDTO request = new PageInfoRequest.createPageDTO();
        request.setPageName("새로운 페이지!!");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/page/create")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pageId").value(4));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pageName").value("새로운 페이지!!"));

    }

    @Test
    @WithUserDetails(value = "test2@test.com")
    void 해당_그룹을_이미_탈퇴한_회원이_pageInfo_생성요청시_404Exception을_응답한다() throws Exception {
        // given
        PageInfoRequest.createPageDTO request = new PageInfoRequest.createPageDTO();
        request.setPageName("새로운 페이지!!");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/page/create")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 그룹에 속한 회원이 아닙니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test3@test.com")
    void 가입한적없는_회원이_pageInfo_생성요청시_404Exception을_응답한다() throws Exception {
        // given
        PageInfoRequest.createPageDTO request = new PageInfoRequest.createPageDTO();
        request.setPageName("새로운 페이지!!");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/page/create")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 그룹에 속한 회원이 아닙니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 이미_존재하는_이름의_페이지랑_동일한_pageInfo_생성요청시_400Exception을_응답한다() throws Exception {
        // given
        PageInfoRequest.createPageDTO request = new PageInfoRequest.createPageDTO();
        request.setPageName("Test Page1");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/page/create")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("이미 존재하는 페이지입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 사용자는_올바른_요청에_대해_pageInfo_삭제가_가능하다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 2L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/page/"+pageId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pageId").value(2));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.title").value("Test Page2"));

    }

    @Test
    @WithUserDetails(value = "test2@test.com")
    void 해당_그룹을_이미_탈퇴한_회원이_pageInfo_삭제요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 2L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/page/"+pageId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 그룹에 속한 회원이 아닙니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test3@test.com")
    void 가입한적없는_회원이_pageInfo_삭제요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 2L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/page/"+pageId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 그룹에 속한 회원이 아닙니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 존재하지않는_페이지에_대해_pageInfo_삭제요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 100L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/page/"+pageId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("존재하지 않는 페이지 입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void post가_존재하는_페이지에_대해서_pageInfo_삭제요청시_400Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/page/"+pageId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("글이 적혀있는 페이지는 삭제가 불가능합니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 사용자는_올바른_요청에_대해_pageInfo_좋아요가_가능하다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/page/"+pageId+"/like")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pageId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.goodCount").value(1));

    }

    @Test
    @WithUserDetails(value = "test2@test.com")
    void 해당_그룹을_이미_탈퇴한_회원이_pageInfo_좋아요_요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/page/"+pageId+"/like")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 그룹에 속한 회원이 아닙니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test3@test.com")
    void 가입한적없는_회원이_pageInfo_좋아요_요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/page/"+pageId+"/like")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 그룹에 속한 회원이 아닙니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 존재하지않는_페이지에_대해_pageInfo_좋아요_요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 100L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/page/"+pageId+"/like")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("존재하지 않는 페이지 입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 사용자는_올바른_요청에_대해_pageInfo_싫어요가_가능하다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/page/"+pageId+"/hate")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pageId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.badCount").value(1));

    }

    @Test
    @WithUserDetails(value = "test2@test.com")
    void 해당_그룹을_이미_탈퇴한_회원이_pageInfo_싫어요_요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/page/"+pageId+"/hate")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 그룹에 속한 회원이 아닙니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test3@test.com")
    void 가입한적없는_회원이_pageInfo_싫어요_요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/page/"+pageId+"/hate")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 그룹에 속한 회원이 아닙니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 존재하지않는_페이지에_대해_pageInfo_싫어요_요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 100L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/page/"+pageId+"/hate")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("존재하지 않는 페이지 입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 사용자는_올바른_요청에_대해_pageInfo_목차_조회가_가능하다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/"+pageId+"/index")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        //result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pageId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pageName").value("Test Page1"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[0].postId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[0].index").value("1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[0].postTitle").value("Test Title 1"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[1].postId").value(2));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[1].index").value("2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[1].postTitle").value("Test Title 2"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[2].postId").value(3));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[2].index").value("2.1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[2].postTitle").value("Test Title 2-1"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[3].postId").value(4));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[3].index").value("2.2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[3].postTitle").value("Test Title 2-2"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[4].postId").value(5));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[4].index").value("3"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[4].postTitle").value("Test Title 3"));

    }

    @Test
    @WithUserDetails(value = "test2@test.com")
    void 해당_그룹을_이미_탈퇴한_회원이_페이지_목차_조회요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/"+pageId+"/index")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 그룹에 속한 회원이 아닙니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test3@test.com")
    void 가입한적없는_회원이_페이지_목차_조회요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/"+pageId+"/index")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 그룹에 속한 회원이 아닙니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 존재하지않는_페이지에_대해_페이지_목차_조회요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 100L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/"+pageId+"/index")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("존재하지 않는 페이지 입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void Post가_없는_페이지_목차_조회요청시_빈_Post를_반환합니다() throws Exception {
        // given
        Long groupId = 1L;
        Long pageId = 2L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/"+pageId+"/index")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        //result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pageId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pageName").value("Test Page2"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList").isEmpty());

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 사용자는_올바른_요청에_대해_pageInfo_제목으로_조회가_가능하다() throws Exception {
        // given
        Long groupId = 1L;
        String title = "Test Page1";

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page")
                        .param("title",title)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pageId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pageName").value("Test Page1"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[0].postId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[0].index").value("1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[0].postTitle").value("Test Title 1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[0].content").value("Test Content 1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[0].order").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[0].parentPostId").value(0));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[1].postId").value(2));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[1].index").value("2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[1].postTitle").value("Test Title 2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[1].content").value("Test Content 2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[1].order").value(2));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[1].parentPostId").value(0));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[2].postId").value(3));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[2].index").value("2.1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[2].postTitle").value("Test Title 2-1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[2].content").value("Test Content 2-1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[2].order").value(3));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[2].parentPostId").value(2));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[3].postId").value(4));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[3].index").value("2.2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[3].postTitle").value("Test Title 2-2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[3].content").value("Test Content 2-2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[3].order").value(4));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[3].parentPostId").value(2));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[4].postId").value(5));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[4].index").value("3"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[4].postTitle").value("Test Title 3"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[4].content").value("Test Content 3"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[4].order").value(5));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList[4].parentPostId").value(0));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.goodCount").value(0));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.badCount").value(0));

    }

    @Test
    @WithUserDetails(value = "test2@test.com")
    void 해당_그룹을_이미_탈퇴한_회원이_pageInfo_제목으로_조회요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        String title = "Test Page1";

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page")
                        .param("title",title)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 그룹에 속한 회원이 아닙니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test3@test.com")
    void 가입한적없는_회원이_pageInfo_제목으로_조회요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        String title = "Test Page1";

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page")
                        .param("title",title)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 그룹에 속한 회원이 아닙니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 존재하지않는_페이지에_대해_pageInfo_제목으로_조회요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        String title = "Test Page No Title";

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page")
                        .param("title",title)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("존재하지 않는 페이지 입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void Post가_없는_페이지에_대해_pageInfo_제목으로_조회요청시_빈_Post를_반환합니다() throws Exception {
        // given
        Long groupId = 1L;
        String title = "Test Page2";

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page")
                        .param("title",title)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        //result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pageId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pageName").value("Test Page2"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postList").isEmpty());

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 사용자는_올바른_요청에_대해_키워드로_pageInfo_검색이_가능하다() throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("keyword","Test");
        params.add("page","1");
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/search")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pages[0].pageId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pages[0].pageName").value("Test Page1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pages[0].content").value("Test Content 1"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pages[1].pageId").value(2));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pages[1].pageName").value("Test Page2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pages[1].content").value(""));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pages[2].pageId").value(3));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pages[2].pageName").value("Test Page3"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pages[2].content").value(""));

    }

    @Test
    @WithUserDetails(value = "test2@test.com")
    void 해당_그룹을_이미_탈퇴한_회원이_키워드로_pageInfo_검색요청시_404Exception을_응답한다() throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("keyword","Test");
        params.add("page","1");
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/search")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 그룹에 속한 회원이 아닙니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test3@test.com")
    void 가입한적없는_회원이_키워드로_pageInfo_검색요청시_404Exception을_응답한다() throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("keyword","Test");
        params.add("page","1");
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/search")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 그룹에 속한 회원이 아닙니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void Post가_존재하지_않는_Page에대해_키워드로_pageInfo_검색요청시_content에_빈문자열을_반환한다() throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("keyword","Test");
        params.add("page","1");
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/search")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pages[1].pageId").value(2));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pages[1].pageName").value("Test Page2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pages[1].content").value(""));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pages[2].pageId").value(3));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pages[2].pageName").value("Test Page3"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pages[2].content").value(""));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 키워드로_pageInfo_검색요청시_해당_keyword를_포함하는_페이지가_존재하지_않을_경우_빈페이지를_반환한다() throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("keyword","BUSAN");
        params.add("page","1");
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/search")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pages").isEmpty());

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 사용자는_올바른_요청에_대해_최근_바뀐_pageInfo_조회가_가능하다() throws Exception {
        // given
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/recent")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[0].pageId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[0].pageName").value("Test Page1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[0].updated_at").value("2024-02-09T00:00:00"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[1].pageId").value(2));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[1].pageName").value("Test Page2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[1].updated_at").value("2024-02-08T00:00:00"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[2].pageId").value(3));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[2].pageName").value("Test Page3"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[2].updated_at").value("2024-02-07T00:00:00"));

    }

    @Test
    @WithUserDetails(value = "test2@test.com")
    void 해당_그룹을_이미_탈퇴한_회원이_최근_바뀐_pageInfo_조회요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/recent")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 그룹에 속한 회원이 아닙니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test3@test.com")
    void 가입한적없는_회원이_최근_바뀐_pageInfo_조회요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/recent")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 그룹에 속한 회원이 아닙니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void Page가_10개_이상있을때_최근_바뀐_pageInfo_조회요청시_10개의_최근변경페이지만_반환한다() throws Exception {
        // given
        Long groupId = 1L;
        for(int i = 0 ; i < 20 ; i++){
            String title = "New Page for Link Test"+i;
            PageInfoRequest.createPageDTO request = new PageInfoRequest.createPageDTO();
            request.setPageName(title);
            pageInfoController.createPage(groupId,request);
        }

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/recent")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[0].pageId").value(23));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[0].pageName").value("New Page for Link Test19"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[1].pageId").value(22));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[1].pageName").value("New Page for Link Test18"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[2].pageId").value(21));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[2].pageName").value("New Page for Link Test17"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[3].pageId").value(20));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[3].pageName").value("New Page for Link Test16"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[4].pageId").value(19));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[4].pageName").value("New Page for Link Test15"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[5].pageId").value(18));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[5].pageName").value("New Page for Link Test14"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[6].pageId").value(17));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[6].pageName").value("New Page for Link Test13"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[7].pageId").value(16));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[7].pageName").value("New Page for Link Test12"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[8].pageId").value(15));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[8].pageName").value("New Page for Link Test11"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[9].pageId").value(14));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.recentPage[9].pageName").value("New Page for Link Test10"));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 사용자는_올바른_요청에_대해_페이지명으로_링크를위한_pageId_조회가_가능하다() throws Exception {
        // given
        Long groupId = 1L;
        String title = "New Page for Link Test";
        PageInfoRequest.createPageDTO request = new PageInfoRequest.createPageDTO();
        request.setPageName(title);
        pageInfoController.createPage(groupId,request);

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/link")
                        .param("title",title)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.pageId").value(4));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 존재하지않는_페이지에_대해_페이지명으로_링크를위한_pageId_조회요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 1L;
        String title = "No Page";

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/link")
                        .param("title",title)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("존재하지 않는 페이지 입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 존재하지않는_그룹에_대해_페이지명으로_링크를위한_pageId_조회요청시_404Exception을_응답한다() throws Exception {
        // given
        Long groupId = 100L;
        String title = "New Page for Link Test";
        PageInfoRequest.createPageDTO request = new PageInfoRequest.createPageDTO();
        request.setPageName(title);
        pageInfoController.createPage(1L,request);

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/page/link")
                        .param("title",title)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("존재하지 않는 페이지 입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

}
