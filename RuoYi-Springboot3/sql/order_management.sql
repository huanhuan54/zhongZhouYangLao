-- Order query module

drop table if exists nursing_refund;
drop table if exists nursing_order;

create table nursing_order (
  id                    bigint not null auto_increment,
  order_no              varchar(50) not null,
  resident_id           bigint not null,
  project_id            bigint not null,
  task_id               bigint default null,
  order_amount          decimal(10,2) not null default 0.00,
  expected_service_time datetime not null,
  order_user            varchar(50) not null,
  order_user_phone      varchar(20) default '',
  order_status          tinyint not null default 0 comment '0 pending pay, 1 pending execute, 2 executed, 3 completed, 4 refunded, 5 closed',
  payment_status        tinyint not null default 0 comment '0 pending, 1 paid, 2 closed',
  pay_channel           varchar(30) default '',
  pay_order_no          varchar(80) default '',
  pay_time              datetime default null,
  cancel_reason         varchar(200) default '',
  cancel_time           datetime default null,
  refund_status         tinyint not null default 0 comment '0 none, 1 processing, 2 success, 3 failed',
  create_by             varchar(64) default '',
  create_time           datetime default null,
  update_by             varchar(64) default '',
  update_time           datetime default null,
  remark                varchar(500) default '',
  primary key (id),
  unique key uk_order_no (order_no)
) engine=innodb default charset=utf8mb4 comment='elder-care service order';

create table nursing_refund (
  id                    bigint not null auto_increment,
  refund_no             varchar(50) not null,
  order_id              bigint not null,
  order_no              varchar(50) not null,
  refund_amount         decimal(10,2) not null default 0.00,
  applicant             varchar(50) not null,
  applicant_type        varchar(30) default '',
  apply_time            datetime not null,
  refund_reason         varchar(200) not null,
  refund_channel        varchar(30) default '',
  refund_method         varchar(30) default '',
  refund_status         tinyint not null default 1 comment '1 processing, 2 success, 3 failed',
  refund_time           datetime default null,
  refund_transaction_no varchar(100) default '',
  failure_code          varchar(30) default '',
  failure_reason        varchar(200) default '',
  create_by             varchar(64) default '',
  create_time           datetime default null,
  update_by             varchar(64) default '',
  update_time           datetime default null,
  primary key (id),
  unique key uk_refund_no (refund_no)
) engine=innodb default charset=utf8mb4 comment='order refund record';

insert into nursing_order
(order_no, resident_id, project_id, task_id, order_amount, expected_service_time,
 order_user, order_user_phone, order_status, payment_status, pay_channel, pay_order_no,
 pay_time, refund_status, create_by, create_time, remark)
values
('DD202606250001', 1, 3, 1, 30.00, '2026-06-26 09:00:00', '顾廷烨', '13878890001', 0, 0, '', '', null, 0, 'admin', '2026-06-25 09:10:00', '饭后进行口腔清洁'),
('DD202606250002', 2, 4, 2, 40.00, '2026-06-26 10:00:00', '顾廷伟', '13878890002', 1, 1, '微信', 'WX202606250002', '2026-06-25 09:25:00', 0, 'admin', '2026-06-25 09:20:00', '使用老人自带洗发用品'),
('DD202606240003', 7, 4, 6, 40.00, '2026-06-24 09:00:00', '顾廷烨', '13878890003', 2, 1, '微信', 'WX202606240003', '2026-06-23 15:05:00', 0, 'admin', '2026-06-23 15:00:00', '老人头上有一块疤，洗头时需要注意'),
('DD202606240004', 8, 6, 7, 20.00, '2026-06-24 10:30:00', '盛明兰', '13878890004', 3, 1, '支付宝', 'ALI202606240004', '2026-06-23 16:05:00', 0, 'admin', '2026-06-23 16:00:00', '注意避免剪伤'),
('DD202606230005', 9, 8, 8, 100.00, '2026-06-24 15:00:00', '盛墨兰', '13878890005', 4, 1, '微信', 'WX202606230005', '2026-06-23 10:05:00', 2, 'admin', '2026-06-23 10:00:00', '全程陪同'),
('DD202606250006', 4, 2, 4, 50.00, '2026-06-27 11:30:00', '盛如兰', '13878890006', 5, 2, '', '', null, 0, 'admin', '2026-06-25 10:30:00', '家属临时取消'),
('DD202606250007', 5, 5, 5, 35.00, '2026-06-27 15:00:00', '盛长柏', '13878890007', 0, 0, '', '', null, 0, 'admin', '2026-06-25 11:00:00', '足部基础护理'),
('DD202606250008', 3, 1, 3, 80.00, '2026-06-26 14:30:00', '盛纮', '13878890008', 1, 1, '微信', 'WX202606250008', '2026-06-25 11:35:00', 1, 'admin', '2026-06-25 11:30:00', '注意防滑和保暖'),
('DD202606240009', 10, 7, 9, 45.00, '2026-06-23 14:00:00', '王若弗', '13878890009', 2, 1, '微信', 'WX202606240009', '2026-06-22 14:05:00', 3, 'admin', '2026-06-22 14:00:00', '护理动作轻柔'),
('DD202606240010', 6, 2, null, 50.00, '2026-06-25 12:00:00', '林噙霜', '13878890010', 3, 1, '支付宝', 'ALI202606240010', '2026-06-24 08:05:00', 0, 'admin', '2026-06-24 08:00:00', '协助老人进餐');

