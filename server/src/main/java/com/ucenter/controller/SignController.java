package com.ucenter.controller;

import com.huicong.Test.dao.model.TestCopy;
import com.huicong.Test.dao.model.TestCopyExample;
import com.huicong.Test.rpc.api.TestCopyService;
import com.zheng.common.base.BaseController;
import com.zheng.common.constants.ucenter.UcenterResult;
import com.zheng.common.constants.ucenter.UcenterResultConstant;
import com.zheng.ucenter.dao.model.UcenterUser;
import com.zheng.ucenter.rpc.api.UcenterUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

/**
 * 注册控制器
 * Created by shuzheng on 2017/5/2.
 */
@Controller
@Api(value = "网站用户管理", description = "主要用于网站访问者的注册，登陆，密码更新")
public class SignController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignController.class);
    private final String UserCenterFolder = "/ucenter";

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    TestCopyService copyService;

    @Autowired
    UcenterUserService ucenterUserService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        return UserCenterFolder.concat("/login.jsp");
    }


    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    @ApiOperation(value = "注册页面")
    public String signup(Model model) {

        return UserCenterFolder.concat("/reg.jsp");
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "用户注册")
    public Object signup(HttpServletRequest request) {
        String backurl = request.getParameter("backurl");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        LOGGER.info("email-->{}, password --> {}", email, password);
        Integer i = 1;
        UcenterUser ucenterUser = new UcenterUser();
        ucenterUser.setCreateIp("" + i++);

        return new UcenterResult(UcenterResultConstant.SUCCESS, "login");
    }

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String signin(Model model) {

        return UserCenterFolder.concat("/login.jsp");
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    @ResponseBody
    public Object signin(HttpServletRequest request) {

        return new UcenterResult(UcenterResultConstant.SUCCESS, "");
    }

    @RequestMapping(value = "/signout", method = RequestMethod.GET)
    @ResponseBody
    public String index(Model model) {

        return "signout";
    }

    @RequestMapping(value = "/password_reset", method = RequestMethod.GET)
    public String passwordReset(Model model) {

        return UserCenterFolder.concat("/password.jsp");
    }


    @ResponseBody
    public Object TestAop() {
        for (int i = 0; i < 10; i++) {
            taskExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    String threadName = Thread.currentThread().getName();
                    TestCopyExample example = new TestCopyExample();
                    TestCopyExample.Criteria criteria = example.createCriteria();
                    criteria.andNameEqualTo("wangwu");
                    TestCopy copy = copyService.selectFirstByExample(example);
                    copy.setName("charlice");
                    long currentVersion = copy.getVersion();
                    copy.setVersion(currentVersion +1);
                    TestCopyExample.Criteria criteriaUpdated = example.createCriteria();
                    criteriaUpdated.andVersionEqualTo(currentVersion );

                    try {
                        int affectCount = copyService.updateByExample(copy, example);

                        if (affectCount == 1) {

                            System.out.println(threadName + "success to update user : " + copy + " into databast");
                        } else {
                            System.out.println(threadName + "failed to update user : " + copy + " into databast");
                        }
                    } catch (Exception e) {
                        System.out.println("exception occurs for -> " + threadName + ". and the exception is: " + e);
                    }
                }
            });


        }
        return new UcenterResult(UcenterResultConstant.SUCCESS, "成功");
    }



}