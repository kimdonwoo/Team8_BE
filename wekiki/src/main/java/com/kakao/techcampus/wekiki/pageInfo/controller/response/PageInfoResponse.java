package com.kakao.techcampus.wekiki.pageInfo.controller.response;

import com.kakao.techcampus.wekiki.group.domain.Group;
import com.kakao.techcampus.wekiki.pageInfo.domain.PageInfo;
import com.kakao.techcampus.wekiki.post.domain.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class PageInfoResponse {

    @Getter
    public static class mainPageDTO {
        List<GroupDTO> myGroup;
        List<GroupDTO> officialGroup;
        List<GroupDTO> unOfficialGroup;
        public mainPageDTO(List<GroupDTO> myGroup, List<GroupDTO> officialGroup, List<GroupDTO> unOfficialGroup) {
            this.myGroup = myGroup;
            this.officialGroup = officialGroup;
            this.unOfficialGroup = unOfficialGroup;
        }
        public mainPageDTO(List<GroupDTO> officialGroup, List<GroupDTO> unOfficialGroup) {
            this.officialGroup = officialGroup;
            this.unOfficialGroup = unOfficialGroup;
        }
        @Getter
        public static class GroupDTO {
            Long groupId;
            String groupImage;
            String groupName;
            public GroupDTO(Group group) {
                this.groupId = group.getId();
                this.groupImage = group.getGroupProfileImage();
                this.groupName = group.getGroupName();
            }
        }
    }

    @Getter @Setter
    public static class deletePageDTO{

        Long pageId;
        String title;

        public deletePageDTO(PageInfo pageInfo){
            this.pageId = pageInfo.getId();
            this.title = pageInfo.getPageName();
        }
    }

    @Getter @Setter
    public static class createPageDTO{

        Long pageId;
        String pageName;

        public createPageDTO(PageInfo pageInfo){
            this.pageId = pageInfo.getId();
            this.pageName = pageInfo.getPageName();
        }
    }

    @Getter @Setter
    public static class likePageDTO{

        Long pageId;
        String pageName;
        int goodCount;

        public likePageDTO(PageInfo pageInfo){
            this.pageId = pageInfo.getId();
            this.pageName = pageInfo.getPageName();
            this.goodCount = pageInfo.getGoodCount();
        }
    }

    @Getter @Setter
    public static class hatePageDTO{

        Long pageId;
        String pageName;
        int badCount;

        public hatePageDTO(PageInfo pageInfo){
            this.pageId = pageInfo.getId();
            this.pageName = pageInfo.getPageName();
            this.badCount = pageInfo.getBadCount();
        }
    }

    @Getter @Setter
    public static class searchPageDTO{

        List<pageDTO> pages;

        public searchPageDTO(List<pageDTO> pageDTO){
            this.pages = pageDTO;
        }

        @Getter @Setter
        public static class pageDTO{

            Long pageId;
            String pageName;
            String content;

            public pageDTO(PageInfo pageInfo ,String content){
                this.pageId = pageInfo.getId();
                this.pageName = pageInfo.getPageName();
                this.content = content;
            }
        }
    }


    @Getter
    @Setter
    public static class getPageIndexDTO{
        String pageName;
        List<postDTO> postList;

        public getPageIndexDTO(PageInfo pageInfo , List<postDTO> postList){
            this.pageName = pageInfo.getPageName();
            this.postList = postList;
        }


        @Getter
        @Setter
        public static class postDTO {
            Long postId;
            String index;
            String postTitle;

            public postDTO(Post post, String index){
                this.postId = post.getId();
                this.index = index;
                this.postTitle = post.getTitle();
            }
        }
    }

    @Getter
    @Setter
    public static class getPageFromIdDTO{

        Long pageId;
        String pageName;
        List<postDTO> postList;
        int goodCount;
        int badCount;

        public getPageFromIdDTO(PageInfo pageInfo , List<postDTO> postList){
            this.pageId = pageInfo.getId();
            this.pageName = pageInfo.getPageName();
            this.postList = postList;
            this.goodCount = pageInfo.getGoodCount();
            this.badCount = pageInfo.getBadCount();
        }


        @Getter
        @Setter
        public static class postDTO {
            Long postId;
            String index;
            String postTitle;
            String content;

            int order;
            Long parentPostId;

            public postDTO(Post post, String index){
                this.postId = post.getId();
                this.index = index;
                this.postTitle = post.getTitle();
                this.content = post.getContent();
                this.order = post.getOrders();
                if(post.getParent() != null){
                    this.parentPostId = post.getParent().getId();
                }else{
                    this.parentPostId = 0L;
                }
            }
        }
    }


    @Getter @Setter
    public static class getRecentPageDTO{

        List<recentPageDTO> recentPage;

        public getRecentPageDTO(List<recentPageDTO> recentPage){
            this.recentPage = recentPage;
        }

        @Getter @Setter
        public static class recentPageDTO{
            Long pageId;
            String pageName;
            private LocalDateTime updated_at;

            public recentPageDTO(PageInfo pageInfo){
                this.pageId = pageInfo.getId();
                this.pageName = pageInfo.getPageName();
                this.updated_at = pageInfo.getUpdated_at();
            }
        }

    }

    @Getter @Setter
    public static class getPageLinkDTO{
        Long pageId;

        public getPageLinkDTO(Long pageId){
            this.pageId = pageId;
        }
    }

}