package com.kakao.techcampus.wekiki.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.techcampus.wekiki.post.controller.request.PostRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql("/db/h2/import.sql")
public class PostIntegrationTest {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 사용자는_올바른_요청에_대해_post_생성이_가능하다() throws Exception{
        // given
        PostRequest.createPostDTO request = new PostRequest.createPostDTO();
        request.setPageId(1L);
        request.setParentPostId(0L);
        request.setOrder(6);
        request.setTitle("새로운 Post의 title");
        request.setContent("새로운 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/create")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postId").value(6));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.title").value("새로운 Post의 title"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.content").value("새로운 Post의 content"));

    }

    @Test
    @WithUserDetails(value = "test2@test.com")
    void 해당_그룹을_이미_탈퇴한_회원이_Post_생성요청시_404Exception을_응답한다() throws Exception{
        // given
        PostRequest.createPostDTO request = new PostRequest.createPostDTO();
        request.setPageId(1L);
        request.setParentPostId(0L);
        request.setOrder(6);
        request.setTitle("새로운 Post의 title");
        request.setContent("새로운 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/create")
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
    void 해당_회원이_가입한적없는_회원이_Post생성요청시_404Exception을_응답한다() throws Exception{
        // given
        PostRequest.createPostDTO request = new PostRequest.createPostDTO();
        request.setPageId(1L);
        request.setParentPostId(0L);
        request.setOrder(6);
        request.setTitle("새로운 Post의 title");
        request.setContent("새로운 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/create")
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
    void 존재하지_않는_페이지에_대해서_Post생성요청시_404Exception을_응답한다() throws Exception{
        // given
        PostRequest.createPostDTO request = new PostRequest.createPostDTO();
        request.setPageId(100L);
        request.setParentPostId(0L);
        request.setOrder(6);
        request.setTitle("새로운 Post의 title");
        request.setContent("새로운 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/create")
                        .content(requestBody)
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
    void 존재하지않는_상위_Post에_대한_새로운_하위_Post_생성요청시_404Exception을_응답한다() throws Exception{
        // given
        PostRequest.createPostDTO request = new PostRequest.createPostDTO();
        request.setPageId(1L);
        request.setParentPostId(100L);
        request.setOrder(6);
        request.setTitle("새로운 Post의 title");
        request.setContent("새로운 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/create")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("존재하지 않는 상위 글입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void post_생성요청시_양수가아닌_groupId값로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        PostRequest.createPostDTO request = new PostRequest.createPostDTO();
        request.setPageId(1L);
        request.setParentPostId(0L);
        request.setOrder(6);
        request.setTitle("새로운 Post의 title");
        request.setContent("새로운 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 0L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/create")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("createPost.arg0: 유효하지 않은 groupID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void post_생성요청시_양수가아닌_pageId값로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        PostRequest.createPostDTO request = new PostRequest.createPostDTO();
        request.setPageId(0L);
        request.setParentPostId(0L);
        request.setOrder(6);
        request.setTitle("새로운 Post의 title");
        request.setContent("새로운 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/create")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Validation error: 유효하지 않은 pageID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void post_생성요청시_음수인_parentPostId값로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        PostRequest.createPostDTO request = new PostRequest.createPostDTO();
        request.setPageId(1L);
        request.setParentPostId(-1L);
        request.setOrder(6);
        request.setTitle("새로운 Post의 title");
        request.setContent("새로운 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/create")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Validation error: 유효하지 않은 parentPostID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void post_생성요청시_음수인_order값로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        PostRequest.createPostDTO request = new PostRequest.createPostDTO();
        request.setPageId(1L);
        request.setParentPostId(0L);
        request.setOrder(-1);
        request.setTitle("새로운 Post의 title");
        request.setContent("새로운 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/create")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Validation error: 유효하지 않은 order입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void post_생성요청시_title이_비어있으면_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        PostRequest.createPostDTO request = new PostRequest.createPostDTO();
        request.setPageId(1L);
        request.setParentPostId(0L);
        request.setOrder(6);
        request.setTitle("");
        request.setContent("새로운 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/create")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Validation error: 게시글 제목을 입력해주세요."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void post_생성요청시_title이_200자가_넘으면_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        PostRequest.createPostDTO request = new PostRequest.createPostDTO();
        request.setPageId(1L);
        request.setParentPostId(0L);
        request.setOrder(6);
        request.setTitle("새로운 Post의 title 새로운 Post의 title 새로운 Post의 title 새로운 Post의 title" +
                "새로운 Post의 title 새로운 Post의 title 새로운 Post의 title 새로운 Post의 title 새로운 Post의 title" +
                "새로운 Post의 title 새로운 Post의 title 새로운 Post의 title 새로운 Post의 title 새로운 Post의 title" +
                "새로운 Post의 title 새로운 Post의 title 새로운 Post의 title 새로운 Post의 title");
        request.setContent("새로운 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/create")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Validation error: 게시글 제목은 최대 200자까지 가능합니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void post_생성요청시_content가_10000자가_넘으면_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        PostRequest.createPostDTO request = new PostRequest.createPostDTO();
        request.setPageId(1L);
        request.setParentPostId(0L);
        request.setOrder(6);
        request.setTitle("새로운 Post의 title");
        String str = "새로운 Content";
        for(int i = 0 ; i < 1000; i++){
            str+="새로운 Content";
        }
        request.setContent(str);
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/create")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Validation error: 게시글 내용은 최대 10000자 이내로 가능합니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 사용자는_올바른_요청에_대해_post_수정이_가능하다() throws Exception {
        // given
        PostRequest.modifyPostDTO request = new PostRequest.modifyPostDTO();
        request.setPostId(1L);
        request.setTitle("수정된 Post의 title");
        request.setContent("수정된 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                put("/group/"+groupId+"/post/modify")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.title").value("수정된 Post의 title"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.content").value("수정된 Post의 content"));

    }

    @Test
    @WithUserDetails(value = "test2@test.com")
    void 해당_그룹을_이미_탈퇴한_회원이_Post수정요청시_404Exception을_응답한다() throws Exception{
        // given
        PostRequest.modifyPostDTO request = new PostRequest.modifyPostDTO();
        request.setPostId(1L);
        request.setTitle("수정된 Post의 title");
        request.setContent("수정된 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                put("/group/"+groupId+"/post/modify")
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
    void 해당_회원이_가입한적없는_회원이_Post수정요청시_404Exception을_응답한다() throws Exception{
        // given
        PostRequest.modifyPostDTO request = new PostRequest.modifyPostDTO();
        request.setPostId(1L);
        request.setTitle("수정된 Post의 title");
        request.setContent("수정된 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                put("/group/"+groupId+"/post/modify")
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
    void 존재하지_않는_Post에_대해서_Post수정요청시_404Exception을_응답한다() throws Exception{
        // given
        PostRequest.modifyPostDTO request = new PostRequest.modifyPostDTO();
        request.setPostId(100L);
        request.setTitle("수정된 Post의 title");
        request.setContent("수정된 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                put("/group/"+groupId+"/post/modify")
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
    void 동일한_내용으로_Post수정요청시_400Exception을_응답한다() throws Exception{
        // given
        PostRequest.modifyPostDTO request = new PostRequest.modifyPostDTO();
        request.setPostId(1L);
        request.setTitle("Test Title 1");
        request.setContent("Test Content 1");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                put("/group/"+groupId+"/post/modify")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("기존 글과 동일한 글입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void post_수정요청시_양수가아닌_groupId값로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        PostRequest.modifyPostDTO request = new PostRequest.modifyPostDTO();
        request.setPostId(1L);
        request.setTitle("수정된 Post의 title");
        request.setContent("수정된 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 0L;

        // when
        ResultActions result = mockMvc.perform(
                put("/group/"+groupId+"/post/modify")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("modifyPost.arg0: 유효하지 않은 groupID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void post_수정요청시_양수가아닌_postId값로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        PostRequest.modifyPostDTO request = new PostRequest.modifyPostDTO();
        request.setPostId(0L);
        request.setTitle("수정된 Post의 title");
        request.setContent("수정된 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                put("/group/"+groupId+"/post/modify")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Validation error: 유효하지 않은 postID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void post_수정요청시_title값이_빈배열이면_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        PostRequest.modifyPostDTO request = new PostRequest.modifyPostDTO();
        request.setPostId(1L);
        request.setTitle("");
        request.setContent("수정된 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                put("/group/"+groupId+"/post/modify")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Validation error: 게시글 제목을 입력해주세요."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void post_수정요청시_title이_200자가_넘으면_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        PostRequest.modifyPostDTO request = new PostRequest.modifyPostDTO();
        request.setPostId(1L);
        request.setTitle("수정된 Post의 title 수정된 Post의 title 수정된 Post의 title 수정된 Post의 title" +
                "수정된 Post의 title 수정된 Post의 title 수정된 Post의 title 수정된 Post의 title 수정된 Post의 title" +
                "수정된 Post의 title 수정된 Post의 title 수정된 Post의 title 수정된 Post의 title 수정된 Post의 title" +
                "수정된 Post의 title 수정된 Post의 title 수정된 Post의 title 수정된 Post의 title");
        request.setContent("수정된 Post의 content");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                put("/group/"+groupId+"/post/modify")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Validation error: 게시글 제목은 최대 200자까지 가능합니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void post_수정요청시_content가_10000자를_넘으면_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        PostRequest.modifyPostDTO request = new PostRequest.modifyPostDTO();
        request.setPostId(1L);
        request.setTitle("수정된 Post의 title");
        String str = "수정된 Content";
        for(int i = 0 ; i < 1000; i++){
            str+="수정된 Content";
        }
        request.setContent(str);
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                put("/group/"+groupId+"/post/modify")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Validation error: 게시글 내용은 최대 10000자 이내로 가능합니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 사용자는_올바른_요청에_대해_Post_삭제가_가능하다() throws Exception {
        // given
        Long groupId = 1L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/post/"+postId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.title").value("Test Title 1"));

    }

    @Test
    @WithUserDetails(value = "test2@test.com")
    void 해당_그룹을_이미_탈퇴한_회원이_Post삭제요청시_404Exception을_응답한다() throws Exception{
        // given
        Long groupId = 1L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/post/"+postId)
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
    void 해당_회원이_가입한적없는_회원이_Post삭제요청시_404Exception을_응답한다() throws Exception{
        // given
        Long groupId = 1L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/post/"+postId)
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
    void 존재하지_않는_Post에_대해서_Post삭제요청시_404Exception을_응답한다() throws Exception{
        // given
        Long groupId = 1L;
        Long postId = 100L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/post/"+postId)
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
    void 하위_글이_존재하는_Post삭제요청시_400Exception을_응답한다() throws Exception{
        // given
        Long groupId = 1L;
        Long postId = 2L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/post/"+postId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("하위 글이 존재하는 글은 삭제가 불가능합니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void Post_삭제요청시_양수가아닌_groupId값로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        Long groupId = 0L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/post/"+postId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("deletePost.arg0: 유효하지 않은 groupID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void Post_삭제요청시_양수가아닌_postId값로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        Long groupId = 1L;
        Long postId = 0L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/group/"+groupId+"/post/"+postId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("deletePost.arg1: 유효하지 않은 postID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 사용자는_올바른_요청에_대해_post_히스토리_조회가_가능하다() throws Exception {
        // given
        Long groupId = 1L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/post/"+postId+"/history")
                        .param("page","1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.postId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.currentTitle").value("Test Title 1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.historyList[0].memberId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.historyList[0].nickName").value("test Nickname1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.historyList[0].historyId").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.historyList[0].title").value("Test Title 1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.historyList[0].content").value("Test Content 1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.historyList[0].createdAt").value("2024-02-09T00:00:00"));

    }

    @Test
    @WithUserDetails(value = "test2@test.com")
    void 해당_그룹을_이미_탈퇴한_회원이_Post의_History조회요청시_404Exception을_응답한다() throws Exception{

        // given
        Long groupId = 1L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/post/"+postId+"/history")
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
    void 해당_회원이_가입한적없는_회원이_Post의_History조회요청시_404Exception을_응답한다() throws Exception{

        // given
        Long groupId = 1L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/post/"+postId+"/history")
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
    void 존재하지_않는_Post에_대해서_Post의_History조회요청시_404Exception을_응답한다() throws Exception{

        // given
        Long groupId = 1L;
        Long postId = 100L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/post/"+postId+"/history")
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
    public void Post의_History조회요청시_양수가아닌_groupId값로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        Long groupId = 0L;
        Long postId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/post/"+postId+"/history")
                        .param("page","1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("getPostHistory.arg0: 유효하지 않은 groupID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void Post의_History조회요청시_양수가아닌_postId값로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        Long groupId = 1L;
        Long postId = 0L;

        // when
        ResultActions result = mockMvc.perform(
                get("/group/"+groupId+"/post/"+postId+"/history")
                        .param("page","1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("getPostHistory.arg1: 유효하지 않은 postID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    void 사용자는_올바른_요청에_대해_Post_신고가_가능하다() throws Exception {

        // given
        PostRequest.createReportDTO request = new PostRequest.createReportDTO();
        request.setPostId(1L);
        request.setContent("해당 post 신고합니다!");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/report")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.reportId").value(1));

    }

    @Test
    @WithUserDetails(value = "test2@test.com")
    void 해당_그룹을_이미_탈퇴한_회원이_Post의_신고요청시_404Exception을_응답한다() throws Exception{
        // given
        PostRequest.createReportDTO request = new PostRequest.createReportDTO();
        request.setPostId(1L);
        request.setContent("해당 post 신고합니다!");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/report")
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
    void 해당_회원이_가입한적없는_회원이_Post의_신고요청시_404Exception을_응답한다() throws Exception{
        // given
        PostRequest.createReportDTO request = new PostRequest.createReportDTO();
        request.setPostId(1L);
        request.setContent("해당 post 신고합니다!");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/report")
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
    void 존재하지_않는_Post에_대해서_Post의_신고요청시_404Exception을_응답한다() throws Exception{
        // given
        PostRequest.createReportDTO request = new PostRequest.createReportDTO();
        request.setPostId(100L);
        request.setContent("해당 post 신고합니다!");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/report")
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
    public void Post_신고요청시_양수가아닌_groupId값로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        PostRequest.createReportDTO request = new PostRequest.createReportDTO();
        request.setPostId(1L);
        request.setContent("해당 post 신고합니다!");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 0L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/report")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("createReport.arg0: 유효하지 않은 groupID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void Post_신고요청시_양수가아닌_postId값로_요청을_보낼시_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        PostRequest.createReportDTO request = new PostRequest.createReportDTO();
        request.setPostId(0L);
        request.setContent("해당 post 신고합니다!");
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/report")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Validation error: 유효하지 않은 postID입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }

    @Test
    @WithUserDetails(value = "test1@test.com")
    public void Post_신고요청시_content가_500자를_초과하면_validation에_의해_400Exception을_응답한다() throws Exception {

        // given
        PostRequest.createReportDTO request = new PostRequest.createReportDTO();
        request.setPostId(1L);
        String str = "해당 post 신고합니다!";
        for(int i = 0 ; i < 50; i++){
            str+="해당 post 신고합니다!";
        }
        request.setContent(str);
        String requestBody = om.writeValueAsString(request);
        Long groupId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/group/"+groupId+"/post/report")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Validation error: 신고글 내용은 최대 500자 이내로 가능합니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));

    }
}
