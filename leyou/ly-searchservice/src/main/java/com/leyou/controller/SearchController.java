package com.leyou.controller;

import com.leyou.common.PageResult;
import com.leyou.pojo.Goods;
import com.leyou.pojo.SearchRequest;
import com.leyou.repository.GoodsRepository;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("search")
public class SearchController {


    @Autowired
    GoodsRepository goodsRepository;

    @RequestMapping("page")
    public PageResult<Goods> findAllGoods(@RequestBody SearchRequest searchRequest){

        //获取前台发送的查询数据和分页页数
        System.out.println(searchRequest.getKey()+"==="+searchRequest.getPage());

        //创建 NativeSearchQueryBuilder对象
        NativeSearchQueryBuilder queryBuilder=new NativeSearchQueryBuilder();

        //通过match查询all中对应的字段
        queryBuilder.withQuery(QueryBuilders.matchQuery("all",searchRequest.getKey()).operator(Operator.AND));

        //进行分页
        queryBuilder.withPageable(PageRequest.of(searchRequest.getPage()-1,searchRequest.getSize()));

        //排序
        queryBuilder.withSort(SortBuilders.fieldSort(searchRequest.getSortBy()).order(searchRequest.isDescending() ? SortOrder.ASC : SortOrder.DESC ));

        Page<Goods> page = goodsRepository.search(queryBuilder.build());

        return new PageResult<Goods>(page.getTotalElements(), page.getContent(), page.getTotalPages());
    }
}
