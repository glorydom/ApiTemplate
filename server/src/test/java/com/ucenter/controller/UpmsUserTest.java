package com.ucenter.controller;


import com.huicong.upms.dao.model.UpmsUser;
import com.huicong.upms.dao.model.UpmsUserExample;
import com.huicong.upms.rpc.api.UpmsUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath*:applicationContext*.xml",
        "classpath*:spring/*.xml"
})
public class UpmsUserTest {

    @Autowired
    UpmsUserService service;


    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;


    @Test
    public void CreateUpmsUserTest(){


        for(int i =0; i< 10 ; i++){
            taskExecutor.submit(new Runnable() {
                @Override
                public void run() {

                    String threadName = Thread.currentThread().getName();
                    UpmsUserExample example = new UpmsUserExample();
                    UpmsUserExample.Criteria criteria = example.createCriteria();
                    criteria.andAvatarEqualTo("jack_threadPoolTaskExecutor-5");
                    UpmsUser upmsUser  = service.selectFirstByExample(example);
                    String phone = upmsUser.getPhone();
                    phone = phone == null ? "":phone;

                    upmsUser.setPhone(phone + threadName.substring(threadName.length()-1));
                    try{

                        service.updateByExample(upmsUser, example);
                        System.out.println(threadName + "try to update user : " + upmsUser + " into databast");
                    }catch (Exception e){
                        System.out.println("exception occurs for -> " + threadName + ". and the exception is: "  + e);
                    }
                }
            });


        }

    }


}
