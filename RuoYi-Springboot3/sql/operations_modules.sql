-- Admission, residence and intelligent monitoring modules

drop table if exists nursing_operation_record;

create table nursing_operation_record (
  id            bigint not null auto_increment,
  module        varchar(40) not null,
  record_type   varchar(40) default '',
  name          varchar(100) default '',
  code          varchar(80) default '',
  status        varchar(30) default '',
  elder_name    varchar(30) default '',
  id_card       varchar(30) default '',
  phone         varchar(20) default '',
  location      varchar(80) default '',
  product_name  varchar(80) default '',
  amount        decimal(12,2) default 0.00,
  start_date    date default null,
  end_date      date default null,
  payload       text,
  create_by     varchar(64) default '',
  create_time   datetime default null,
  update_by     varchar(64) default '',
  update_time   datetime default null,
  remark        varchar(500) default '',
  primary key (id),
  key idx_module (module),
  key idx_module_status (module, status)
) engine=innodb default charset=utf8mb4 comment='operations module shared records';

insert into nursing_operation_record
(module,record_type,name,code,status,elder_name,id_card,phone,location,product_name,amount,start_date,end_date,payload,create_by,create_time,remark)
values
('health','assessment','厦康健康体检中心','PG20260625001','已入住','安欣','230203199701221029','','','一级护理等级',87.70,'2026-06-20',null,
'{"score":87.7,"advice":"建议","risk":"提示","birthday":"1978-10-10","gender":"男","report":"/profile/upload/health-anxin.pdf","analysisReport":"/profile/upload/health-anxin-ai.pdf","summary":"身体健康程度良好，建议保持均衡饮食和规律运动。","systems":[62,65,72,75,77,78],"abnormal":[["代谢性疾病","空腹血糖","14.01","3.9-6.1","mmol/L"],["脂蛋白代谢异常","总胆固醇","7.07","2.9-5.86","mmol/L"],["高血脂","低密度脂蛋白胆固醇","4.19","0-3.36","mmol/L"]]}',
'admin','2026-06-25 09:00:00','体检完成'),
('health','assessment','厦康健康体检中心','PG20260625002','未入住','高启强','230203197801221029','','','一级护理等级',78.60,'2026-06-19',null,
'{"score":78.6,"advice":"建议","risk":"提示","birthday":"1978-10-10","gender":"男","report":"/profile/upload/health-gao.pdf","analysisReport":"/profile/upload/health-gao-ai.pdf","summary":"体重指数偏高，建议控制油脂摄入，增加有氧运动。","systems":[62,65,72,75,77,78],"abnormal":[["代谢性疾病","空腹血糖","14.01","3.9-6.1","mmol/L"],["肥胖","体重指数BMI","29.2",">24","-"]]}',
'admin','2026-06-24 09:00:00','AI分析完成'),
('health','assessment','市中心医院','PG20260625003','已退住','陈泰','230203196001221029','','','二级护理等级',72.30,'2026-06-18',null,
'{"score":72.3,"advice":"建议","risk":"中风险","birthday":"1960-01-22","gender":"男","summary":"心血管指标需持续关注。","systems":[58,63,68,72,70,74],"abnormal":[["心脑血管疾病","血清载脂蛋白B","0.50","0.64-1.14","g/L"]]}',
'admin','2026-06-23 09:00:00','定期复查'),

('admission','resident','安欣入住申请','RZ20260625001','已入住','安欣','230203199701221029','13878898881','1011床位','一级护理等级',3700.00,'2026-06-20','2027-06-19',
'{"gender":"男","birthday":"1978-10-10","address":"京海市长虹区幸福路1号","family":[{"name":"范闲","phone":"13899998877","relation":"子女"},{"name":"范思哲","phone":"13456656656","relation":"配偶"}],"deposit":3000,"bedFee":1700,"nursingFee":2000,"medicalFee":0,"otherFee":0,"subsidy":0,"contractName":"安欣入住合同","contractFile":"/profile/upload/admission-anxin.pdf"}',
'admin','2026-06-25 10:00:00','入住办理完成'),
('admission','resident','高启强入住申请','RZ20260625002','已入住','高启强','230203197801221029','13878898882','1012床位','一级护理等级',4200.00,'2026-06-19','2027-06-18',
'{"gender":"男","birthday":"1978-10-10","address":"京海市旧厂街2号","family":[{"name":"高启盛","phone":"13899998878","relation":"兄弟"}],"deposit":3000,"bedFee":1700,"nursingFee":2500,"medicalFee":0,"otherFee":0,"subsidy":0,"contractName":"高启强入住合同"}',
'admin','2026-06-24 10:00:00','入住办理完成'),
('admission','resident','孟钰入住申请','RZ20260625003','已入住','孟钰','230203199001221029','13878898883','2012床位','二级护理等级',4500.00,'2026-06-18','2027-06-17',
'{"gender":"女","birthday":"1990-01-22","address":"京海市中心区3号","family":[{"name":"孟德海","phone":"13899998879","relation":"父女"}],"deposit":3000,"bedFee":2000,"nursingFee":2500,"medicalFee":0,"otherFee":0,"subsidy":0,"contractName":"孟钰入住合同"}',
'admin','2026-06-23 10:00:00','入住办理完成'),

