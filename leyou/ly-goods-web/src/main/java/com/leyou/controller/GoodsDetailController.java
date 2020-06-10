package com.leyou.controller;

import com.leyou.client.*;
import com.leyou.listener.MessageListener;
import com.leyou.pojo.*;
import com.leyou.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

@Controller
public class GoodsDetailController {


   /* @Autowired
    BrandClientServer brandClientServer;

    @Autowired
    CategotyClientServer categotyClientServer;

    @Autowired
    SkuCilentServer skuCilentServer;

    @Autowired
    SpecClientServer specClientServer;

    @Autowired
    SpuClientServer spuClientServer;

*/
  /*  @Autowired
    TemplateEngine templateEngine;*/

    //rabbit同步数据
    @Autowired
    GoodService goodService;


    @RequestMapping("hello")
    public String hello(Model model) {

        String name = "哈哈";
        model.addAttribute("name", name);

        return "hello";

    }


    /**
     * 请求商品详情的微服务
     * 1：spu
     * 2：spudetail
     * 3：sku
     * 4：规格参数组
     * <p>
     * 5：规格参数详情
     * 6；三级分类
     * 7 品牌
     *
     * @param spuId
     * @param model
     * @return
     */
    @RequestMapping("item/{spuId}.html")
    public String item(@PathVariable("spuId") Long spuId, Model model) {

        //获取查询数据

        Map<String, Object> map = goodService.item(spuId);

        model.addAllAttributes(map);

        //写入文件
        /*createHtml(spu, spuDetail, skuList, specGroupList, categoryList, brand, paramMap);
*/
        goodService.createHtml(spuId);

        return "item";

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
  /*  public void createHtml(Spu spu, SpuDetail spuDetail, List<Sku> skuList, List<SpecGroup> specGroupList, List<Category> categoryList, Brand brand, Map<Long, String> paramMap) {

        PrintWriter writer = null;

        try {
            // 创建thymeleaf上下文对象
            Context context = new Context();
            // 把数据放入上下文对象
            context.setVariable("spu", spu);
            context.setVariable("spuDetail", spuDetail);
            context.setVariable("skuList", skuList);
            context.setVariable("specGroupList", specGroupList);
            context.setVariable("categoryList", categoryList);
            context.setVariable("brand", brand);
            context.setVariable("paramMap", paramMap);

            // 创建输出流
            File file = new File("F:\\nginx-1.16.1\\html\\" + spu.getId() + ".html");
            writer = new PrintWriter(file);
            // 执行页面静态化方法
            templateEngine.process("item", context, writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            writer.close();
        }


    }*/


}
