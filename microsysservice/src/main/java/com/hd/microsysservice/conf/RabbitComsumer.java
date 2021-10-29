package com.hd.microsysservice.conf;

import com.alibaba.fastjson.JSON;
import com.hd.common.vo.SyUserVo;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: liwei
 * @Description:
 */
//@Component
@Slf4j
public class RabbitComsumer {
    @RabbitListener(queues = "queue-test" )
    public void process1(String jsonObj, Channel channel, @Headers Map<String, Object> headers) throws IOException {
        // 采用手动应答模式, 手动确认应答更为安全稳定
        SyUserVo syUserVo =JSON.parseObject(jsonObj, SyUserVo.class);
        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, true);
        log.info("receive1: " +Thread.currentThread().getId()+ new String(jsonObj));
    }
    @RabbitListener(queues = "queue-test")
    public void process2(Message message, Channel channel) throws IOException {
        // 采用手动应答模式, 手动确认应答更为安全稳定
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        SyUserVo syUserVo =JSON.parseObject(message.getBody(), SyUserVo.class);
        log.info("receive2: " +Thread.currentThread().getId()+ syUserVo.getAccount());
    }
}
