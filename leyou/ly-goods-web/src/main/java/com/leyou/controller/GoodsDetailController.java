package com.leyou.controller;

import com.leyou.client.*;
import com.leyou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
public class GoodsDetailController {



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







    @RequestMapping("hello")
    public String hello(Model model){

        String name="哈哈";
        model.addAttribute("name",name);

        return  "hello";

    }


    /**
     * 请求商品详情的微服务
     * 1：spu
     * 2：spudetail
     * 3：sku
     * 4：规格参数组
     *
     * 5：规格参数详情
     * 6；三级分类
     * 7 品牌
     * @param spuId
     * @param model
     * @return
     */
    @RequestMapping("item/{spuId}.html")
    public String item(@PathVariable("spuId") Long spuId,Model model){

        //一spu
        Spu spu = spuClientServer.findSpuById(spuId);
        model.addAttribute("spu",spu);

        //一 spuDetail
        SpuDetail spuDetail = spuClientServer.findSpuDetailBySpuId(spuId);
        model.addAttribute("spuDetail",spuDetail);

        //根据spuId查询sku
        List<Sku> skuList = skuCilentServer.findSkuBySpuId(spuId);
        model.addAttribute("skuList",skuList);


       //一 specgroup 查询规格参数及组内信息
        List<SpecGroup> specGroupList = specClientServer.findAllSpecGroup(spu.getCid3());
        model.addAttribute("specGroupList",specGroupList);

        //一 三级分类
        List<Category> categoryList= Arrays.asList(
                categotyClientServer.findCategoryById(spu.getCid1()),
                categotyClientServer.findCategoryById(spu.getCid2()),
                categotyClientServer.findCategoryById(spu.getCid3())
        );

        model.addAttribute("categoryList",categoryList);


        //一 specparm详情 根据cid 是否通用查询
        List<SpecParam> specParamList = specClientServer.findSpecParamsByCidAndGeneric(spu.getCid3(),false);

        //规格参数的特殊属性
        Map<Long,String> specParamMap=new HashMap<>();
        //存的是id跟名称
        specParamList.forEach(specParam -> {
            specParamMap.put(specParam.getId(),specParam.getName());
        });

        model.addAttribute("specParamMap",specParamMap);

        //一 品牌
        Brand brand = brandClientServer.findBrandById(spu.getBrandId());
        model.addAttribute("brand",brand);


        return  "item";

    }



}