('checkout','settlement','高启强退住申请','TZ20260625001','已退住','高启强','230203197801221029','13878898882','1012床位','一级护理等级',5520.00,'2026-06-19','2026-06-25',
'{"reason":"服务不同","releaseDate":"2026-06-25","releaseFile":"/profile/upload/release-gao.pdf","refundMethod":"现金","refundVoucher":"/static/service/hy-004.png","refundRemark":"共退款5520.00元，现金退款，已结清。","receivable":[{"bill":"ZD20260625001","type":"月度账单","amount":2000},{"bill":"ZD20260625002","type":"费用账单","amount":20}],"arrears":[{"bill":"ZD20260625003","type":"月度账单","amount":2000}],"balance":[{"name":"押金金额","amount":2000},{"name":"预缴款金额","amount":20}],"unpaid":[{"bill":"ZD20260625004","type":"费用账单","amount":20}]}',
'admin','2026-06-25 14:00:00','退住完成'),
('checkout','settlement','陈泰退住申请','TZ20260625002','已退住','陈泰','230203196001221029','13878898884','1021床位','二级护理等级',3200.00,'2026-05-01','2026-06-22',
'{"reason":"身体康复回家","releaseDate":"2026-06-22","refundMethod":"银行卡","refundRemark":"费用已清算并退回银行卡。","receivable":[],"arrears":[],"balance":[{"name":"押金金额","amount":3000},{"name":"预缴款金额","amount":200}],"unpaid":[]}',
'admin','2026-06-22 14:00:00','退住完成'),

('room-type','room','单人间','FX001','启用','','','','','/static/service/hy-001.png',800.00,null,null,
'{"bedCount":1,"description":"房间内设有24小时CCTV监控","image":"/static/service/hy-001.png"}','admin','2026-06-25 08:00:00',''),
('room-type','room','双人间','FX002','启用','','','','','/static/service/hy-002.png',1000.00,null,null,
'{"bedCount":2,"description":"房间内设有独立卫生间","image":"/static/service/hy-002.png"}','admin','2026-06-24 08:00:00',''),
('room-type','room','豪华单人间','FX003','禁用','','','','','/static/service/hy-003.png',2000.00,null,null,
'{"bedCount":1,"description":"房间内设有智能监测设备","image":"/static/service/hy-003.png"}','admin','2026-06-23 08:00:00',''),

('bed','floor','1楼','F1','正常','','','','1楼','','0',null,null,
'{"rooms":[{"room":"101","beds":[{"bed":"1011","elder":"安欣","status":"occupied"},{"bed":"1012","elder":"高启强","status":"occupied"},{"bed":"1013","elder":"","status":"empty"}]},{"room":"102","beds":[{"bed":"1021","elder":"陈泰","status":"occupied"},{"bed":"1022","elder":"李有田","status":"occupied"},{"bed":"1023","elder":"","status":"empty"}]},{"room":"103","beds":[{"bed":"1031","elder":"高启盛","status":"occupied"},{"bed":"1032","elder":"","status":"empty"}]}]}',
'admin','2026-06-25 08:00:00',''),
('bed','floor','2楼','F2','正常','','','','2楼','','0',null,null,
'{"rooms":[{"room":"201","beds":[{"bed":"2011","elder":"陈书婷","status":"occupied"},{"bed":"2012","elder":"孟钰","status":"occupied"}]},{"room":"202","beds":[{"bed":"2021","elder":"唐小龙","status":"occupied"},{"bed":"2022","elder":"","status":"empty"}]}]}',
'admin','2026-06-24 08:00:00',''),

('smart-bed','monitor','101房间','SB101','正常','','','','101房间','智能床位',0,null,null,
'{"door":"开启","temperature":26,"humidity":40,"alarm":"正常","beds":[{"bed":"1011","elder":"安欣","state":"清醒中","heart":80,"breath":20,"devices":["智能手表","智能烟感"]},{"bed":"1012","elder":"高启盛","state":"已离床","leaveCount":6,"leaveTime":"20:00:00","devices":["智能手表"]},{"bed":"1013","elder":"高启兰","state":"睡眠中","heart":80,"breath":20,"devices":["智能手表","智能烟感"]}]}',
'admin','2026-06-25 08:00:00',''),
('smart-bed','monitor','102房间','SB102','报警','','','','102房间','智能床位',0,null,null,
'{"door":"开启","temperature":27,"humidity":45,"alarm":"心率异常","beds":[{"bed":"1021","elder":"高启强","state":"清醒中","heart":0,"breath":0,"devices":["智能手表"]},{"bed":"1022","elder":"高启盛","state":"已离床","leaveCount":6,"leaveTime":"20:00:00","devices":["智能手表"]},{"bed":"1023","elder":"高启兰","state":"睡眠中","heart":80,"breath":20,"devices":["智能手表"]}]}',
'admin','2026-06-25 08:00:00',''),

