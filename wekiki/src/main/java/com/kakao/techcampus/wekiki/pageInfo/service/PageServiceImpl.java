package com.kakao.techcampus.wekiki.pageInfo.service;

import com.kakao.techcampus.wekiki._core.error.exception.Exception400;
import com.kakao.techcampus.wekiki._core.error.exception.Exception404;
import com.kakao.techcampus.wekiki._core.utils.port.RedisUtils;
import com.kakao.techcampus.wekiki.group.domain.Group;
import com.kakao.techcampus.wekiki.group.domain.GroupMember;
import com.kakao.techcampus.wekiki.group.service.port.GroupMemberRepository;
import com.kakao.techcampus.wekiki.group.service.port.GroupRepository;
import com.kakao.techcampus.wekiki.member.domain.Member;
import com.kakao.techcampus.wekiki.member.service.port.MemberRepository;
import com.kakao.techcampus.wekiki.pageInfo.controller.port.PageInfoCreateService;
import com.kakao.techcampus.wekiki.pageInfo.controller.port.PageInfoDeleteService;
import com.kakao.techcampus.wekiki.pageInfo.controller.port.PageInfoReadService;
import com.kakao.techcampus.wekiki.pageInfo.controller.port.PageInfoUpdateService;
import com.kakao.techcampus.wekiki.pageInfo.controller.response.PageInfoResponse;
import com.kakao.techcampus.wekiki.pageInfo.domain.PageInfo;
import com.kakao.techcampus.wekiki.pageInfo.service.port.PageIndexGenerator;
import com.kakao.techcampus.wekiki.pageInfo.service.port.PageRepository;
import com.kakao.techcampus.wekiki.post.domain.Post;
import com.kakao.techcampus.wekiki.post.service.port.PostRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
@Builder
public class PageServiceImpl implements PageInfoCreateService, PageInfoDeleteService, PageInfoReadService, PageInfoUpdateService {

    private final PageRepository pageRepository;
    private final PostRepository postRepository;
    private final PageIndexGenerator pageIndexGenerator;
    private final RedisUtils redisUtils;

    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;

    final int PAGE_COUNT = 10;
    final int RECENTLY_PAGE_COUNT = 10;
    @Getter
    final String GROUP_PREFIX = "GROUP_";

    @Override
    @Transactional
    public PageInfoResponse.getPageIndexDTO getPageIndex(Long groupId,Long memberId, Long pageId){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        checkGroupMember(memberId, groupId);

        // 2. pageId로 PageInfo + Post 객체 fetch join으로 한번에 들고오기
        PageInfo PageInfo = getPageAndPostFromPageId(pageId);

        // 3. 목차 생성하기
        HashMap<Long, String> indexs = pageIndexGenerator.createIndex(PageInfo.getPosts());

        // 4. DTO로 return
        List<PageInfoResponse.getPageIndexDTO.postDTO> temp = PageInfo.getPosts().stream()
                .map(p -> new PageInfoResponse.getPageIndexDTO.postDTO(p, indexs.get(p.getId())))
                .collect(Collectors.toList());

        log.info(memberId + " 님이 " + groupId + "의 "  + pageId + " 페이지 목차 조회 API를 호출하였습니다. ");
        return new PageInfoResponse.getPageIndexDTO(PageInfo, temp);

    }

    @Override
    @Transactional
    public PageInfoResponse.deletePageDTO deletePage(Long memberId, Long groupId, Long pageId){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        checkGroupMember(memberId, groupId);

        // 2. 존재하는 페이지 인지 체크
        PageInfo PageInfo = checkPageFromPageId(pageId);

        // 3. pageId로 하위 post들이 존재하는지 확인 -> 존재하면 Exception
        if(postRepository.existsByPageInfoId(pageId)){
            throw new Exception400("글이 적혀있는 페이지는 삭제가 불가능합니다.");
        }

        // 4. 포스트가 하나도 없으면 삭제시키기
        PageInfoResponse.deletePageDTO response = new PageInfoResponse.deletePageDTO(PageInfo);
        pageRepository.deleteById(pageId);

        // 5. redis에 페이지 목록 삭제 시켜주기
        redisUtils.deleteHashValue(GROUP_PREFIX+groupId, PageInfo.getPageName());

        // 6. return DTO
        log.info(memberId + " 님이 " + groupId + "의 "  + pageId + " 페이지 삭제 API를 호출하였습니다. ");
        return response;
    }

