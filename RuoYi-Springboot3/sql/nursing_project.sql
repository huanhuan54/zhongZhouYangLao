-- ----------------------------
-- 护理项目表
-- ----------------------------
drop table if exists nursing_project;
create table nursing_project (
  id                    bigint(20)      not null auto_increment    comment '护理项目ID',
  name                  varchar(100)    not null                   comment '项目名称',
  order_no              int(11)         default 0                  comment '排序号',
  unit                  varchar(20)     not null                   comment '单位',
  price                 decimal(10,2)   default 0.00               comment '价格',
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
(1, '洗头', 1, '次', 30.00, 'https://itheim.oss-cn-beijing.aliyuncs.com/60f89ec5-a53b-4a53-8b6a-499a32dcc421.png', '温水洗头', 1, 'admin', sysdate(), '', null, null),
(2, '康复', 3, '次', 99.90, 'https://itheim.oss-cn-beijing.aliyuncs.com/7cbe6478-2499-4162-89f1-e642893aa2ce.png', '每天坚持30分钟', 1, 'admin', sysdate(), '', null, null),
(3, '洗澡', 12, '次', 23.00, 'https://itheim.oss-cn-beijing.aliyuncs.com/60f89ec5-a53b-4a53-8b6a-499a32dcc421.png', '无', 1, 'admin', sysdate(), '', null, null);

-- ----------------------------
-- 服务管理菜单与护理项目按钮权限
-- 如菜单ID与现有数据冲突，可在执行前替换为未占用ID。
-- ----------------------------
insert into sys_menu values('200', '服务管理', '0', '1', 'nursing', null, '', '', 1, 0, 'M', '0', '0', '', 'server', 'admin', sysdate(), '', null, '服务管理目录');
insert into sys_menu values('201', '护理项目', '200', '1', 'project', 'nursing/project/index', '', '', 1, 0, 'C', '0', '0', 'nursing:project:list', 'guide', 'admin', sysdate(), '', null, '护理项目菜单');
insert into sys_menu values('1200', '护理项目查询', '201', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:project:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1201', '护理项目新增', '201', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:project:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1202', '护理项目修改', '201', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:project:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1203', '护理项目删除', '201', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:project:remove', '#', 'admin', sysdate(), '', null, '');
