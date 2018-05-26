package com.zheng.upms.server.controller;


import com.dto.huiyi.meeting.util.Constants;
import com.zheng.common.base.BaseResult;
import com.zheng.common.constants.upms.UpmsResult;
import com.zheng.common.constants.upms.UpmsResultConstant;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

@Controller
@RequestMapping("/token")
@Api(value = "token", description = "发送REST请求之前必须获取token")
public class TokenController {


    @ApiOperation(value = "获取token")
    @RequestMapping(value = "get", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object index(HttpServletRequest request) throws Exception {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        if (StringUtils.isBlank(username)) {
            return new UpmsResult(UpmsResultConstant.EMPTY_USERNAME, "帐号不能为空！");
        }
        if (StringUtils.isBlank(password)) {
            return new UpmsResult(UpmsResultConstant.EMPTY_PASSWORD, "密码不能为空！");
        }

        Subject subject = SecurityUtils.getSubject();
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

        SignatureAlgorithm sigAlg = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("secret");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, sigAlg.getJcaName());
        System.out.println(request.getUserPrincipal().getName());
        JwtBuilder builder = Jwts.builder()
                .setSubject(request.getUserPrincipal().getName())
                .signWith(sigAlg, signingKey);

        return new BaseResult(Constants.SUCCESS_CODE, "token returned", builder.compact());
    }

}
