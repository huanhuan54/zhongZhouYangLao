-- ----------------------------
-- 作业5：中州养老护理管理模块
-- ----------------------------

drop table if exists nursing_level;
drop table if exists nursing_plan_project;
drop table if exists nursing_plan;
drop table if exists nursing_project;

create table nursing_project (
  id                    bigint(20)      not null auto_increment    comment '护理项目ID',
  name                  varchar(100)    not null                   comment '项目名称',
  project_code          varchar(50)     default ''                 comment '项目编号',
  project_type          varchar(20)     default ''                 comment '项目类型',
  order_no              int(11)         default 0                  comment '排序号',
  unit                  varchar(20)     not null                   comment '单位',
  price                 decimal(10,2)   default 0.00               comment '价格',
  service_duration      int(11)         default 0                  comment '服务时长（分钟）',
  image                 varchar(500)    default ''                 comment '项目图片',
  nursing_requirement   varchar(500)    default ''                 comment '护理要求',
  status                tinyint(1)      default 1                  comment '状态（0禁用 1启用）',
  create_by             varchar(64)     default ''                 comment '创建者',
  create_time           datetime                                   comment '创建时间',
  update_by             varchar(64)     default ''                 comment '更新者',
  update_time           datetime                                   comment '更新时间',
  remark                varchar(500)    default null               comment '备注',
  primary key (id)
) engine=innodb auto_increment=100 comment = '护理项目表';

insert into nursing_project values
(1, '助浴服务', 'HY-001', '生活护理', 1, '次', 80.00, 45, '/static/service/hy-001.png', '协助老人完成安全助浴，注意防滑和保暖', 1, 'admin', sysdate(), '', null, null),
(2, '助餐服务', 'HY-002', '生活护理', 2, '次', 50.00, 30, '/static/service/hy-009.png', '协助老人进餐并做好餐后清洁', 1, 'admin', sysdate(), '', null, null),
(3, '洗脸护理', 'HY-003', '生活护理', 3, '次', 30.00, 15, '/static/service/hy-003.png', '温水洗脸，保持面部清洁', 1, 'admin', sysdate(), '', null, null),
(4, '洗头护理', 'HY-004', '生活护理', 4, '次', 40.00, 20, '/static/service/hy-002.png', '协助老人洗头并吹干头发', 1, 'admin', sysdate(), '', null, null),
(5, '足部护理', 'HY-005', '生活护理', 5, '次', 35.00, 25, '/static/service/hy-004.png', '足部清洁和基础护理', 1, 'admin', sysdate(), '', null, null),
(6, '修剪指甲', 'HY-006', '生活护理', 6, '次', 20.00, 15, '/static/service/hy-006.png', '修剪手脚指甲并清洁', 1, 'admin', sysdate(), '', null, null),
(7, '翻身拍背', 'HY-007', '医疗护理', 7, '次', 45.00, 20, '/static/service/hy-007.png', '协助翻身拍背，预防压疮', 1, 'admin', sysdate(), '', null, null),
(8, '康复运动', 'HY-008', '康复护理', 8, '次', 100.00, 60, '/static/service/hy-008.png', '在护理员陪同下完成康复训练', 1, 'admin', sysdate(), '', null, null);

create table nursing_plan (
  id          bigint(20)      not null auto_increment    comment '护理计划ID',
  sort_no     int(11)         default 0                  comment '排序号',
  plan_name   varchar(100)    not null                   comment '护理计划名称',
  status      tinyint(1)      default 1                  comment '状态（0禁用 1启用）',
  create_by   varchar(64)     default ''                 comment '创建者',
  create_time datetime                                   comment '创建时间',
  update_by   varchar(64)     default ''                 comment '更新者',
  update_time datetime                                   comment '更新时间',
  remark      varchar(500)    default null               comment '备注',
  primary key (id)
) engine=innodb auto_increment=100 comment = '护理计划表';

create table nursing_plan_project (
  id                bigint(20)      not null auto_increment    comment '计划项目明细ID',
  plan_id           bigint(20)      not null                   comment '护理计划ID',
  project_id        bigint(20)      not null                   comment '护理项目ID',
  execute_time      varchar(20)     default ''                 comment '计划执行时间',
  execute_cycle     tinyint(1)      default 0                  comment '执行周期（0天 1周 2月）',
  execute_frequency int(11)         default 1                  comment '执行频次',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (id)
) engine=innodb auto_increment=100 comment = '护理计划项目明细表';

