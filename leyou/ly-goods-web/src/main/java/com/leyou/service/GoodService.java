package com.leyou.service;

import com.leyou.client.*;
import com.leyou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodService {
    @Autowired
    BrandClientServer brandClientServer;

    @Autowired
    CategotyClientServer categotyClientServer;

    @Autowired
    SkuCilentServer skuCilentServer;

    @Autowired
    SpecClientServer specClientServer;

    @Autowired
    SpuClientServer spuClientServer;

    @Autowired
    TemplateEngine templateEngine;


    public  Map<String,Object> item(Long spuId) {
        //一spu
        Spu spu = spuClientServer.findSpuById(spuId);

        //一 spuDetail
        SpuDetail spuDetail = spuClientServer.findSpuDetailBySpuId(spuId);

        //根据spuId查询sku
        List<Sku> skuList = skuCilentServer.findSkuBySpuId(spuId);


        //一 specgroup 查询规格参数及组内信息
        List<SpecGroup> specGroupList = specClientServer.findAllSpecGroup(spu.getCid3());

        //一 三级分类
        List<Category> categoryList = Arrays.asList(
                categotyClientServer.findCategoryById(spu.getCid1()),
                categotyClientServer.findCategoryById(spu.getCid2()),
                categotyClientServer.findCategoryById(spu.getCid3())
        );


        //一 specparm详情 根据cid 是否通用查询
        List<SpecParam> specParamList = specClientServer.findSpecParamsByCidAndGeneric(spu.getCid3(), false);

        //规格参数的特殊属性
        Map<Long, String> paramMap = new HashMap<>();
        //存的是id跟名称
        specParamList.forEach(specParam -> {
            paramMap.put(specParam.getId(), specParam.getName());
        });

        //一 品牌
        Brand brand = brandClientServer.findBrandById(spu.getBrandId());



        Map<String,Object> map=new HashMap<>();


       map.put("spu", spu);
       map.put("spuDetail", spuDetail);
       map.put("skuList", skuList);
       map.put("specGroupList", specGroupList);

       map.put("categoryList", categoryList);

       map.put("paramMap", paramMap);
       map.put("brand", brand);

        return  map;


    }


    /**
     * thymeleaf 实现页面静态化
     *
     * @param spu
     * @param spuDetail
     * @param skuList
     * @param specGroupList
     * @param categoryList
     * @param brand
     * @param paramMap
     */
    public void createHtml(Long spuId) {

        PrintWriter writer = null;

        try {
            // 创建thymeleaf上下文对象
            Context context = new Context();
            // 把数据放入上下文对象
          /*  context.setVariable("spu", spu);
            context.setVariable("spuDetail", spuDetail);
            context.setVariable("skuList", skuList);
            context.setVariable("specGroupList", specGroupList);
            context.setVariable("categoryList", categoryList);
            context.setVariable("brand", brand);
            context.setVariable("paramMap", paramMap);*/

            context.setVariables(item(spuId));





            // 创建输出流
            File file = new File("F:\\nginx-1.16.1\\html\\" + spuId + ".html");
            writer = new PrintWriter(file);
            // 执行页面静态化方法
            templateEngine.process("item", context, writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            writer.close();
        }


    }


    /**删除静态文件
     * @param spuId
     */
    public void deleteHtml(Long spuId) {
        File file = new File("F:\\nginx-1.16.1\\html\\" + spuId + ".html");
        if(file.exists() && file!=null){
            file.delete();
        }

    }
}
