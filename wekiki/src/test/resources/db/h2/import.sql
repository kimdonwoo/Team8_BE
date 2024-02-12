-- 모든 제약 조건 비활성화
SET REFERENTIAL_INTEGRITY FALSE;
truncate table member_tb;
truncate table group_tb;
truncate table group_member_tb;
truncate table pageinfo_tb;
truncate table post_tb;
truncate table comment_tb;
truncate table history_tb;
truncate table report_tb;
SET REFERENTIAL_INTEGRITY TRUE;
-- 모든 제약 조건 활성화

INSERT INTO member_tb (`id`, `email`, `password`, `name`, `authority`, `created_at`) VALUES ('1', 'test1@test.com', 'test1test', 'testman1', 'user', '2024-02-09 00:00:00.000000');
INSERT INTO member_tb (`id`, `email`, `password`, `name`, `authority`, `created_at`) VALUES ('2', 'test2@test.com', 'test2test', 'testman2', 'user', '2024-02-09 00:00:00.000000');
INSERT INTO member_tb (`id`, `email`, `password`, `name`, `authority`, `created_at`) VALUES ('3', 'test3@test.com', 'test3test', 'testman3', 'user', '2024-02-09 00:00:00.000000');
INSERT INTO member_tb (`id`, `email`, `password`, `name`, `authority`, `created_at`) VALUES ('4', 'test4@test.com', 'test4test', 'testman4', 'user', '2024-02-09 00:00:00.000000');

INSERT INTO group_tb (`id`,`group_type`,`group_name`,`member_count`,`created_at`) VALUES ('1','GroupEntity','테스트 그룹1','0','2024-02-09 00:00:00.000000');
INSERT INTO group_tb (`id`,`group_type`,`group_name`,`member_count`,`created_at`) VALUES ('2','GroupEntity','테스트 그룹2','0','2024-02-09 00:00:00.000000');

INSERT INTO group_member_tb (`id`,`group_entity_id`,`member_entity_id`,`nick_name`,`active_status`,`member_level`,`created_at`) VALUES ('1','1','1','test Nickname1','1','0','2024-02-09 00:00:00.000000');
INSERT INTO group_member_tb (`id`,`group_entity_id`,`member_entity_id`,`nick_name`,`active_status`,`member_level`,`created_at`) VALUES ('2','1','2','test Nickname2','0','0','2024-02-09 00:00:00.000000');
INSERT INTO group_member_tb (`id`,`group_entity_id`,`member_entity_id`,`nick_name`,`active_status`,`member_level`,`created_at`) VALUES ('3','1','4','test Nickname3','1','0','2024-02-09 00:00:00.000000');

INSERT INTO pageinfo_tb (`id`,`group_entity_id`,`page_name`,`bad_count`,`good_count`,`view_count`,`created_at`,`updated_at`) VALUES ('1','1','Test Page1','0','0','0','2024-02-09 00:00:00.000000','2024-02-09 00:00:00.000000');
INSERT INTO pageinfo_tb (`id`,`group_entity_id`,`page_name`,`bad_count`,`good_count`,`view_count`,`created_at`,`updated_at`) VALUES ('2','1','Test Page2','0','0','0','2024-02-08 00:00:00.000000','2024-02-08 00:00:00.000000');
INSERT INTO pageinfo_tb (`id`,`group_entity_id`,`page_name`,`bad_count`,`good_count`,`view_count`,`created_at`,`updated_at`) VALUES ('3','1','Test Page3','0','0','0','2024-02-07 00:00:00.000000','2024-02-07 00:00:00.000000');

INSERT INTO post_tb (`id`,`parent_id`,`page_info_entity_id`,`group_member_entity_id`,`orders`,`title`,`content`,`created_at`) VALUES ('1',null,'1','1','1','Test Title 1','Test Content 1','2024-02-09 00:00:00.000000');
INSERT INTO history_tb (`id`,`group_member_entity_id`,`post_id`, `title`,`content`,`created_at`) VALUES ('1','1','1','Test Title 1','Test Content 1','2024-02-09 00:00:00.000000');
INSERT INTO post_tb (`id`,`parent_id`,`page_info_entity_id`,`group_member_entity_id`,`orders`,`title`,`content`,`created_at`) VALUES ('2',null,'1','1','2','Test Title 2','Test Content 2','2024-02-09 00:00:00.000000');
INSERT INTO history_tb (`id`,`group_member_entity_id`,`post_id`, `title`,`content`,`created_at`) VALUES ('2','1','2','Test Title 2','Test Content 2','2024-02-09 00:00:00.000000');
INSERT INTO post_tb (`id`,`parent_id`,`page_info_entity_id`,`group_member_entity_id`,`orders`,`title`,`content`,`created_at`) VALUES ('3','2','1','1','3','Test Title 2-1','Test Content 2-1','2024-02-09 00:00:00.000000');
INSERT INTO history_tb (`id`,`group_member_entity_id`,`post_id`, `title`,`content`,`created_at`) VALUES ('3','1','3','Test Title 2-1','Test Content 2-1','2024-02-09 00:00:00.000000');
INSERT INTO post_tb (`id`,`parent_id`,`page_info_entity_id`,`group_member_entity_id`,`orders`,`title`,`content`,`created_at`) VALUES ('4','2','1','1','4','Test Title 2-2','Test Content 2-2','2024-02-09 00:00:00.000000');
INSERT INTO history_tb (`id`,`group_member_entity_id`,`post_id`, `title`,`content`,`created_at`) VALUES ('4','1','4','Test Title 2-2','Test Content 2-2','2024-02-09 00:00:00.000000');
INSERT INTO post_tb (`id`,`parent_id`,`page_info_entity_id`,`group_member_entity_id`,`orders`,`title`,`content`,`created_at`) VALUES ('5',null,'1','1','5','Test Title 3','Test Content 3','2024-02-09 00:00:00.000000');
INSERT INTO history_tb (`id`,`group_member_entity_id`,`post_id`, `title`,`content`,`created_at`) VALUES ('5','1','5','Test Title 3','Test Content 3','2024-02-09 00:00:00.000000');

INSERT INTO comment_tb (`id`,`post_entity_id`,`group_member_entity_id`,`content`,`created_at`) VALUES ('1','1','1','Test Comment1','2024-02-09 00:00:00.000000');
INSERT INTO comment_tb (`id`,`post_entity_id`,`group_member_entity_id`,`content`,`created_at`) VALUES ('2','1','1','Test Comment2','2024-02-09 00:00:00.000000');
INSERT INTO comment_tb (`id`,`post_entity_id`,`group_member_entity_id`,`content`,`created_at`) VALUES ('3','1','1','Test Comment3','2024-02-09 00:00:00.000000');
