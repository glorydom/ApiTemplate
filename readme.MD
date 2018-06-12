
#开发者API：
http://XXXX:XX/swagger-ui.html
admin/123456
所有密码均为123456

#监控Druid 连接池状态
http://XXXX:XX/druid/



#配置mysql
*1.通过which mysqld命令来查看mysql的安装位置
*2.
{{ which mysqld result}} --verbose --help |grep -A 1 'Default options'

*3. 修改以下值：
>[mysqld]
>sql_mode = "STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION"


# 工作流的流程图需要系统中包含中文字体
安装中文字体的步骤参考：https://www.jianshu.com/p/8b1e8aed0049


