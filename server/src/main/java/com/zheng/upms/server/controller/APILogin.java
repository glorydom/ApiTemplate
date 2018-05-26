package com.zheng.upms.server.controller;

import com.dto.huiyi.meeting.util.Constants;
import com.huicong.upms.rpc.api.UpmsSystemService;
import com.huicong.upms.rpc.api.UpmsUserService;
import com.zheng.common.base.BaseController;
import com.zheng.common.base.BaseResult;
import com.zheng.common.constants.upms.UpmsResult;
import com.zheng.common.constants.upms.UpmsResultConstant;
import com.zheng.common.util.RedisUtil;
import com.zheng.upms.client.shiro.session.UpmsSession;
import com.zheng.upms.client.shiro.session.UpmsSessionDao;
import com.zheng.upms.server.controller.dto.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.UUID;

@Controller
@RequestMapping("/api")
@Api(value = "API单点登录管理", description = "API单点登录管理")
public class APILogin extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(APILogin.class);
    // 全局会话key
    private final static String ZHENG_UPMS_SERVER_SESSION_ID = "zheng-upms-server-session-id";
    // 全局会话key列表
    private final static String ZHENG_UPMS_SERVER_SESSION_IDS = "zheng-upms-server-session-ids";
    // code key
    private final static String ZHENG_UPMS_SERVER_CODE = "zheng-upms-server-code";

    @Autowired
    UpmsSystemService upmsSystemService;

    @Autowired
    UpmsUserService upmsUserService;

    @Autowired
    UpmsSessionDao upmsSessionDao;

    @ApiOperation(value = "api登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(@RequestBody UserLogin userLogin) {
        String username = userLogin.getUsername();
        String password = userLogin.getPassword();
        String rememberMe = userLogin.getReadme();
        if (StringUtils.isBlank(username)) {
            return new UpmsResult(UpmsResultConstant.EMPTY_USERNAME, "帐号不能为空！");
        }
        if (StringUtils.isBlank(password)) {
            return new UpmsResult(UpmsResultConstant.EMPTY_PASSWORD, "密码不能为空！");
        }
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String sessionId = session.getId().toString();
        // 判断是否已登录，如果已登录，则回跳，防止重复登录
        String hasCode = RedisUtil.get(ZHENG_UPMS_SERVER_SESSION_ID + "_" + sessionId);
        // code校验值
        if (StringUtils.isBlank(hasCode)) {
            // 使用shiro认证
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
            try {
                if (BooleanUtils.toBoolean(rememberMe)) {
                    usernamePasswordToken.setRememberMe(true);
                } else {
                    usernamePasswordToken.setRememberMe(false);
                }
                subject.login(usernamePasswordToken);
            } catch (UnknownAccountException e) {
                return new UpmsResult(UpmsResultConstant.INVALID_USERNAME, "帐号不存在！");
            } catch (IncorrectCredentialsException e) {
                return new UpmsResult(UpmsResultConstant.INVALID_PASSWORD, "密码错误！");
            } catch (LockedAccountException e) {
                return new UpmsResult(UpmsResultConstant.INVALID_ACCOUNT, "帐号已锁定！");
            }
            // 更新session状态
            upmsSessionDao.updateStatus(sessionId, UpmsSession.OnlineStatus.on_line);
            // 全局会话sessionId列表，供会话管理
            RedisUtil.lpush(ZHENG_UPMS_SERVER_SESSION_IDS, sessionId.toString());
            // 默认验证帐号密码正确，创建code
            String code = UUID.randomUUID().toString();
            // 全局会话的code
            RedisUtil.set(ZHENG_UPMS_SERVER_SESSION_ID + "_" + sessionId, code, (int) subject.getSession().getTimeout() / 1000);
            LOGGER.info("把session放入缓存，如果{}秒未进行有效操作，则自动登出", (int) subject.getSession().getTimeout() / 1000);
            // code校验值
            RedisUtil.set(ZHENG_UPMS_SERVER_CODE + "_" + code, code, (int) subject.getSession().getTimeout() / 1000);
        }
        // 回跳登录前地址
        return new BaseResult(Constants.SUCCESS_CODE, "got sessionID", sessionId);
    }

    @ApiOperation(value = "api登录")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public Object login() {
        // shiro退出登录
        SecurityUtils.getSubject().logout();
        // 跳回原地址
        return new BaseResult(Constants.SUCCESS_CODE, "logout successfully", null);
    }


}