insert into nursing_plan values
(1, 1, '一级护理计划', 1, 'admin', sysdate(), '', null, '适用于需要重点照护的老人'),
(2, 2, '二级护理计划', 1, 'admin', sysdate(), '', null, '适用于需要中等照护的老人'),
(3, 3, '三级护理计划', 1, 'admin', sysdate(), '', null, '适用于基本自理老人');

insert into nursing_plan_project values
(1, 1, 1, '08:30:00', 0, 1, 'admin', sysdate(), '', null, null),
(2, 1, 2, '11:30:00', 0, 1, 'admin', sysdate(), '', null, null),
(3, 1, 7, '15:00:00', 0, 2, 'admin', sysdate(), '', null, null),
(4, 2, 2, '11:30:00', 0, 1, 'admin', sysdate(), '', null, null),
(5, 2, 4, '09:00:00', 1, 2, 'admin', sysdate(), '', null, null),
(6, 2, 8, '16:00:00', 1, 3, 'admin', sysdate(), '', null, null),
(7, 3, 3, '08:00:00', 0, 1, 'admin', sysdate(), '', null, null),
(8, 3, 6, '10:00:00', 2, 1, 'admin', sysdate(), '', null, null);

create table nursing_level (
  id          bigint(20)      not null auto_increment    comment '护理等级ID',
  name        varchar(100)    not null                   comment '护理等级名称',
  lplan_id    bigint(20)      not null                   comment '护理计划ID',
  fee         decimal(10,2)   default 0.00               comment '护理费用',
  status      tinyint(1)      default 1                  comment '状态（0禁用 1启用）',
  description varchar(500)    default ''                 comment '等级说明',
  create_by   varchar(64)     default ''                 comment '创建者',
  create_time datetime                                   comment '创建时间',
  update_by   varchar(64)     default ''                 comment '更新者',
  update_time datetime                                   comment '更新时间',
  remark      varchar(500)    default null               comment '备注',
  primary key (id)
) engine=innodb auto_increment=100 comment = '护理等级表';

insert into nursing_level values
(1, '一级护理等级', 1, 2000.00, 1, '适用于生活不能完全自理，需要重点照护的老人。', 'admin', sysdate(), '', null, null),
(2, '二级护理等级', 2, 1500.00, 1, '适用于能够自理但需要一定辅助护理的老人。', 'admin', sysdate(), '', null, null),
(3, '三级护理等级', 3, 1000.00, 1, '适用于能够基本自理，需要少量日常护理的老人。', 'admin', sysdate(), '', null, null);

delete from sys_menu where menu_id in (200,201,202,203,1200,1201,1202,1203,1210,1211,1212,1213,1220,1221,1222,1223);

insert into sys_menu values('200', '护理管理', '0', '4', 'nursing', null, '', '', 1, 0, 'M', '0', '0', '', 'server', 'admin', sysdate(), '', null, '护理管理目录');
insert into sys_menu values('201', '护理项目', '200', '1', 'project', 'nursing/project/index', '', '', 1, 0, 'C', '0', '0', 'nursing:project:list', 'list', 'admin', sysdate(), '', null, '护理项目菜单');
insert into sys_menu values('202', '护理计划', '200', '2', 'plan', 'nursing/plan/index', '', '', 1, 0, 'C', '0', '0', 'nursing:plan:list', 'date', 'admin', sysdate(), '', null, '护理计划菜单');
insert into sys_menu values('203', '护理等级', '200', '3', 'level', 'nursing/level/index', '', '', 1, 0, 'C', '0', '0', 'nursing:level:list', 'star', 'admin', sysdate(), '', null, '护理等级菜单');

insert into sys_menu values('1200', '护理项目查询', '201', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:project:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1201', '护理项目新增', '201', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:project:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1202', '护理项目修改', '201', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:project:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1203', '护理项目删除', '201', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:project:remove', '#', 'admin', sysdate(), '', null, '');

insert into sys_menu values('1210', '护理计划查询', '202', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:plan:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1211', '护理计划新增', '202', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:plan:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1212', '护理计划修改', '202', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:plan:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1213', '护理计划删除', '202', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:plan:remove', '#', 'admin', sysdate(), '', null, '');

insert into sys_menu values('1220', '护理等级查询', '203', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:level:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1221', '护理等级新增', '203', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:level:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1222', '护理等级修改', '203', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:level:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1223', '护理等级删除', '203', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:level:remove', '#', 'admin', sysdate(), '', null, '');
