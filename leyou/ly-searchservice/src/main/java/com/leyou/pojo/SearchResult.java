package com.leyou.pojo;


import com.leyou.common.PageResult;

import java.util.List;
import java.util.Map;

//对pageresult扩展的类  不能污染pagerult
public class SearchResult extends PageResult<Goods> {
    //二
    private List<Category> categoryList;
    private List<Brand> brandList;
    //三
    private List<Map<String, Object>> paramList;
    //新追加的有参构造


    public SearchResult(Long total, List<Goods> items, Integer totalPage) {
        super(total, items, totalPage);
    }

    public SearchResult(Long total, List<Goods> items, Integer totalPage, List<Category> categoryList, List<Brand> brandList, List<Map<String, Object>> paramList) {
        super(total, items, totalPage);
        this.categoryList = categoryList;
        this.brandList = brandList;
        this.paramList = paramList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }

    public List<Map<String, Object>> getParamList() {
        return paramList;
    }

    public void setParamList(List<Map<String, Object>> paramList) {
        this.paramList = paramList;
    }
}
