<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-CN"  xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>用户中心</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="stylesheet" href="${basePath}/resources/ucenter/css/bootstrap.min.css">
    <link rel="stylesheet" href="//at.alicdn.com/t/font_ay4w4j5c2tke29.css">
    <link rel="stylesheet" href="${basePath}/resources/ucenter/css/style.css">
</head>
<body class="login-page">
<div class="login-box">
    <div class="login-logo">
        <a href="#"><b>Zheng</b>UC</a>
    </div>

    <div class="row login-box-body">
        <div class="row" style="margin-bottom: 20px;">
            <div class="col-xs-6 logo-tip">
                <i class="iconfont icon-icon053"></i> <span>登录</span>
            </div>
            <div class="col-xs-6" style="text-align: right">
                <a href="signup">注册</a>
            </div>
        </div>
        <!-- Nav tabs -->
        <ul class="tabs-menu">
            <li class="active"><a href="#email" data-toggle="tab">邮箱登录</a></li>
            <li><a href="#phone" data-toggle="tab">短信登录</a></li>
        </ul>

        <!-- Tab panes -->
        <div class="tab-content">
            <div class="tab-pane active" id="email">
                <div class="login-box-body">

                    <form>
                        <div class="form-group has-feedback">
                            <input type="email" class="form-control" placeholder="邮箱">
                            <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
                        </div>
                        <div class="form-group has-feedback">
                            <input type="password" class="form-control" placeholder="登录密码">
                            <span class="glyphicon glyphicon-lock form-control-feedback"></span>
                        </div>
                        <div class="row">
                            <div class="col-xs-6">
                                <div class="checkbox icheck">
                                    <label>
                                        <input type="checkbox"> 记住我
                                    </label>
                                </div>
                            </div>
                            <div class="col-xs-6" style="text-align: right">
                                <div class="checkbox icheck">
                                    <label>
                                        <a href="password_reset">忘记密码？</a>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <!-- /.col -->
                            <div class="col-xs-12">
                                <button type="submit" class="btn btn-primary btn-block btn-flat">登录</button>
                            </div>
                            <!-- /.col -->
                        </div>
                    </form>
                </div>
            </div>
            <div class="tab-pane" id="phone">
                <div class="login-box-body">

                    <form>
                        <div class="form-group has-feedback">
                            <input type="email" class="form-control" placeholder="手机号">
                            <span class="glyphicon glyphicon-phone form-control-feedback"></span>
                        </div>
                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group has-feedback">
                                    <input type="password" class="form-control" placeholder="短信验证码">
                                    <span class="glyphicon glyphicon-lock form-control-feedback"></span>
                                </div>
                            </div>
                            <div class="col-xs-6">
                                <button type="submit" class="btn btn-primary btn-block btn-flat">获取验证码</button>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-6">
                                <div class="checkbox icheck">
                                    <label>
                                        <input type="checkbox"> 记住我
                                    </label>
                                </div>
                            </div>
                            <div class="col-xs-6" style="text-align: right">
                                <div class="checkbox icheck">
                                    <label>
                                        <a href="">忘记密码？</a>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <!-- /.col -->
                            <div class="col-xs-12">
                                <button type="submit" class="btn btn-primary btn-block btn-flat">登录</button>
                            </div>
                            <!-- /.col -->
                        </div>
                    </form>

                </div>
                <!-- /.login-box-body -->
            </div>
        </div>

        <div class="social-auth-links">
            <p>- OR -</p>
            <a style="margin-left:0" href="#"><i class="iconfont icon-qq"></i></a>
            <a href="#"><i class="iconfont icon-weixin"></i></a>
            <a href="#"><i class="iconfont icon-weibo"></i></a>
            <a href="#"><i class="iconfont icon-github1"></i></a>
        </div>
        <!-- /.social-auth-links -->

    </div>
</div>
<!-- /.login-box -->

<script>var BASE_PATH = '${basePath}';</script>
<script>var BACK_URL = '${backurl}';</script>
<script src="${basePath}/resources/ucenter/js/jquery.min.js" src="js/jquery.min.js"></script>
<script src="${basePath}/resources/ucenter/js/bootstrap.min.js" src="js/bootstrap.min.js"></script>
</body>
</html>