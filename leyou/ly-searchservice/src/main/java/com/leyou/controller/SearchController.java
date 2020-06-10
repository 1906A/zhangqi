package com.leyou.controller;

import com.leyou.client.BrandClientServer;
import com.leyou.client.CategotyClientServer;
import com.leyou.client.SpecClientServer;
import com.leyou.common.PageResult;
import com.leyou.pojo.*;
import com.leyou.repository.GoodsRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //三
    @Autowired
    SpecClientServer specClientServer;


    @RequestMapping("page")
    public PageResult<Goods> findAllGoods(@RequestBody SearchRequest searchRequest) {

        //一获取前台发送的查询数据和分页页数
        System.out.println(searchRequest.getKey() + "===" + searchRequest.getPage());

        //一创建 NativeSearchQueryBuilder对象
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //一通过match查询all中对应的字段
        queryBuilder.withQuery(QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND));


        //三过滤filter查询条件
        //三匹配规则参数时改造
        BoolQueryBuilder queryBuilder1 = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("all", searchRequest.getKey()));
        //判断过滤条件
        if (searchRequest.getFilter() != null && searchRequest.getFilter().size() > 0) {
            searchRequest.getFilter().keySet().forEach(key -> {
                String filed = "specs." + key + ".keyword";
                if (key.equals("分类")) {
                    filed = "cid3";
                } else if (key.equals("品牌")) {
                    filed = "brandId";
                }
                //根据过滤条件过滤
                queryBuilder1.filter(QueryBuilders.termQuery(filed, searchRequest.getFilter().get(key)));
            });
        }
        //组装过滤条件
        queryBuilder.withQuery(queryBuilder1);

        //一进行分页
        queryBuilder.withPageable(PageRequest.of(searchRequest.getPage() - 1, searchRequest.getSize()));

        //一排序
        queryBuilder.withSort(SortBuilders.fieldSort(searchRequest.getSortBy()).order(searchRequest.isDescending() ? SortOrder.ASC : SortOrder.DESC));

        //二加载分类和品牌
        String categoryName = "categoryName";
        String brandName = "brandName";

        //二聚合查询
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryName).field("cid3"));


        //二
        queryBuilder.addAggregation(AggregationBuilders.terms(brandName).field("brandId"));
        //二 查询  强转
        AggregatedPage<Goods> search = (AggregatedPage<Goods>) goodsRepository.search(queryBuilder.build());

        //二页面展示分类集合
        List<Category> categoryList = new ArrayList<>();
        //二 构造分页信息--根据分类id获取名称 强转 LongTerms   stringTerms doubleTerms
        LongTerms categoryAgg = (LongTerms) search.getAggregation(categoryName);

        //二 不知道根据三级分类聚合能聚合桶多少条数据，所以循环遍历
        categoryAgg.getBuckets().forEach(bucket -> {
            Long categoryId = (Long) bucket.getKey();

            //根据分类id去数据库查询
            Category category = categotyClientServer.findCategoryById(categoryId);
            //将查到的分类添加到集合中去
            categoryList.add(category);


        });

        //二页面展示品牌集合
        List<Brand> brandList = new ArrayList<>();
        //二 构造分页信息--根据品牌id获取名称 强转 LongTerms   stringTerms doubleTerms
        LongTerms brandAgg = (LongTerms) search.getAggregation(brandName);


        //二 不知道根据品牌聚合能聚合桶多少条数据，所以循环遍历
        brandAgg.getBuckets().forEach(bucket -> {
            Long brandId = (Long) bucket.getKey();
            //根据分类id去数据库查询
            Brand brand = brandClientServer.findBrandById(brandId);
            //将查到的品牌添加到集合中去
            brandList.add(brand);
        });


        //三构造规格参数组数据
        //规格参数是根据分类的id获取的
        //三 根据三级分类id查询规格参数，集合是Map形式的集合
        List<Map<String, Object>> paramList = new ArrayList<>();
        //如果分类能查到   默认前端显示栏显示一个分类
        if (categoryList.size() == 1) {
            //规格参数是根据分类的id获取的
            List<SpecParam> specParams = specClientServer.findSpecParamsByCid1(categoryList.get(0).getId());
            //遍历分组的属性
            specParams.forEach(specParam -> {
                //得到Map的key值
                String key = specParam.getName();
                //聚合查询                                              设置不分词
                queryBuilder.addAggregation(AggregationBuilders.terms(key).field("specs" + "." + key + "." + "keyword"));
            });
        }

        //三
        //重新查询数据  强转
        AggregatedPage<Goods> search1 = (AggregatedPage<Goods>) goodsRepository.search(queryBuilder.build());

        //获取桶内的属性转为Map集合
        Map<String, Aggregation> aggregationMap = search1.getAggregations().asMap();

        //因为分类和品牌聚合查询完有id
        //遍历Map集合
        aggregationMap.keySet().forEach(mKey -> {

            //三把品牌和分类的聚合过滤掉
            if (!(mKey.equals(categoryName) || mKey.equals(brandName))) {
                //转换数据类型
                StringTerms aggregation = (StringTerms) aggregationMap.get(mKey);
                //封装到Map集合
                Map<String, Object> map = new HashMap<>();
                map.put("key", mKey);

                List<Map<String, String>> list = new ArrayList<>();
                //遍历桶集合
                aggregation.getBuckets().forEach(bucket -> {
                    Map<String, String> valueMap = new HashMap<>();
                    valueMap.put("name", bucket.getKeyAsString());
                    list.add(valueMap); //对应的属性没有id，封装到options中为对象
                });

                map.put("options", list);

                //获取到的参数加入paramlist集合
                paramList.add(map);
            }

        });


        //一执行查询  tatalelement 总条数 totalpage 总页数  count 数据内容
        // Page<Goods> page = goodsRepository.search(queryBuilder.build());

        //return new PageResult<Goods>(page.getTotalElements(), page.getContent(), page.getTotalPages());
        //二聚合查询 分类加品牌
//        return new SearchResult(search.getTotalElements(), search.getContent(), search.getTotalPages(),
//                categoryList,brandList
//                );
        //二聚合查询 分类加品牌 规格组
        return new SearchResult(search.getTotalElements(), search.getContent(), search.getTotalPages(),
                categoryList, brandList, paramList);


    }
}