('contract','contract','安欣试住合同','HT202606250001','未生效','安欣','230203199701221029','13878898881','','',0,'2026-07-01','2027-06-30',
'{"signDate":"2026-06-25","partyB":"范闲","partyPhone":"13899998877","file":"/profile/upload/contract-anxin.pdf"}','admin','2026-06-25 09:00:00',''),
('contract','contract','高启强长住合同','HT202606240002','生效中','高启强','230203197801221029','13878898882','','',0,'2026-06-19','2027-06-18',
'{"signDate":"2026-06-19","partyB":"高启盛","partyPhone":"13899998878","file":"/profile/upload/contract-gao.pdf"}','admin','2026-06-24 09:00:00',''),
('contract','contract','陈泰短住合同','HT202606230003','已失效','陈泰','230203196001221029','13878898884','','',0,'2026-05-01','2026-06-22',
'{"signDate":"2026-05-01","partyB":"陈书婷","partyPhone":"13899998885","file":"/profile/upload/contract-chen.pdf","releaseDate":"2026-06-22","releaseFile":"/profile/upload/release-chen.pdf"}','admin','2026-06-23 09:00:00',''),

('customer','customer','范闲','KH20260625001','已签约','','','13899999999','','',4,null,null,
'{"signed":"是","orders":4,"elders":["安欣"],"firstLogin":"2026-06-25 15:00:00"}','admin','2026-06-25 15:00:00',''),
('customer','customer','范思哲','KH20260624002','未签约','','','13788995678','','',5,null,null,
'{"signed":"否","orders":5,"elders":["高启强","安欣","陈泰"],"firstLogin":"2026-06-24 15:00:00"}','admin','2026-06-24 15:00:00',''),
('customer','customer','范若若','KH20260623003','已签约','','','13567678543','','',5,null,null,
'{"signed":"是","orders":5,"elders":["陈泰","高启盛"],"firstLogin":"2026-06-23 15:00:00"}','admin','2026-06-23 15:00:00',''),

('device','iot','SmartWatch-01','DEV001','在线','安欣','','','安欣','智能手表',0,null,null,
'{"alias":"智能手表01","type":"随身设备","deviceSecret":"7980e85a0cb22d89ac6219023c34af2c","productKey":"j0dz5GFiqte","region":"华东2（上海）","nodeType":"设备","auth":"设备密钥","activated":"2026-06-25 15:00:00","properties":[{"identifier":"HeartRate","name":"心率","value":"80次/分钟"},{"identifier":"Steps","name":"步数","value":"3200步"}]}',
'admin','2026-06-25 15:00:00',''),
('device','iot','smoke alarm-01','DEV002','在线','','','','1011床位','智能烟感',0,null,null,
'{"alias":"烟雾报警器01","type":"固定设备","deviceSecret":"b61673980cb67410e2493645ca8ad5ed","productKey":"j0dz5GFiqte","region":"华东2（上海）","nodeType":"设备","auth":"设备密钥","activated":"2026-06-24 15:00:00","properties":[{"identifier":"Smoke","name":"烟雾浓度","value":"10ppm"},{"identifier":"PowerConsumption","name":"用电量","value":"10.0kW·h"}]}',
'admin','2026-06-24 15:00:00',''),
('device','iot','Intelligent access control','DEV003','在线','','','','1楼','智能门禁',0,null,null,
'{"alias":"智能门禁","type":"固定设备","deviceSecret":"b61673980cb67410e2493645ca8ad5ed","productKey":"j0dz5GFiqte","region":"华东2（上海）","nodeType":"设备","auth":"设备密钥","activated":"2026-06-23 15:00:00","services":[{"identifier":"DoorOpen","name":"远程开门","input":"开门","output":"成功"}]}',
'admin','2026-06-23 15:00:00',''),

