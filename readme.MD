
#开发者API：
http://XXXX:XX/swagger-ui.html
admin/123456

#监控Druid 连接池状态
http://XXXX:XX/druid/



#配置mysql
*1.通过which mysqld命令来查看mysql的安装位置
*2.
{{ which mysqld result}} --verbose --help |grep -A 1 'Default options'

*3. 修改以下值：
>[mysqld]
>sql_mode = "STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION"