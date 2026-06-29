-- 服务管理 - 任务安排

drop table if exists nursing_task;

create table nursing_task (
  id                    bigint not null auto_increment,
  resident_id           bigint not null comment '老人ID',
  project_id            bigint not null comment '护理项目ID',
  caregiver_id          bigint not null comment '护理员ID',
  task_type              tinyint not null default 0 comment '项目类型 0计划内 1计划外',
  status                 tinyint not null default 0 comment '状态 0待执行 1已执行 2已取消',
  related_order_no       varchar(50) default '' comment '关联单据',
  expected_service_time datetime not null comment '期望服务时间',
  execute_time           datetime default null comment '执行时间',
  execute_image          varchar(500) default '' comment '执行图片',
  execute_record         varchar(500) default '' comment '执行记录',
  cancel_time            datetime default null comment '取消时间',
  cancel_reason          varchar(500) default '' comment '取消原因',
  create_by              varchar(64) default '',
  create_time            datetime default null,
  update_by              varchar(64) default '',
  update_time            datetime default null,
  remark                 varchar(500) default null,
  primary key (id)
) engine=innodb default charset=utf8mb4 comment='护理任务安排';

insert into nursing_task
  (resident_id, project_id, caregiver_id, task_type, status, related_order_no,
   expected_service_time, create_by, create_time, remark)
values
  (1, 3, 1, 1, 0, 'ZD202606250001', '2026-06-26 09:00:00', 'admin', sysdate(), '饭后进行口腔清洁'),
  (2, 4, 2, 1, 0, 'ZD202606250002', '2026-06-26 10:00:00', 'admin', sysdate(), '使用老人自带洗发用品'),
  (3, 1, 3, 0, 0, 'JH202606250003', '2026-06-26 14:30:00', 'admin', sysdate(), '注意防滑和保暖'),
  (4, 2, 4, 0, 0, 'JH202606250004', '2026-06-27 11:30:00', 'admin', sysdate(), '协助老人进餐'),
  (5, 5, 5, 1, 0, 'ZD202606250005', '2026-06-27 15:00:00', 'admin', sysdate(), '足部基础护理');

insert into nursing_task
  (resident_id, project_id, caregiver_id, task_type, status, related_order_no,
   expected_service_time, execute_time, execute_image, execute_record,
   create_by, create_time, update_by, update_time, remark)
values
  (7, 4, 1, 1, 1, 'ZD202606240006', '2026-06-24 09:00:00', '2026-06-24 09:10:00',
   '/static/service/hy-002.png', '已完成洗头护理，老人状态良好，无不适反应。',
   'admin', '2026-06-23 15:00:00', 'admin', '2026-06-24 09:10:00', '使用老人自带洗发水'),
  (8, 6, 2, 0, 1, 'JH202606240007', '2026-06-24 10:30:00', '2026-06-24 10:35:00',
   '/static/service/hy-006.png', '完成手部指甲修剪并清洁。',
   'admin', '2026-06-23 15:00:00', 'admin', '2026-06-24 10:35:00', '注意避免剪伤'),
  (9, 8, 3, 0, 1, 'JH202606240008', '2026-06-24 15:00:00', '2026-06-24 15:05:00',
   '/static/service/hy-008.png', '完成30分钟康复步行训练。',
   'admin', '2026-06-23 15:00:00', 'admin', '2026-06-24 15:05:00', '全程陪同');

insert into nursing_task
  (resident_id, project_id, caregiver_id, task_type, status, related_order_no,
   expected_service_time, cancel_time, cancel_reason,
   create_by, create_time, update_by, update_time, remark)
values
  (10, 1, 4, 1, 2, 'ZD202606230009', '2026-06-23 09:00:00', '2026-06-22 17:30:00',
   '老人身体不适，家属申请取消本次助浴。',
   'admin', '2026-06-22 14:00:00', 'admin', '2026-06-22 17:30:00', '注意后续健康状况'),
  (11, 7, 5, 0, 2, 'JH202606230010', '2026-06-23 14:00:00', '2026-06-23 10:00:00',
   '老人外出就医，无法按时执行。',
   'admin', '2026-06-22 14:00:00', 'admin', '2026-06-23 10:00:00', '返回后重新安排');

delete from sys_role_menu where menu_id in (205, 206, 1240, 1241, 1242, 1243, 1244);
delete from sys_menu where menu_id in (205, 206, 1240, 1241, 1242, 1243, 1244);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache,
   menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values
  (205, '任务安排', 200, 5, 'task', 'nursing/task/index', '', 'NursingTask', 1, 0,
   'C', '0', '0', 'nursing:task:list', 'job', 'admin', sysdate(), '', null, '护理任务安排');

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache,
   menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values
  (1240, '任务查询', 205, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:task:query', '#', 'admin', sysdate(), '', null, ''),
  (1241, '任务执行', 205, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:task:execute', '#', 'admin', sysdate(), '', null, ''),
  (1242, '任务取消', 205, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:task:cancel', '#', 'admin', sysdate(), '', null, ''),
  (1243, '任务修改', 205, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:task:edit', '#', 'admin', sysdate(), '', null, '');

insert ignore into sys_role_menu(role_id, menu_id)
select 2, menu_id from sys_menu where menu_id in (205, 1240, 1241, 1242, 1243);