('alarm-rule','rule','智能烟感烟雾量','RULE001','启用','','','','全部设备','智能烟感',3,null,null,
'{"function":"烟雾量","operator":">","threshold":3,"duration":"持续1个周期","time":"00:00:00-23:59:00","type":"设备异常报警"}','admin','2026-06-25 15:00:00',''),
('alarm-rule','rule','智能手表心率过高','RULE002','启用','','','','全部设备','智能手表',150,null,null,
'{"function":"心率","operator":">","threshold":150,"duration":"持续1个周期","time":"00:00:00-23:59:00","type":"老人异常报警"}','admin','2026-06-24 15:00:00',''),
('alarm-rule','rule','智能手表心率过低','RULE003','禁用','','','','全部设备','智能手表',50,null,null,
'{"function":"心率","operator":"<","threshold":50,"duration":"持续5个周期","time":"00:00:00-23:59:00","type":"老人异常报警"}','admin','2026-06-23 15:00:00',''),

('alarm-data','alarm','智能手表01心率报警','ALARM001','待处理','安欣','','','1011床位','智能手表',160,null,null,
'{"device":"watch 01","alias":"智能手表01","type":"老人异常数据","value":"160次/分钟","rule":"心率>=150，持续1个周期就报警","alarmTime":"2026-06-25 15:00:00"}','admin','2026-06-25 15:00:00',''),
('alarm-data','alarm','智能烟感烟雾报警','ALARM002','已处理','','','','1011床位','智能烟感',5,null,null,
'{"device":"smoke alarm-01","alias":"烟雾报警器01","type":"设备异常数据","value":"5ppm","rule":"烟雾量>=3，持续1个周期就报警","alarmTime":"2026-06-24 15:00:00","handler":"余佳俊","handleTime":"2026-06-24 15:10:00","result":"现场检查无明火，已通风处理。"}','admin','2026-06-24 15:00:00','');

delete from sys_role_menu where menu_id between 700 and 799 or menu_id between 1270 and 1299;
delete from sys_menu where menu_id between 1270 and 1299;
delete from sys_menu where menu_id between 700 and 799;

insert into sys_menu
(menu_id,menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
values
(700,'入退管理',0,2,'admission',null,'','Admission',1,0,'M','0','0','','guide','admin',sysdate(),'',null,''),
(701,'健康评估',700,1,'health','operations/health/index','','HealthAssessment',1,0,'C','0','0','operations:record:list','chart','admin',sysdate(),'',null,''),
(702,'入住办理',700,2,'checkin','operations/admission/index','','AdmissionManage',1,0,'C','0','0','operations:record:list','form','admin',sysdate(),'',null,''),
(703,'退住办理',700,3,'checkout','operations/checkout/index','','CheckoutManage',1,0,'C','0','0','operations:record:list','exit-fullscreen','admin',sysdate(),'',null,''),
(720,'在住管理',0,3,'residence',null,'','Residence',1,0,'M','0','0','','build','admin',sysdate(),'',null,''),
(721,'房型设置',720,1,'room-type','operations/roomType/index','','RoomTypeManage',1,0,'C','0','0','operations:record:list','build','admin',sysdate(),'',null,''),
(722,'床位预览',720,2,'bed','operations/bed/index','','BedPreview',1,0,'C','0','0','operations:record:list','eye-open','admin',sysdate(),'',null,''),
(723,'智能床位',720,3,'smart-bed','operations/smartBed/index','','SmartBed',1,0,'C','0','0','operations:record:list','monitor','admin',sysdate(),'',null,''),
(724,'合同跟踪',720,4,'contract','operations/contract/index','','ContractTrack',1,0,'C','0','0','operations:record:list','documentation','admin',sysdate(),'',null,''),
(725,'客户管理',720,5,'customer','operations/customer/index','','CustomerManage',1,0,'C','0','0','operations:record:list','peoples','admin',sysdate(),'',null,''),
(740,'智能监测',0,7,'monitoring',null,'','IntelligentMonitoring',1,0,'M','0','0','','devices','admin',sysdate(),'',null,''),
(741,'设备管理',740,1,'device','operations/device/index','','DeviceManage',1,0,'C','0','0','operations:record:list','devices','admin',sysdate(),'',null,''),
(742,'报警规则',740,2,'alarm-rule','operations/alarmRule/index','','AlarmRule',1,0,'C','0','0','operations:record:list','alarm-light','admin',sysdate(),'',null,''),
(743,'报警数据',740,3,'alarm-data','operations/alarmData/index','','AlarmData',1,0,'C','0','0','operations:record:list','alarm-record','admin',sysdate(),'',null,''),
(1270,'业务详情',700,1,'','','','',1,0,'F','0','0','operations:record:query','#','admin',sysdate(),'',null,''),
(1271,'业务编辑',700,2,'','','','',1,0,'F','0','0','operations:record:edit','#','admin',sysdate(),'',null,''),
(1272,'业务删除',700,3,'','','','',1,0,'F','0','0','operations:record:remove','#','admin',sysdate(),'',null,'');

insert ignore into sys_role_menu(role_id,menu_id)
select 2,menu_id from sys_menu
where menu_id between 700 and 799 or menu_id between 1270 and 1299;
