package com.leyou.controller;

import com.leyou.client.*;
import com.leyou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

       //一 specgroup
        List<SpecGroup> specGroupList = specClientServer.findAllSpecGroup(spu.getCid3());
        model.addAttribute("specGroupList",specGroupList);
        //一 specparm
        List<SpecParam> params = specClientServer.findSpecParamsByCid1(spu.getCid1());

        List<SpecParam> specParamList=new ArrayList<>();

        params.forEach(param -> {
            //判断条件
            if(!param.getGeneric()){
                specParamList.add(param);
            }

        });
        model.addAttribute("specParamList",specParamList);

        List<Category> categoryList= Arrays.asList(
                categotyClientServer.findCategoryById(spu.getCid1()),
                categotyClientServer.findCategoryById(spu.getCid2()),
                categotyClientServer.findCategoryById(spu.getCid3())
        );

        model.addAttribute("categoryList",categoryList);

        return  "item";

    }



}
