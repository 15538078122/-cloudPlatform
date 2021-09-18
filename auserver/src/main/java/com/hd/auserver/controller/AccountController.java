package com.hd.auserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hd.auserver.entity.AccountEntity;
import com.hd.auserver.service.AccountService;
import com.hd.common.MyPage;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @Author: liwei
 * @Description:
 */
@RestController
public class AccountController {

    @Autowired
    AccountService accountService;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/account/list")
    public RetResult list(@RequestParam("account") String account,String enterprise,int pageNum,int pageSize) {
        QueryWrapper qw = new QueryWrapper();
        qw.like("account",account);
        qw.eq("enterprise",enterprise);
        //qw1.orderByDesc("id","account");
        MyPage<AccountEntity> accountEntityPage = accountService.selectAccounts(pageNum, pageSize,qw);
        return RetResponse.makeRsp(accountEntityPage);
    }
    @PostMapping("/account")
    public RetResult Add(@RequestParam ("account")  String account,@RequestParam("enterprise") String enterprise,@RequestParam("password") String password) throws Exception {
        //TODO: 创建用户时，pwd需要加密
        //byte[] res = RSAEncrypt.decrypt(GenRsaFileTask.rsaPrivateKey, Base64.decode(password));
        //password=new String(res);
        AccountEntity accountEntity = new AccountEntity(null, enterprise, account, passwordEncoder.encode(password), new Date(),false);
        accountService.save(accountEntity);
        return RetResponse.makeRsp("添加账号成功.",accountEntity.getId());
    }

    @GetMapping("/account")
    public RetResult get(@RequestParam ("account")  String account,@RequestParam("enterprise") String enterprise) throws Exception {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("account",account);
        qw.eq("enterprise",enterprise);
        qw.eq("delete_flag",0);
        List<AccountEntity> list = accountService.list(qw);
        if(list.size()<=0){
            throw  new Exception("账号不存在!");
        }
        return RetResponse.makeRsp(list.get(0).getId());
    }

    @DeleteMapping("/account/{id}")
    public RetResult delete(@PathVariable("id")  Long id) {
//        QueryWrapper qw = new QueryWrapper();
//        qw.eq("account",account);
//        qw.eq("enterprise",enterprise);
        accountService.removeById(id);
        return RetResponse.makeRsp("删除账号成功.");
    }

    @DeleteMapping("/account/all")
    public RetResult delete(@RequestParam("enterprise") String enterprise) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("enterprise",enterprise);
        accountService.remove(qw);
        return RetResponse.makeRsp("删除账号成功.");
    }

    @PutMapping("/account")
    public RetResult changePwd(@RequestParam ("account")  String account,@RequestParam("enterprise") String enterprise,@RequestParam("password") String password,@RequestParam("passwordOld") String passwordOld) throws Exception {
        //TODO: 修改用户密码，pwd需要加密
        accountService.changePwd(account, enterprise, password, passwordOld);
        return RetResponse.makeRsp("修改密码成功.");
    }
}
