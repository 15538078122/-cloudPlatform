package com.hd.microsysservice.controller;

import com.alibaba.fastjson.JSON;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.vo.SyUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: liwei
 * @Description:
 */
@Api(tags = "消息Controller")
@RestController
@Slf4j
public class RabbitProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @ApiOperation(value = "产生message")
    //@RequiresPermissions("")
    @PostMapping("/sendMsg")
    public RetResult sendMsg(String msg) throws Exception {
        int i=100;
        while (i-->0){
            String msg2= String.format("第%d个消息:", i)+msg;
            log.debug("send------>"+msg2);
            CorrelationData correlationData = new CorrelationData(new Integer(87998).toString());
            rabbitTemplate.convertAndSend("TestDirectExchange","TestDirectRouting", JSON.toJSONString(new SyUserVo(){{
                setAccount(msg2);
            }}),correlationData);
//            rabbitTemplate.convertAndSend("TestDirectExchange","TestDirectRouting", JSON.toJSONString(new SyUserVo(){{
//                setAccount(msg2);
//            }}),correlationData);

        }
        return RetResponse.makeRsp("发送消息成功.");
    }
}
