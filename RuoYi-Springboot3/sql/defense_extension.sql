-- 中州养老实验答辩扩展：智能问答、来访管理、负责老人

drop table if exists nursing_resident_caregiver;
drop table if exists nursing_caregiver;
drop table if exists nursing_resident;
drop table if exists visitor_record;
drop table if exists visitor_appointment;

create table visitor_appointment (
  id                bigint not null auto_increment comment '预约ID',
  visit_type        tinyint not null default 0 comment '来访类型 0参观 1探访',
  visitor_name      varchar(20) not null comment '预约人姓名',
  visitor_phone     varchar(20) not null comment '预约人手机号',
  elder_name        varchar(20) default '' comment '老人姓名',
  appointment_time  datetime not null comment '预约时间',
  status            tinyint not null default 0 comment '状态 0待上门 1已完成 2已取消',
  arrival_time      datetime default null comment '到院时间',
  create_by         varchar(64) default '',
  create_time       datetime default null,
  update_by         varchar(64) default '',
  update_time       datetime default null,
  remark            varchar(500) default null,
  primary key (id)
) engine=innodb default charset=utf8mb4 comment='预约登记';

create table visitor_record (
  id                bigint not null auto_increment comment '来访记录ID',
  appointment_id    bigint default null comment '预约ID',
  visit_type        tinyint not null default 0 comment '来访类型 0参观 1探访',
  visitor_name      varchar(20) not null comment '来访人姓名',
  visitor_phone     varchar(20) not null comment '来访人手机号',
  elder_name        varchar(20) default '' comment '老人姓名',
  relation_name     varchar(30) default '' comment '与老人关系',
  visit_time        datetime not null comment '来访时间',
  create_by         varchar(64) default '',
  create_time       datetime default null,
  remark            varchar(500) default null,
  primary key (id)
) engine=innodb default charset=utf8mb4 comment='来访登记';

create table nursing_resident (
  id                bigint not null auto_increment,
  floor_no          int not null comment '楼层',
  room_no           varchar(20) not null comment '房间号',
  bed_no            varchar(20) not null comment '床位号',
  elder_name        varchar(20) not null comment '老人姓名',
  elder_gender      varchar(10) default '男',
  nursing_level     varchar(50) default '',
  status            tinyint not null default 1,
  primary key (id)
) engine=innodb default charset=utf8mb4 comment='在住老人床位';

create table nursing_caregiver (
  id                bigint not null auto_increment,
  name              varchar(20) not null,
  phone             varchar(20) default '',
  status            tinyint not null default 1,
  primary key (id)
) engine=innodb default charset=utf8mb4 comment='护理员';

create table nursing_resident_caregiver (
  id                bigint not null auto_increment,
  resident_id       bigint not null,
  caregiver_id      bigint not null,
  create_by         varchar(64) default '',
  create_time       datetime default null,
  primary key (id),
  unique key uk_resident_caregiver(resident_id, caregiver_id)
) engine=innodb default charset=utf8mb4 comment='老人护理员关系';

insert into visitor_appointment
  (visit_type, visitor_name, visitor_phone, elder_name, appointment_time, status, arrival_time, create_by, create_time, remark)
values
  (1, '余佳俊', '13875568891', '安欣', '2026-06-25 09:00:00', 0, null, 'admin', sysdate(), '携带水果探访'),
  (0, '高启强', '13975568892', '', '2026-06-25 10:30:00', 0, null, 'admin', sysdate(), '参观养老环境'),
  (1, '陈泰', '13775568893', '陈书婷', '2026-06-24 15:00:00', 1, '2026-06-24 14:55:00', 'admin', sysdate(), '已到院'),
  (1, '李有田', '13675568894', '李顺', '2026-06-23 15:30:00', 2, null, 'admin', sysdate(), '临时取消'),
  (0, '孟钰', '13575568895', '', '2026-06-20 09:30:00', 0, null, 'admin', sysdate(), '过期演示数据');

insert into visitor_record
  (appointment_id, visit_type, visitor_name, visitor_phone, elder_name, relation_name, visit_time, create_by, create_time, remark)
values
  (3, 1, '陈泰', '13775568893', '陈书婷', '父亲', '2026-06-24 14:55:00', 'admin', sysdate(), '预约到院自动生成'),
  (null, 0, '唐小龙', '13475568896', '', '参观访客', '2026-06-24 10:20:00', 'admin', sysdate(), '现场登记'),
  (null, 1, '程程', '13375568897', '高启盛', '朋友', '2026-06-23 16:10:00', 'admin', sysdate(), '现场登记');

