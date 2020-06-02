package com.leyou.controller;

import com.leyou.client.BrandClientServer;
import com.leyou.client.CategotyClientServer;
import com.leyou.common.PageResult;
import com.leyou.pojo.*;
import com.leyou.repository.GoodsRepository;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("search")
public class SearchController {

    //一
    @Autowired
    GoodsRepository goodsRepository;
    //二
    @Autowired
    CategotyClientServer categotyClientServer;
    //二
    @Autowired
    BrandClientServer brandClientServer;


    @RequestMapping("page")
    public PageResult<Goods> findAllGoods(@RequestBody SearchRequest searchRequest){

        //一获取前台发送的查询数据和分页页数
       // 一测试  System.out.println(searchRequest.getKey()+"==="+searchRequest.getPage());

        //一创建 NativeSearchQueryBuilder对象
        NativeSearchQueryBuilder queryBuilder=new NativeSearchQueryBuilder();

        //一通过match查询all中对应的字段
        queryBuilder.withQuery(QueryBuilders.matchQuery("all",searchRequest.getKey()).operator(Operator.AND));

        //一进行分页
        queryBuilder.withPageable(PageRequest.of(searchRequest.getPage()-1,searchRequest.getSize()));

        //一排序
        queryBuilder.withSort(SortBuilders.fieldSort(searchRequest.getSortBy()).order(searchRequest.isDescending() ? SortOrder.ASC : SortOrder.DESC ));

        //二加载分类和品牌
        String categoryName="categoryName";
        String brandName="brandName";

        //二聚合查询
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryName).field("cid3"));


        //二
        queryBuilder.addAggregation(AggregationBuilders.terms(brandName).field("brandId"));
        //二 查询  强转
        AggregatedPage<Goods> search = (AggregatedPage<Goods>) goodsRepository.search(queryBuilder.build());

        //二 构造分页信息--根据分类id获取名称 强转 LongTerms   stringTerms doubleTerms
        LongTerms categoryAgg = (LongTerms) search.getAggregation(categoryName);

        //二页面展示分类集合
        List<Category> categoryList=new ArrayList<>();

        //二 不知道根据三级分类聚合能聚合桶多少条数据，所以循环遍历
        categoryAgg.getBuckets().forEach(bucket->{
            Long categoryId = (Long) bucket.getKey();

            //根据分类id去数据库查询
            Category category = categotyClientServer.findCategoryById(categoryId);
            //将查到的分类添加到集合中去
            categoryList.add(category);


        });



        //二 构造分页信息--根据品牌id获取名称 强转 LongTerms   stringTerms doubleTerms
        LongTerms brandAgg = (LongTerms) search.getAggregation(brandName);
        //二页面展示品牌集合
        List<Brand> brandList=new ArrayList<>();

        //二 不知道根据品牌聚合能聚合桶多少条数据，所以循环遍历
        brandAgg.getBuckets().forEach(bucket->{
            Long brandId = (Long) bucket.getKey();
            //根据分类id去数据库查询
            Brand brand = brandClientServer.findBrandById(brandId);
            //将查到的品牌添加到集合中去
            brandList.add(brand);
        });

        //一执行查询  tatalelement 总条数 totalpage 总页数  count 数据内容
       // Page<Goods> page = goodsRepository.search(queryBuilder.build());

        //return new PageResult<Goods>(page.getTotalElements(), page.getContent(), page.getTotalPages());
        return new SearchResult(search.getTotalElements(), search.getContent(), search.getTotalPages(),
                categoryList,brandList
                );
    }
}
