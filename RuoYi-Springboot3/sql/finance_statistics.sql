-- Financial statistics module, menu ordering and residence icon repair.

delete from nursing_operation_record
where module in ('finance-bill', 'finance-prepayment', 'finance-balance', 'finance-arrears');

insert into nursing_operation_record
(module, record_type, name, code, status, elder_name, id_card, phone, location,
 product_name, amount, start_date, end_date, payload, create_by, create_time, remark)
values
('finance-bill','monthly','月度账单','ZD202606250001','已支付','安欣','230203199701221029','','1011',
 '月度账单',2022.00,'2026-06-01','2026-06-30',
 '{"month":"2026-06","payableAmount":1022.00,"paymentDeadline":"2026-07-06 00:00:00","transactionStatus":"已支付","relationOrder":"DD202606250001","billingCycle":"2026-06-01 至 2026-06-30","totalDays":30,"details":[{"type":"添加项","feeItem":"护理费用","service":"特护护理等级","amount":2000.00},{"type":"添加项","feeItem":"床位费用","service":"单人间","amount":1000.00},{"type":"添加项","feeItem":"其他费用","service":"协助更衣","amount":22.00},{"type":"扣减项","feeItem":"医保支付","service":"医保报销","amount":-1000.00}],"monthlyPayable":2022.00,"arrears":0.00,"deposit":3000.00,"billAmount":2022.00,"refundEstimate":978.00,"payment":{"operator":"顾廷烨","time":"2026-06-25 13:00:00","channel":"线下支付","method":"微信","voucher":"/static/service/hy-004.png","note":"家属已确认并完成支付。"}}',
 'admin','2026-06-25 13:00:00','六月月度账单'),
('finance-bill','monthly','月度账单','ZD202606240002','待支付','高启强','230203197801221029','','1012',
 '月度账单',2200.22,'2026-06-01','2026-06-30',
 '{"month":"2026-06","payableAmount":1200.22,"paymentDeadline":"2026-07-06 00:00:00","transactionStatus":"待支付","billingCycle":"2026-06-01 至 2026-06-30","totalDays":30,"details":[{"type":"添加项","feeItem":"护理费用","service":"一级护理等级","amount":1500.22},{"type":"添加项","feeItem":"床位费用","service":"双人间","amount":700.00}],"monthlyPayable":2200.22,"arrears":1200.22,"deposit":3000.00,"billAmount":2200.22,"refundEstimate":799.78}',
 'admin','2026-06-24 13:00:00','待支付'),
('finance-bill','expense','费用账单','ZD202606230003','已支付','陈泰','230203196001221029','','1013',
 '费用账单',1900.01,'2026-06-01','2026-06-30',
 '{"month":"2026-06","payableAmount":1900.01,"paymentDeadline":"2026-07-06 00:00:00","transactionStatus":"已支付","details":[{"type":"添加项","feeItem":"护理项目","service":"助浴服务","amount":100.01},{"type":"添加项","feeItem":"护理费用","service":"二级护理等级","amount":1800.00}],"monthlyPayable":1900.01,"arrears":0,"deposit":3000,"billAmount":1900.01,"refundEstimate":1099.99,"payment":{"operator":"顾廷烨","time":"2026-06-23 13:00:00","channel":"线上支付","method":"微信","transactionNo":"1217752501201407033233368018"}}',
 'admin','2026-06-23 13:00:00','费用已结清'),
('finance-bill','monthly','月度账单','ZD202606220004','已关闭','李有田','230203198801221029','','1014',
 '月度账单',1899.00,'2026-06-01','2026-06-30',
 '{"month":"2026-06","payableAmount":1899.00,"paymentDeadline":"2026-07-06 00:00:00","transactionStatus":"已关闭","details":[{"type":"添加项","feeItem":"护理费用","service":"一级护理等级","amount":1899.00}],"monthlyPayable":1899.00,"arrears":0,"deposit":3000,"billAmount":1899.00,"refundEstimate":1101.00,"cancellation":{"operator":"顾廷烨","time":"2026-06-24 13:00:00","reason":"账单信息调整，已重新生成。"}}',
 'admin','2026-06-22 13:00:00','账单已关闭'),
('finance-bill','expense','费用账单','ZD202606210005','待支付','安长林','230203197501221029','','1015',
 '费用账单',1122.22,'2026-06-01','2026-06-30',
 '{"month":"2026-06","payableAmount":1122.22,"paymentDeadline":"2026-07-06 00:00:00","transactionStatus":"待支付","details":[{"type":"添加项","feeItem":"护理项目","service":"助洁服务","amount":122.22},{"type":"添加项","feeItem":"床位费用","service":"双人间","amount":1000.00}],"monthlyPayable":1122.22,"arrears":1122.22,"deposit":3000,"billAmount":1122.22,"refundEstimate":1877.78}',
 'admin','2026-06-21 13:00:00','待支付'),

('finance-prepayment','recharge','预缴款充值','YJ202606250001','已到账','安欣','230203199701221029','','1011',
 '现金',2022.00,null,null,
 '{"method":"现金","voucher":"/static/service/hy-004.png","note":"线下充值，现金缴纳","operator":"顾廷烨"}',
 'admin','2026-06-25 10:15:00','预缴款充值'),
