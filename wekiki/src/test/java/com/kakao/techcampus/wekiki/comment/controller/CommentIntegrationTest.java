package com.kakao.techcampus.wekiki.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.techcampus.wekiki.comment.controller.request.CommentRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql("/db/h2/import.sql")
public class CommentIntegrationTest {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void 사용자는_올바른_요청에_대해_댓글조회가_가능하다() throws Exception {

        // given
        Long groupId = 1L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/post/"+postId+"/comment")
                        .param("page","1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.comments[0].commentId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.comments[0].nickName").value("test Nickname1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.comments[0].content").value("Test Comment1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.comments[0].createdAt").value("2024-02-09T00:00:00"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.comments[0].mine").value(true));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.comments[1].commentId").value(2));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.comments[1].nickName").value("test Nickname1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.comments[1].content").value("Test Comment2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.comments[1].createdAt").value("2024-02-09T00:00:00"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.comments[1].mine").value(true));

        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.comments[2].commentId").value(3));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.comments[2].nickName").value("test Nickname1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.comments[2].content").value("Test Comment3"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.comments[2].createdAt").value("2024-02-09T00:00:00"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.comments[2].mine").value(true));

    }

    @Test
    @WithUserDetails(value = "test2@test.com")
    public void 해당_그룹을_이미_탈퇴한_회원이_댓글조회요청시_404Exception을_응답한다() throws Exception {

        // given
        Long groupId = 1L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/post/"+postId+"/comment")
                        .param("page","1")
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
    public void 해당_회원이_가입한적없는_회원이_댓글조회요청시_404Exception을_응답한다() throws Exception {

        // given
        Long groupId = 1L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/post/"+postId+"/comment")
                        .param("page","1")
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
    public void 존재하지_않는_게시글의_댓글에_대해서_조회요청시_404Exception을_응답한다() throws Exception {


        // given
        Long groupId = 1L;
        Long postId = 100L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/post/"+postId+"/comment")
                        .param("page","1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("존재하지 않는 글 입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void 게시글의_댓글에_대해서_조회요청시_양수가아닌_groupId값으로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        Long groupId = 0L;
        Long postId = 1L;
        int page = 1;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/post/"+postId+"/comment")
                        .param("page",String.valueOf(page))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("getComment.arg0: 유효하지 않은 groupID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void 게시글의_댓글에_대해서_조회요청시_양수가아닌_postId값으로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        Long groupId = 1L;
        Long postId = 0L;
        int page = 1;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/post/"+postId+"/comment")
                        .param("page",String.valueOf(page))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("getComment.arg1: 유효하지 않은 postID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void 게시글의_댓글에_대해서_조회요청시_양수가아닌_page값으로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        Long groupId = 1L;
        Long postId = 1L;
        int page = 0;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/post/"+postId+"/comment")
                        .param("page",String.valueOf(page))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("getComment.arg2: 유효하지 않은 pageID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void 사용자는_올바른_요청에_대해_댓글생성이_가능하다() throws Exception {

        // given
        CommentRequest.createComment request = new CommentRequest.createComment();
        request.setContent("create Comment Integration Test");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/"+postId+"/comment")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.commentId").value(4));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.content").value("create Comment Integration Test"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.nickName").value("test Nickname1"));

    }

    @Test
    @WithUserDetails(value = "test2@test.com")
    public void 해당_그룹을_이미_탈퇴한_회원이_댓글생성요청시_404Exception을_응답한다() throws Exception {

        // given
        CommentRequest.createComment request = new CommentRequest.createComment();
        request.setContent("create Comment Integration Test");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/"+postId+"/comment")
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
    public void 해당_회원이_가입한적없는_회원이_댓글생성요청시_404Exception을_응답한다() throws Exception {

        // given
        CommentRequest.createComment request = new CommentRequest.createComment();
        request.setContent("create Comment Integration Test");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/"+postId+"/comment")
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
    public void 존재하지_않는_게시글에_대해서_댓글생성요청시_404Exception을_응답한다() throws Exception {

        // given
        CommentRequest.createComment request = new CommentRequest.createComment();
        request.setContent("create Comment Integration Test");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;
        Long postId = 100L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/"+postId+"/comment")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("존재하지 않는 글 입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void 게시글의_댓글생성요청시_양수가아닌_groupId값으로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        CommentRequest.createComment request = new CommentRequest.createComment();
        request.setContent("create Comment Integration Test");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 0L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/"+postId+"/comment")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("createComment.arg0: 유효하지 않은 groupID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void 게시글의_댓글생성요청시_양수가아닌_postId값으로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        CommentRequest.createComment request = new CommentRequest.createComment();
        request.setContent("create Comment Integration Test");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;
        Long postId = 0L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/"+postId+"/comment")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("createComment.arg1: 유효하지 않은 postID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void 게시글의_댓글생성요청시_댓글_요청이_빈문자열이면_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        CommentRequest.createComment request = new CommentRequest.createComment();
        request.setContent("");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/"+postId+"/comment")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Validation error: 댓글 내용을 입력해주세요."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void 게시글의_댓글생성요청시_댓글_글자수가_200자를_초과하면_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        CommentRequest.createComment request = new CommentRequest.createComment();
        request.setContent("create Comment Integration Test create Comment Integration Test " +
                "create Comment Integration Test create Comment Integration Test create Comment Integration Test " +
                "create Comment Integration Test create Comment Integration Test create Comment Integration Test");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/"+postId+"/comment")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        assertThat(request.getContent().length()).isGreaterThan(200);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Validation error: 댓글 작성은 200자 이내로 가능합니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void 사용자는_올바른_요청에_대해_댓글수정이_가능하다() throws Exception {

        // given
        CommentRequest.updateComment request = new CommentRequest.updateComment();
        request.setContent("update Comment Controller Test");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;
        Long commentId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                patch("/group/"+groupId+"/comment/"+commentId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.commentId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.newContent").value("update Comment Controller Test"));

    }

    @Test
    @WithUserDetails(value = "test2@test.com")
    public void 해당_그룹을_이미_탈퇴한_회원이_댓글수정요청시_404Exception을_응답한다() throws Exception {

        // given
        CommentRequest.updateComment request = new CommentRequest.updateComment();
        request.setContent("update Comment Controller Test");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;
        Long commentId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                patch("/group/"+groupId+"/comment/"+commentId)
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
    public void 해당_회원이_가입한적없는_회원이_댓글수정요청시_404Exception을_응답한다() throws Exception {

        // given
        CommentRequest.updateComment request = new CommentRequest.updateComment();
        request.setContent("update Comment Controller Test");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;
        Long commentId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                patch("/group/"+groupId+"/comment/"+commentId)
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
    public void 존재하지_않는_댓글에_대해서_수정요청시_404Exception을_응답한다() throws Exception {

        // given
        CommentRequest.updateComment request = new CommentRequest.updateComment();
        request.setContent("update Comment Controller Test");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;
        Long commentId = 100L;

        // when
        ResultActions result = mockMvc.perform(
                patch("/group/"+groupId+"/comment/"+commentId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("존재하지 않는 댓글 입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test4@test.com")
    public void 댓글작성자가_아닌_사용자가_수정요청시_400Exception을_응답한다() throws Exception {

        // given
        CommentRequest.updateComment request = new CommentRequest.updateComment();
        request.setContent("update Comment Controller Test");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;
        Long commentId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                patch("/group/"+groupId+"/comment/"+commentId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("본인이 쓴 댓글만 수정이 가능합니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void 기존_댓글_내용과_동일하게_수정요청시_400Exception을_응답한다() throws Exception {

        // given
        CommentRequest.updateComment request = new CommentRequest.updateComment();
        request.setContent("Test Comment1");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;
        Long commentId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                patch("/group/"+groupId+"/comment/"+commentId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("기존 댓글과 동일한 내용입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void 게시글의_댓글수정요청시_양수가아닌_groupId값으로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        CommentRequest.updateComment request = new CommentRequest.updateComment();
        request.setContent("update Comment Controller Test");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 0L;
        Long commentId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                patch("/group/"+groupId+"/comment/"+commentId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("updateComment.arg0: 유효하지 않은 groupID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void 게시글의_댓글수정요청시_양수가아닌_commentId값으로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        CommentRequest.updateComment request = new CommentRequest.updateComment();
        request.setContent("update Comment Controller Test");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;
        Long commentId = 0L;

        // when
        ResultActions result = mockMvc.perform(
                patch("/group/"+groupId+"/comment/"+commentId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("updateComment.arg1: 유효하지 않은 commentID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void 게시글의_댓글수정요청시_댓글_요청이_빈문자열이면_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        CommentRequest.createComment request = new CommentRequest.createComment();
        request.setContent("");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/"+postId+"/comment")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Validation error: 댓글 내용을 입력해주세요."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void 게시글의_댓글수정요청시_댓글_글자수가_200자를_초과하면_요청을_보내면_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        CommentRequest.createComment request = new CommentRequest.createComment();
        request.setContent("update Comment Controller Test update Comment Controller Test update Comment Controller Test" +
                " update Comment Controller Test update Comment Controller Test update Comment Controller Test update Comment Controller Test" +
                " update Comment Controller Test update Comment Controller Test update Comment Controller Test update Comment Controller Test");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/"+postId+"/comment")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        assertThat(request.getContent().length()).isGreaterThan(200);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Validation error: 댓글 작성은 200자 이내로 가능합니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void 사용자는_올바른_요청에_대해_댓글삭제가_가능하다() throws Exception {

        // given
        Long groupId = 1L;
        Long commentId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/comment/"+commentId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.commentId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.content").value("Test Comment1"));

    }

    @Test
    @WithUserDetails(value = "test2@test.com")
    public void 해당_그룹을_이미_탈퇴한_회원이_댓글삭제요청시_404Exception을_응답한다() throws Exception {

        // given
        Long groupId = 1L;
        Long commentId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/comment/"+commentId)
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
    public void 해당_회원이_가입한적없는_회원이_댓글삭제요청시_404Exception을_응답한다() throws Exception {

        // given
        Long groupId = 1L;
        Long commentId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/comment/"+commentId)
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
    public void 존재하지_않는_댓글에_대해서_삭제요청시_404Exception을_응답한다() throws Exception {

        // given
        Long groupId = 1L;
        Long commentId = 100L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/comment/"+commentId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("존재하지 않는 댓글 입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test4@test.com")
    public void 댓글작성자가_아닌_사용자가_삭제요청시_400Exception을_응답한다() throws Exception {

        // given
        Long groupId = 1L;
        Long commentId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/comment/"+commentId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("본인이 쓴 댓글만 삭제가 가능합니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void 게시글의_댓글삭제요청시_양수가아닌_groupId값으로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        Long groupId = 0L;
        Long commentId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/comment/"+commentId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("deleteComment.arg0: 유효하지 않은 groupID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void 게시글의_댓글삭제요청시_양수가아닌_commentId값으로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        Long groupId = 1L;
        Long commentId = 0L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/comment/"+commentId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("deleteComment.arg1: 유효하지 않은 commentID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

}
