package com.leyou.listener;

import com.leyou.util.SmsUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MessageListener {





    @RabbitListener( bindings = @QueueBinding(
            //队列名字
            value = @Queue(name = "sms.send.queue", durable = "true"),
            exchange = @Exchange(
                    //   交换机名称
                    value = "sms.changes",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"sms.send"}))
    public  void sendMeg(Map<String,String> map ) throws Exception {

        System.out.println("开始监听短信发送 phone : "+map.get("phone")+"code:" +map.get("code"));

        if(map.size()>0 && map !=null){

            SmsUtils.sendMsg(map.get("phone"),map.get("code"));
        }


        System.out.println("结束监听短信发送");
    }









}