('finance-prepayment','recharge','预缴款充值','YJ202606240002','已到账','高启强','230203197801221029','','1012',
 '微信',2200.22,null,null,
 '{"method":"微信","voucher":"/static/service/hy-005.png","note":"微信转账","operator":"顾廷烨"}',
 'admin','2026-06-24 10:15:00','预缴款充值'),
('finance-prepayment','recharge','预缴款充值','YJ202606230003','已到账','陈泰','230203196001221029','','1013',
 '支付宝',1900.01,null,null,
 '{"method":"支付宝","voucher":"/static/service/hy-006.png","note":"线上充值","operator":"顾廷烨"}',
 'admin','2026-06-23 10:15:00','预缴款充值'),
('finance-prepayment','recharge','预缴款充值','YJ202606220004','已到账','李有田','230203198801221029','','1014',
 '现金',1899.00,null,null,
 '{"method":"现金","voucher":"/static/service/hy-007.png","note":"现金充值","operator":"顾廷烨"}',
 'admin','2026-06-22 10:15:00','预缴款充值'),

('finance-balance','balance','账户余额','YE202606250001','正常','安欣','230203199701221029','','1011',
 '账户余额',5500.00,null,null,'{"prepaid":5500.00,"balance":5500.00}','admin','2026-06-25 10:15:00',''),
('finance-balance','balance','账户余额','YE202606240002','正常','高启强','230203197801221029','','1012',
 '账户余额',5000.00,null,null,'{"prepaid":5000.00,"balance":5000.00}','admin','2026-06-24 10:15:00',''),
('finance-balance','balance','账户余额','YE202606230003','正常','陈泰','230203196001221029','','1013',
 '账户余额',4500.00,null,null,'{"prepaid":4500.00,"balance":4500.00}','admin','2026-06-23 10:15:00',''),
('finance-balance','balance','账户余额','YE202606220004','正常','李有田','230203198801221029','','1014',
 '账户余额',4400.00,null,null,'{"prepaid":4400.00,"balance":4400.00}','admin','2026-06-22 10:15:00',''),

('finance-arrears','arrears','欠费账单','QF202606250001','待支付','安欣','230203199701221029','','1011',
 '欠费',5500.00,null,'2026-07-06',
 '{"deadline":"2026-07-06 00:00:00","bills":[{"billNo":"ZD202606250001","month":"2026-04","deadline":"2026-05-06 00:00:00","amount":2022.00},{"billNo":"ZD202605250001","month":"2026-05","deadline":"2026-06-06 00:00:00","amount":1099.00},{"billNo":"ZD202604250001","month":"2026-03","deadline":"2026-04-06 00:00:00","amount":2039.00}]}',
 'admin','2026-06-25 10:15:00',''),
('finance-arrears','arrears','欠费账单','QF202606240002','待支付','高启强','230203197801221029','','1012',
 '欠费',5000.00,null,'2026-07-06',
 '{"deadline":"2026-07-06 00:00:00","bills":[{"billNo":"ZD202606240002","month":"2026-06","deadline":"2026-07-06 00:00:00","amount":2200.22},{"billNo":"ZD202605240002","month":"2026-05","deadline":"2026-06-06 00:00:00","amount":2799.78}]}',
 'admin','2026-06-24 10:15:00',''),
('finance-arrears','arrears','欠费账单','QF202606230003','待支付','陈泰','230203196001221029','','1013',
 '欠费',4500.00,null,'2026-07-06',
 '{"deadline":"2026-07-06 00:00:00","bills":[{"billNo":"ZD202606230003","month":"2026-06","deadline":"2026-07-06 00:00:00","amount":1900.01},{"billNo":"ZD202605230003","month":"2026-05","deadline":"2026-06-06 00:00:00","amount":2599.99}]}',
 'admin','2026-06-23 10:15:00','');

delete from sys_role_menu where menu_id between 800 and 804;
delete from sys_menu where menu_id between 800 and 804;

insert into sys_menu
(menu_id,menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
values
(800,'财务统计',0,6,'finance',null,'','FinanceStatistics',1,0,'M','0','0','','money','admin',sysdate(),'',null,''),
(801,'账单管理',800,1,'bill','finance/bill/index','','FinanceBill',1,0,'C','0','0','operations:record:list','money','admin',sysdate(),'',null,''),
(802,'预缴款管理',800,2,'prepayment','finance/prepayment/index','','FinancePrepayment',1,0,'C','0','0','operations:record:list','form','admin',sysdate(),'',null,''),
(803,'余额查询',800,3,'balance','finance/balance/index','','FinanceBalance',1,0,'C','0','0','operations:record:list','search','admin',sysdate(),'',null,''),
(804,'欠费老人',800,4,'arrears','finance/arrears/index','','FinanceArrears',1,0,'C','0','0','operations:record:list','warning','admin',sysdate(),'',null,'');

insert ignore into sys_role_menu(role_id,menu_id)
select 2,menu_id from sys_menu where menu_id between 800 and 804;

-- Requested top-level order: visit, admission, residence, nursing, order,
-- finance, monitoring, AI consultation last.
update sys_menu set order_num = 1 where menu_id = 300;
update sys_menu set order_num = 2 where menu_id = 700;
update sys_menu set order_num = 3, icon = 'build' where menu_id = 720;
update sys_menu set order_num = 4 where menu_id = 200;
update sys_menu set order_num = 5 where menu_id = 600;
update sys_menu set order_num = 6 where menu_id = 800;
update sys_menu set order_num = 7 where menu_id = 740;
update sys_menu set order_num = 8 where menu_id = 400;
