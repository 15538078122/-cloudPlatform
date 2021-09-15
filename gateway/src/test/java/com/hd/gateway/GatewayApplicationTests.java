package com.hd.gateway;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: liwei
 * @Description:
 */
@SpringBootTest
public class GatewayApplicationTests {

    @Test
    public void contextLoads() {
        //TODO 此处调用接口方法
        System.out.println("contextLoads");

    }

    @Test
    public void testTwo(){
        System.out.println("test hello 2");
    }
    @Test
    public void testTwo2(){
        System.out.println("test hello 22");
    }

    @Before
    public void testBefore(){
        System.out.println("before");
    }

    @After
    public void testAfter(){
        System.out.println("after");
    }

}
