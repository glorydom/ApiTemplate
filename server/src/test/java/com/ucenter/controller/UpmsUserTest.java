package com.ucenter.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huicong.Test.dao.model.TestCopy;
import com.huicong.Test.dao.model.TestCopyExample;
import com.huicong.Test.rpc.api.TestCopyService;
import com.huicong.upms.dao.model.UpmsUser;
import com.huicong.upms.dao.model.UpmsUserExample;
import com.huicong.upms.rpc.api.UpmsUserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
        "classpath*:applicationContext*.xml",
        "classpath*:spring/*.xml",
        "classpath*:spring*.xml"
})
public class UpmsUserTest {

    @Autowired
    UpmsUserService service;
    @Autowired
    TestCopyService copyService;

    public MockMvc mockMvc;

//    public MockHttpSession session;


    @Autowired
    public WebApplicationContext wac;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Before
    public void createMvcMock(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }


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
                    phone = phone == null ? "0":phone;
                    int version = Integer.parseInt(phone);
                    criteria.andPhoneEqualTo(version+ "");

                    upmsUser.setPhone(phone + 1 + "");
                    try{

                        int affectCount = service.updateByExample(upmsUser, example);
                        if(affectCount == 1){

                            System.out.println(threadName + "success to update user : " + upmsUser + " into databast");
                        }else{
                            System.out.println(threadName + "failed to update user : " + upmsUser + " into databast");
                        }
                    }catch (Exception e){
                        System.out.println("exception occurs for -> " + threadName + ". and the exception is: "  + e);
                    }
                }
            });


        }

    }

//    @Test
    public void OptimisticLockTest(){
//        for(int i =0; i< 10 ; i++){
//            taskExecutor.submit(new Runnable() {
//                @Override
//                public void run() {
//                    String threadName = Thread.currentThread().getName();
//                    TestCopyExample example = new TestCopyExample();
//                    TestCopyExample.Criteria criteria = example.createCriteria();
//                    criteria.andNameEqualTo("lisi");
//                    TestCopy copy = new TestCopy();
//                    copy.setName("wangwu");
//                    copy.setId(5);
//                    try{
//                            int affectCount = copyService.updateByExample(copy, example);
//
//                        if(affectCount == 1){
//
//                            System.out.println(threadName + "success to update user : " + copy + " into databast");
//                        }else{
//                            System.out.println(threadName + "failed to update user : " + copy + " into databast");
//                        }
//                    }catch (Exception e){
//                        System.out.println("exception occurs for -> " + threadName + ". and the exception is: "  + e);
//                    }
//                }
//            });
//
//
//        }

        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/test"))
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            int status = mvcResult.getResponse().getStatus();
            System.out.println("请求状态码：" + status);
            String result = mvcResult.getResponse().getContentAsString();
            System.out.println("接口返回结果：" + result);
            JSONObject resultObj = JSON.parseObject(result);
            // 判断接口返回json中success字段是否为true
            Assert.assertTrue(resultObj.getBooleanValue("success"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
