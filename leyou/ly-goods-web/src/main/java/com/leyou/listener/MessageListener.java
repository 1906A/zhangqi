package com.leyou.listener;

import com.leyou.service.GoodService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @Autowired
    GoodService goodService;



    @RabbitListener( bindings = @QueueBinding(
            //队列名字
            value = @Queue(name = "leyou.edit.web.queue", durable = "true"),
            exchange = @Exchange(
                    //   交换机名称
                    value = "leyou.exchanges",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"leyou.add","leyou.update"}))
    public  void editthymeleafData(Long spuId) throws Exception {

        System.out.println("开始监听修改thymeleaf索引库数据,spuId : "+spuId);

        if(spuId==null){
            return;
        }


        goodService.createHtml(spuId);


        System.out.println("结束监听修改thymeleaf索引库数据,spuId : "+spuId+"    成功");
    }





    @RabbitListener( bindings = @QueueBinding(
            //队列名字
            value = @Queue(name = "leyou.delete.web.queue", durable = "true"),
            exchange = @Exchange(
                    //   交换机名称
                    value = "leyou.exchanges",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"leyou.delete"}))
    public  void deletethymeleafData(Long spuId) throws Exception {

        System.out.println("开始监听删除thymeleaf索引库数据,spuId : "+spuId);

        if(spuId==null){
            return;
        }


        goodService.deleteHtml(spuId);


        System.out.println("结束监听删除thymeleaf索引库数据,spuId : "+spuId+"    成功");
    }






}
