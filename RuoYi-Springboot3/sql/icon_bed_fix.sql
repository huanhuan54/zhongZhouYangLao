-- 修复设备、报警、负责老人菜单图标。
update sys_menu set icon = 'caregiver' where menu_id = 204;
update sys_menu set icon = 'devices' where menu_id in (740, 741);
update sys_menu set icon = 'alarm-light' where menu_id = 742;
update sys_menu set icon = 'alarm-record' where menu_id = 743;
