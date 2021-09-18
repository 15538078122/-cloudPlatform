package com.hd.auserver;

import com.hd.auserver.config.MyJwtAccessTokenConverter;
import com.hd.auserver.entity.AccountEntity;
import com.hd.auserver.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Date;
import java.util.List;

@SpringBootTest
class AuServerApplicationTests {

    @Autowired
    JwtAccessTokenConverter  jwtAccessTokenConverter;

    @Test
    void jwtDecodeTest() {
        String token="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHRlcm5hbF9pbmZvIjoiMjEtMDYtMDggMTA6NDY6MTkiLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbInJlYWQxIiwid3JpdGUxIl0sImV4cCI6MTYyMzEyNzU3OSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJqdGkiOiIxY2UwMDlhZS04MWQyLTQ3NGEtYjg4Yy02MWZlZTdjOTlkZjIiLCJjbGllbnRfaWQiOiJjbGllbnQifQ.Gl8E8JKiKFvw7M_HQJCsYPTxXKMEm_9N8qEopUFHSh2_VbwjpovMa58j7h_0TTNrsNq-C3k_RNLnAdMw2TBluezgZ9Cr1_AeRHL1b1z2t66JLxaexhqnjh3JoL1vplaUU3-txmiDZy523SYuo_0oZKHCyC1qWXrKbcpgxiMaDMgZA26RqUDBFwYcUxbutpgFiOchhAnj6MhuRhwHpO5HByJtGWhTWNGvGzPc5ED4T0r5IXppAxv5A4hU99xmh4zjYlh6fQqv9uHans5ihB3sS4m622qh3vxEAVxbDAO6agOssCNQx_UQYw793OF86xf74Q5DfZjSWOqy0yweondULA";

        ((MyJwtAccessTokenConverter)jwtAccessTokenConverter).decodeJwtToken("");
    }

    @Autowired
    AccountService accountService;

    @Test
    void accountOpTest(){
        AccountEntity accountEntity=new AccountEntity(null,"enterprise","admin","", new Date(),false);
        accountService.save(accountEntity);
        List<AccountEntity> accountEntities = accountService.list();

    }

}
