package com.ucenter.controller;


import com.zheng.ucenter.dao.model.UcenterUser;
import com.zheng.ucenter.dao.model.UcenterUserExample;
import com.zheng.ucenter.rpc.api.UcenterUserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath*:applicationContext*.xml",
        "classpath*:spring/*.xml"
})

public class SignonControllerTest {


    @Autowired
    UcenterUserService ucenterUserService;




    @Before
    public void beforeuCenterCreatedTest(){
        List<UcenterUser> list = ucenterUserService.selectByExample(new UcenterUserExample());
        for(UcenterUser u : list){
            System.out.println("before -->" +u);
        }

    }


    @Transactional
    @Test
    public void uCenterCreateTest() throws InterruptedException {
        UcenterUser ucenterUser = new UcenterUser();
        ucenterUser.setNickname("jack");
        int money = 100;
        ucenterUser.setCreateIp(money + "");
        ucenterUserService.insert(ucenterUser);
        List<UcenterUser> list = ucenterUserService.selectByExample(new UcenterUserExample());
        for(UcenterUser u : list){
            System.out.println("in -->" +u);
        }
    }

    @After
    public void afteruCenterCreatedTest(){
        List<UcenterUser> list = ucenterUserService.selectByExample(new UcenterUserExample());
        for(UcenterUser u : list){
            System.out.println("after -->" + u);
        }

    }


}
