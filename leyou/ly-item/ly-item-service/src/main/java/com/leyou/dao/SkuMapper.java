package com.leyou.dao;

import com.leyou.pojo.Sku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SkuMapper extends Mapper<Sku> {

    @Select("SELECT * FROM `tb_sku` k WHERE k.spu_id=#{id} and k.enable=1 ")
    List<Sku> findSkuBySpuId(Long id);



    @Select("select * from tb_sku where spu_id=#{spuId}")
    List<Sku> findskusByspuId(@Param("spuId") Long spuId);
}
