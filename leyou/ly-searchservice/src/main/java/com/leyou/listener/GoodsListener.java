package com.leyou.listener;

import com.leyou.service.SpuService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsListener {


    @Autowired
    SpuService spuService;



    @RabbitListener ( bindings = @QueueBinding(
                                    //队列名字
            value = @Queue(name = "leyou.edit.search.queue", durable = "true"),
            exchange = @Exchange(
                    //   交换机名称
                    value = "leyou.exchanges",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"leyou.add","leyou.update"}))
   public  void editEsData(Long spuId) throws Exception {

        System.out.println("开始监听修改es索引库数据,spuId : "+spuId);

        if(spuId==null){
            return;
        }
        spuService.editEsData(spuId);




        System.out.println("结束监听修改es索引库数据,spuId : "+spuId+"    成功");
    }
    @RabbitListener ( bindings = @QueueBinding(
                                    //队列名字
            value = @Queue(name = "leyou.delete.search.queue", durable = "true"),
            exchange = @Exchange(
                    //   交换机名称
                    value = "leyou.exchanges",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"leyou.delete"}))
   public  void deleteEsData(Long spuId) throws Exception {

        System.out.println("开始监听删除es索引库数据,spuId : "+spuId);

        if(spuId==null){
            return;
        }
        spuService.deleteEsData(spuId);




        System.out.println("结束监听删除es索引库数据,spuId : "+spuId+"    成功");
    }








}
