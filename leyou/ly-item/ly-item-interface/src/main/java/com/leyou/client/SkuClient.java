package com.leyou.client;

import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("sku")
public interface SkuClient {

    @RequestMapping("list")
    public List<Sku> findSkuBySpuId(@RequestParam("id") Long id);


}
