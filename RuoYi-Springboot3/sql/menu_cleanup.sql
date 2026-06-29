-- 答辩版菜单精简：隐藏若依框架演示菜单，保留中州养老业务菜单。

update sys_menu
set visible = '1'
where menu_id in (1, 2, 3, 4);

update sys_menu
set menu_name = '服务管理', order_num = 3, icon = 'server'
where menu_id = 200;

update sys_menu
set order_num = 2, icon = 'user'
where menu_id = 300;

update sys_menu
set order_num = 4, icon = 'message'
where menu_id = 400;

update sys_user
set nick_name = '管理员'
where user_name = 'admin';
