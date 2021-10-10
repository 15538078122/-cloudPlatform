package com.hd.gateway;

import com.hd.gateway.utils.JwtUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: liwei
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GatewayApplication.class)
public class GatewayApplicationTests {

    @Test
    public void contextLoads() {
        //TODO 此处调用接口方法
        System.out.println("contextLoads");

    }
    @Autowired
    JwtUtils jwtUtils;

    @Test
    public void decodeToken() throws Exception {
        jwtUtils.decodeToken("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJyb290IiwibG9naW5fdGltZSI6IjIwMjEtMDktMTggMTA6NTE6MTEiLCJzY29wZSI6WyJ1c2VyIl0sImF0aSI6IjRiZWE0MjQ0LWNhZjEtNDU2Yy05M2NhLTc3ZDEzMzY4NGE4NCIsImlkIjoiODg4ODg4ODg4ODg4ODg4ODg4OCIsImV4cCI6MTYzMjE5MjY3MSwiZW50ZXJwcmlzZV9pZCI6InJvb3QiLCJqdGkiOiIxY2U2ZWM4ZC04ODZiLTRkNmEtOTI4MC0xMzlmYjllNTZkOWQiLCJjbGllbnRfaWQiOiJjbGllbnQifQ.S9I---VfRNuaoJnNTm_yg1ctjWeqK8F_U1zph4kr0oOHvbnYlIJhunW7NVM9aWE6ALggWhgIJxR-Scih777_mKkdN-BKUBEcXNrXFKoHImEA9q7vllphMIdKBsOs-lk6mov2XYAgchzNtP_8pvICzh5XohM55vkeSwv__rHbNC2E79MvJgnYJZIHX80-YskJGvGCEubN4uh0aQZEhzqZf29dq35_XWNAJ0eY2wFvWrfS1zAho9_1kkg191MB0AgDTCgAxVUazwWsXq4bYB5VGglIl_MShOHhTzYj5YncTE3ymmc0zIUSj0_qKkkYMkPsCfW-Pz9DOBTrc4mVN0LjIQ");
        jwtUtils.decodeToken("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJyb290IiwibG9naW5fdGltZSI6IjIwMjEtMDktMTggMTA6NTE6MTEiLCJzY29wZSI6WyJ1c2VyIl0sImlkIjoiODg4ODg4ODg4ODg4ODg4ODg4OCIsImV4cCI6MTYzMTk0MDY3MSwiZW50ZXJwcmlzZV9pZCI6InJvb3QiLCJqdGkiOiI0YmVhNDI0NC1jYWYxLTQ1NmMtOTNjYS03N2QxMzM2ODRhODQiLCJjbGllbnRfaWQiOiJjbGllbnQifQ.L_MWN4YbSkgWeslolA4CBUCMFI_P9anJMGZ40vw8D3_oREfpLbb6HA1KtJbbNqgfOEM8E0fpkF5ALexbukHcBrtEnGmBJnjaEZGAMFNdFT77Efm9iEq2UEoJjzv2vFYnP5glSJ1OdS_RqyiRBKnwmSjq1dmQ1TutN-zjgNziexW7yY8YAjDPdRe4DiycQ-g09hrzOS39cKOAaRAvVbnnvZhuaiylwi7mzkUNbbsKToyrOXptvtQzK9Ns0gz1siUccjM1ZXTWDZSiBJzz_3x7DbfwAr_0GKVEoYKp1tfWqHDr0FKT7Ga3SZqxJojbTZk6Fmb0D9IO5AClrc2kQbxTHA");
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
