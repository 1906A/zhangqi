package com.leyou.pojo;


import com.leyou.common.PageResult;

import java.util.List;

//对pageresult扩展的类  不能污染pagerult
public class SearchResult extends PageResult<Goods> {

    List<Category> categoryList;
    List<Brand> brandList;

    //新追加的有参构造


    public SearchResult(Long total, List<Goods> items, Integer totalPage, List<Category> categoryList, List<Brand> brandList) {
        super(total, items, totalPage);
        this.categoryList = categoryList;
        this.brandList = brandList;
    }

    //对父类进行有参构造
    public SearchResult() {
    }

    public SearchResult(Long total) {
        super(total);
    }

    public SearchResult(Long total, List<Goods> items) {
        super(total, items);
    }

    public SearchResult(Long total, List<Goods> items, Integer totalPage) {
        super(total, items, totalPage);
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
}