    @Override
    @Transactional
    public PageInfoResponse.getPageFromIdDTO getPageFromId(Long memberId,Long groupId, Long pageId){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        checkGroupMember(memberId, groupId);

        // 2. pageId로 PageInfo + Post 객체 fetch join으로 한번에 들고오기
        PageInfo PageInfo = getPageAndPostFromPageId(pageId);

        // 3. 목차 생성하기
        HashMap<Long, String> indexs = pageIndexGenerator.createIndex(PageInfo.getPosts());

        // 4. DTO로 return
        List<PageInfoResponse.getPageFromIdDTO.postDTO> temp = PageInfo.getPosts().stream()
                .map(p -> new PageInfoResponse.getPageFromIdDTO.postDTO(p, indexs.get(p.getId())))
                .collect(Collectors.toList());

        log.info(memberId + " 님이 " + groupId + "의 "  + pageId + " 페이지 조회 API를 호출하였습니다. ");
        return new PageInfoResponse.getPageFromIdDTO(PageInfo, temp);

    }

    @Override
    @Transactional
    public PageInfoResponse.createPageDTO createPage(String title, Long groupId, Long memberId){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        GroupMember activeGroupMember = checkGroupMember(memberId, groupId);

        // 2. 그룹 내 동일한 title의 Page가 존재하는지 체크
        if(pageRepository.findByTitle(groupId,title).isPresent()){
            throw new Exception400("이미 존재하는 페이지입니다.");
        }

        // 3. Page 생성
        PageInfo newPageInfo = PageInfo.from(activeGroupMember.getGroup(),title);

        // 4. Page 저장
        PageInfo savedPageInfo = pageRepository.save(newPageInfo);

        // 5. Redis에 Hash 자료구조로 pageID 저장
        redisUtils.saveKeyAndHashValue(GROUP_PREFIX+groupId,title, savedPageInfo.getId().toString());

        // 6. return DTO
        log.info(memberId + " 님이 " + groupId + " 그룹에서 "  + title + " 페이지를 생성하였습니다.");
        return new PageInfoResponse.createPageDTO(savedPageInfo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PageInfoResponse.likePageDTO likePage(Long pageId , Long groupId, Long memberId){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        checkGroupMember(memberId, groupId);

        // 2. 존재하는 페이지 인지 체크
        PageInfo PageInfo = checkPageFromPageId(pageId);

        // 3. 페이지 goodCount 증가
        PageInfoResponse.likePageDTO response = new PageInfoResponse.likePageDTO(pageRepository.update(PageInfo.plusGoodCount()));

        // 4. return DTO
        log.info(memberId + " 님이 " + groupId + " 그룹에서 "  + pageId + " 페이지 좋아요를 눌렀습니다.");
        return response;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PageInfoResponse.hatePageDTO hatePage(Long pageId , Long groupId, Long memberId){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        checkGroupMember(memberId, groupId);

        // 2. 존재하는 페이지 인지 체크
        PageInfo PageInfo = checkPageFromPageId(pageId);

        // 3. 페이지 goodCount 증가
        PageInfoResponse.hatePageDTO response = new PageInfoResponse.hatePageDTO(pageRepository.update(PageInfo.plusBadCount()));

        // 4. return DTO
        log.info(memberId + " 님이 " + groupId + " 그룹에서 "  + pageId + " 페이지 싫어요를 눌렀습니다.");
        return response;
    }

    @Override
    @Transactional
    public PageInfoResponse.searchPageDTO searchPage(Long groupId, Long memberId, int pageNo, String keyword){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        checkGroupMember(memberId, groupId);

        // 2. 페이지네이션 적용하여 Page 이름으로 keyword를 들고있는 페이지 들고오기
        List<PageInfo> pageInfos = pageRepository.findPages(groupId, keyword, PageRequest.of(pageNo, PAGE_COUNT)).getContent();

        // 3. 가져온 페이지들 중에 첫 포스트 가져오기
        List<Post> posts = postRepository.findPostInPages(pageInfos);

        // 4. responseDTO 만들기
        List<PageInfoResponse.searchPageDTO.pageDTO> res = new ArrayList<>();

        boolean flag;
        for(PageInfo p : pageInfos) { // 최대 10개
            flag = false;
            for (Post po : posts) { // 최대 10개
                if (p.getId() == po.getPageInfo().getId()) {
                    res.add(new PageInfoResponse.searchPageDTO.pageDTO(p, po.getContent()));
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                res.add(new PageInfoResponse.searchPageDTO.pageDTO(p, ""));
            }
        }

        // 5. pages로 DTO return DTO
        log.info(memberId + " 님이 " + groupId + " 그룹에서 "  + keyword + " 키워드로 페이지 검색을 요청하였습니다.");
        return new PageInfoResponse.searchPageDTO(res);
    }

    @Override
    @Transactional
    public PageInfoResponse.getRecentPageDTO getRecentPage(Long memberId , Long groupId){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        checkGroupMember(memberId, groupId);

        // 2. 특정 groupId를 가진 Page들 order by로 updated_at이 최신인 10개 Page 조회
        List<PageInfo> recentPage = pageRepository.findByGroupIdOrderByUpdatedAtDesc(groupId, PageRequest.of(0, RECENTLY_PAGE_COUNT));

        // 3. return DTO
        List<PageInfoResponse.getRecentPageDTO.recentPageDTO> collect = recentPage.stream().map(pageInfo ->
                new PageInfoResponse.getRecentPageDTO.recentPageDTO(pageInfo)).collect(Collectors.toList());

        log.info(memberId + " 님이 " + groupId + " 그룹에서 최근 변경/생성된 페이지 10개을 조회합니다.");
        return new PageInfoResponse.getRecentPageDTO(collect);

    }

    @Override
    @Transactional
    public PageInfoResponse.getPageFromIdDTO getPageFromTitle(Long memberId, Long groupId, String title){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        checkGroupMember(memberId, groupId);

        // 2. groupId랑 title로 Page있는지 확인 (fetch join으로 post들 가져오기)
        String value = redisUtils.getHashValue(GROUP_PREFIX + groupId, title);

        if(value == null){
            throw new Exception404("존재하지 않는 페이지 입니다.");
        }

        // 2. pageId로 PageInfo + Post 객체 fetch join으로 한번에 들고오기
        PageInfo page = getPageAndPostFromPageId(Long.parseLong(value));

        // 3. 목차 생성하기
        HashMap<Long, String> indexs = pageIndexGenerator.createIndex(page.getPosts());

        // 4. DTO로 return
        List<PageInfoResponse.getPageFromIdDTO.postDTO> temp = page.getPosts().stream()
                .map(p -> new PageInfoResponse.getPageFromIdDTO.postDTO(p, indexs.get(p.getId())))
                .collect(Collectors.toList());

        log.info(memberId + " 님이 " + groupId + " 그룹에서 "+ title + " 페이지를 조회합니다.");
        return new PageInfoResponse.getPageFromIdDTO(page, temp);
    }

    @Override
    @Transactional
    public PageInfoResponse.getPageLinkDTO getPageLink(Long groupId, String title){

        // 1. redis로 groupId_title을 key로 value 받아오기 (페이지 테이블에 접근할 필요 x)
        String value = redisUtils.getHashValue(GROUP_PREFIX + groupId, title);
        log.info(groupId + " 그룹에서 " + title + " 페이지의 링크를 요청합니다.");

        if(value == null){
            throw new Exception404("존재하지 않는 페이지 입니다.");
        }else{
            // 2. return DTO
            return new PageInfoResponse.getPageLinkDTO(Long.valueOf(value));
        }
    }

    @Override
    @Transactional
    public PageInfoResponse.mainPageDTO getMainPage(Long memberId) {
        if(SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            // 로그인 안한 사람
            log.info("로그인하지 않은 사람의 메인 페이지 조회");
            List<PageInfoResponse.mainPageDTO.GroupDTO> officialGroupList = getOfficialGroupList();
            List<PageInfoResponse.mainPageDTO.GroupDTO> unOfficialGroupList = getUnLoginUnOfficialGroupList();
            return new PageInfoResponse.mainPageDTO(officialGroupList, unOfficialGroupList);
        }
        else {
            //로그인을 한 사람
            log.info("로그인을 한 사람의 메인 페이지 조회");
            Optional<Member> member = memberRepository.findById(memberId);
            if(member.isEmpty()) {
                log.error("회원이 존재하지 않습니다.");
                throw new Exception400("없는 회원입니다.");
            }
            List<Group> myGroupList = member.get().getGroupMembers().stream()
                    .filter(GroupMember::isActiveStatus).map(GroupMember::getGroup).toList();
            List<Long> myGroupIdList = myGroupList.stream().map(Group::getId).toList();
            List<PageInfoResponse.mainPageDTO.GroupDTO> myGroupListDTO = getMyGroupList(myGroupList);
            List<PageInfoResponse.mainPageDTO.GroupDTO> officialGroupList = getOfficialGroupList();
            List<PageInfoResponse.mainPageDTO.GroupDTO> unOfficialGroupList = getLoginUnOfficialGroupList(myGroupIdList);
            return new PageInfoResponse.mainPageDTO(myGroupListDTO, officialGroupList, unOfficialGroupList);
        }
    }

    private List<PageInfoResponse.mainPageDTO.GroupDTO> getMyGroupList (List<Group> myGroupList) {
        return myGroupList.stream()
                .map(PageInfoResponse.mainPageDTO.GroupDTO::new)
                .collect(Collectors.toList());
    }
    private List<PageInfoResponse.mainPageDTO.GroupDTO> getOfficialGroupList () {
        return groupRepository.findAllOfficialGroup().stream()
                .map(PageInfoResponse.mainPageDTO.GroupDTO::new)
                .limit(3)
                .collect(Collectors.toList());
    }

    private List<PageInfoResponse.mainPageDTO.GroupDTO> getUnLoginUnOfficialGroupList() {
        return groupRepository.findAllUnOfficialOpenGroup().stream()
                .map(PageInfoResponse.mainPageDTO.GroupDTO::new)
                .limit(8)
                .collect(Collectors.toList());
    }

    private List<PageInfoResponse.mainPageDTO.GroupDTO> getLoginUnOfficialGroupList(List<Long> myGroupIdList) {
        return groupRepository.findAllUnOfficialOpenGroup().stream()
                .map(PageInfoResponse.mainPageDTO.GroupDTO::new)
                .filter(tempGroup -> !myGroupIdList.contains(tempGroup.getGroupId()))
                .limit(8)
                .toList();
    }

    public GroupMember checkGroupMember(Long memberId, Long groupId){

        GroupMember activeGroupMember = groupMemberRepository.findGroupMemberByMemberIdAndGroupIdFetchJoin(memberId, groupId)
                .orElseThrow(() -> new Exception404("해당 그룹에 속한 회원이 아닙니다."));
        if(!activeGroupMember.isActiveStatus()) throw new Exception404("해당 그룹에 속한 회원이 아닙니다.");
        if(activeGroupMember.getMember() == null) throw new Exception404("존재하지 않는 회원입니다.");
        if(activeGroupMember.getGroup() == null) throw new Exception404("존재하지 않는 그룹입니다.");

        return activeGroupMember;
    }


    public PageInfo checkPageFromPageId(Long pageId){
        return pageRepository.findById(pageId)
                .orElseThrow(() -> new Exception404("존재하지 않는 페이지 입니다."));
    }

    public PageInfo getPageAndPostFromPageId(Long pageId){
        return pageRepository.findByPageIdWithPosts(pageId)
                .orElseThrow(() -> new Exception404("존재하지 않는 페이지 입니다."));
    }
}