insert into nursing_resident(floor_no, room_no, bed_no, elder_name, elder_gender, nursing_level, status) values
  (1, '101', '1011', '安欣', '男', '二级护理等级', 1),
  (1, '101', '1012', '高启强', '男', '三级护理等级', 1),
  (1, '101', '1013', '高启盛', '男', '一级护理等级', 1),
  (1, '102', '1021', '陈泰', '男', '二级护理等级', 1),
  (1, '102', '1022', '李有田', '男', '三级护理等级', 1),
  (1, '102', '1023', '徐江', '男', '一级护理等级', 1),
  (2, '201', '2011', '陈书婷', '女', '三级护理等级', 1),
  (2, '201', '2012', '孟钰', '女', '二级护理等级', 1),
  (2, '201', '2013', '程程', '女', '一级护理等级', 1),
  (2, '202', '2021', '唐小龙', '男', '二级护理等级', 1),
  (2, '202', '2022', '唐小虎', '男', '二级护理等级', 1),
  (2, '202', '2023', '杨健', '男', '三级护理等级', 1),
  (3, '301', '3011', '老默', '男', '一级护理等级', 1),
  (3, '301', '3012', '黄瑶', '女', '三级护理等级', 1),
  (3, '301', '3013', '谭思言', '男', '二级护理等级', 1);

insert into nursing_caregiver(name, phone, status) values
  ('顾廷烨', '13800001001', 1),
  ('盛明兰', '13800001002', 1),
  ('盛如兰', '13800001003', 1),
  ('盛华兰', '13800001004', 1),
  ('盛长柏', '13800001005', 1);

insert into nursing_resident_caregiver(resident_id, caregiver_id, create_by, create_time) values
  (1, 1, 'admin', sysdate()), (1, 2, 'admin', sysdate()),
  (2, 1, 'admin', sysdate()), (2, 3, 'admin', sysdate()),
  (3, 2, 'admin', sysdate()), (3, 4, 'admin', sysdate()),
  (4, 3, 'admin', sysdate()), (5, 4, 'admin', sysdate()),
  (7, 1, 'admin', sysdate()), (7, 5, 'admin', sysdate()),
  (8, 2, 'admin', sysdate()), (9, 3, 'admin', sysdate()),
  (10, 4, 'admin', sysdate()), (11, 5, 'admin', sysdate()),
  (13, 1, 'admin', sysdate()), (14, 2, 'admin', sysdate());

delete from sys_role_menu where menu_id in (204, 300, 301, 302, 400, 401, 1230, 1231, 1300, 1301, 1302, 1310, 1311, 1400);
delete from sys_menu where menu_id in (204, 300, 301, 302, 400, 401, 1230, 1231, 1300, 1301, 1302, 1310, 1311, 1400);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache,
   menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values
  (204, '负责老人', 200, 4, 'responsible', 'nursing/responsible/index', '', '', 1, 0,
   'C', '0', '0', 'nursing:responsible:list', 'caregiver', 'admin', sysdate(), '', null, '负责老人及护理员设置'),
  (300, '来访管理', 0, 3, 'visit', null, '', '', 1, 0,
   'M', '0', '0', '', 'user', 'admin', sysdate(), '', null, '预约和来访登记'),
  (301, '预约登记', 300, 1, 'appointment', 'visit/appointment/index', '', '', 1, 0,
   'C', '0', '0', 'visit:appointment:list', 'date', 'admin', sysdate(), '', null, '预约登记'),
  (302, '来访登记', 300, 2, 'record', 'visit/record/index', '', '', 1, 0,
   'C', '0', '0', 'visit:record:list', 'form', 'admin', sysdate(), '', null, '来访登记'),
  (400, '中州智询', 0, 5, 'aiconsult', null, '', '', 1, 0,
   'M', '0', '0', '', 'message', 'admin', sysdate(), '', null, '中州养老智能问答'),
  (401, '小智养老', 400, 1, 'virtualagent', 'aiconsult/virtualagent/index', '', 'VirtualAgent', 1, 0,
   'C', '0', '0', 'aiconsult:virtualagent:view', 'question', 'admin', sysdate(), '', null, 'AI养老咨询助手');

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache,
   menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values
  (1230, '负责老人查询', 204, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:responsible:list', '#', 'admin', sysdate(), '', null, ''),
  (1231, '设置护理员', 204, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:responsible:edit', '#', 'admin', sysdate(), '', null, ''),
  (1300, '预约查询', 301, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'visit:appointment:list', '#', 'admin', sysdate(), '', null, ''),
  (1301, '预约新增', 301, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'visit:appointment:add', '#', 'admin', sysdate(), '', null, ''),
  (1302, '预约状态', 301, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'visit:appointment:arrive', '#', 'admin', sysdate(), '', null, ''),
  (1310, '来访查询', 302, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'visit:record:list', '#', 'admin', sysdate(), '', null, ''),
  (1311, '来访新增', 302, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'visit:record:add', '#', 'admin', sysdate(), '', null, ''),
  (1400, '智能问答访问', 401, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'aiconsult:virtualagent:view', '#', 'admin', sysdate(), '', null, '');

insert ignore into sys_role_menu(role_id, menu_id)
select 2, menu_id from sys_menu where menu_id in (204, 300, 301, 302, 400, 401, 1230, 1231, 1300, 1301, 1302, 1310, 1311, 1400);

-- 答辩环境隐藏若依框架自带的演示与运维菜单。
update sys_menu set visible = '1' where menu_id in (1, 2, 3, 4);
update sys_menu set menu_name = '服务管理', order_num = 3, icon = 'server' where menu_id = 200;
update sys_menu set order_num = 2, icon = 'user' where menu_id = 300;
update sys_menu set order_num = 4, icon = 'message' where menu_id = 400;
update sys_user set nick_name = '管理员' where user_name = 'admin';