update nursing_order
set cancel_reason = '家属临时有事，取消本次服务',
    cancel_time = '2026-06-25 10:40:00',
    update_by = 'admin',
    update_time = '2026-06-25 10:40:00'
where order_no = 'DD202606250006';

insert into nursing_refund
(refund_no, order_id, order_no, refund_amount, applicant, applicant_type, apply_time,
 refund_reason, refund_channel, refund_method, refund_status, refund_time,
 refund_transaction_no, failure_code, failure_reason, create_by, create_time)
select 'TK202606230005', id, order_no, order_amount, '顾廷烨', '后台用户',
       '2026-06-23 19:00:00', '老人临时身体不适，护理人员排期太长',
       '原路退回', '微信', 2, '2026-06-23 19:10:00',
       '50000000382019052707932678859', '', '', 'admin', '2026-06-23 19:00:00'
from nursing_order where order_no = 'DD202606230005';

insert into nursing_refund
(refund_no, order_id, order_no, refund_amount, applicant, applicant_type, apply_time,
 refund_reason, refund_channel, refund_method, refund_status, create_by, create_time)
select 'TK202606250008', id, order_no, order_amount, '余佳俊', '后台用户',
       '2026-06-25 13:00:00', '服务时间与家属安排冲突',
       '原路退回', '微信', 1, 'admin', '2026-06-25 13:00:00'
from nursing_order where order_no = 'DD202606250008';

insert into nursing_refund
(refund_no, order_id, order_no, refund_amount, applicant, applicant_type, apply_time,
 refund_reason, refund_channel, refund_method, refund_status, refund_time,
 failure_code, failure_reason, create_by, create_time)
select 'TK202606240009', id, order_no, order_amount, '顾廷烨', '后台用户',
       '2026-06-24 18:30:00', '护理效果未达到预期',
       '原路退回', '微信', 3, '2026-06-24 18:35:00',
       '403', '余额不足', 'admin', '2026-06-24 18:30:00'
from nursing_order where order_no = 'DD202606240009';

delete from sys_role_menu where menu_id in (600, 601, 602, 1250, 1251, 1252, 1253, 1260, 1261);
delete from sys_menu where menu_id in (1250, 1251, 1252, 1253, 1260, 1261, 601, 602, 600);

insert into sys_menu
(menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame,
 is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by,
 update_time, remark)
values
(600, '订单查询', 0, 4, 'order', null, '', 'Order', 1, 0, 'M', '0', '0', '', 'shopping', 'admin', sysdate(), '', null, '订单查询目录'),
(601, '订单管理', 600, 1, 'manage', 'order/manage/index', '', 'OrderManage', 1, 0, 'C', '0', '0', 'nursing:order:list', 'list', 'admin', sysdate(), '', null, '订单管理'),
(602, '退款管理', 600, 2, 'refund', 'order/refund/index', '', 'RefundManage', 1, 0, 'C', '0', '0', 'nursing:refund:list', 'money', 'admin', sysdate(), '', null, '退款管理'),
(1250, '订单查询', 601, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:order:query', '#', 'admin', sysdate(), '', null, ''),
(1251, '取消订单', 601, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:order:cancel', '#', 'admin', sysdate(), '', null, ''),
(1252, '申请退款', 601, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:order:refund', '#', 'admin', sysdate(), '', null, ''),
(1260, '退款查询', 602, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'nursing:refund:query', '#', 'admin', sysdate(), '', null, '');

insert ignore into sys_role_menu(role_id, menu_id)
select 2, menu_id from sys_menu
where menu_id in (600, 601, 602, 1250, 1251, 1252, 1260);
