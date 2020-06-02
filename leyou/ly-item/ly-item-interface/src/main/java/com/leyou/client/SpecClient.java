package com.leyou.client;

import com.leyou.pojo.SpecParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("spec")
public interface SpecClient {

    @RequestMapping("paramByCid")
    public List<SpecParam> findSpecParamsByCid1(@RequestParam("cid")Long cid);
}
