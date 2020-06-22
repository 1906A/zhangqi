package com.leyou.controller;

import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.pojo.SkuVo;
import com.leyou.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CartController {

            /*
            * 操作购物车数据放入redis里面 hash类型
            *
            *加入购物车
            * 修改购物车
            * 删除购物车
            * 查询购物车
            *
            *
            * */

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    com.leyou.config.JwtProperties JwtProperties;

    private String prifix="ly_carts_";

    //登录状态下渲染购物车 操作当前对象 ， 需要再存一份reidis
    private String prifixSelectedSku="ly_carts_SelectedSku_";

    /** 增加购物车
     * @param token
     * @param skuVo
     */
    @RequestMapping("add")
    public void add(@CookieValue("token") String token,@RequestBody SkuVo skuVo){
        UserInfo userInfo = getUserInfoByToken(token);
        //添加购物车
        if(userInfo!=null){
            BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(prifix + userInfo.getId());

            //判断redis得信息
            if(hashOps.hasKey(skuVo.getId()+"")){
                //从redis中获取已存在的购物车信息
                SkuVo redisSkuVo=JsonUtils.parse( hashOps.get(skuVo.getId()+"").toString(),SkuVo.class);
                //修改购物车信息中的数量
                redisSkuVo.setNum(redisSkuVo.getNum()+skuVo.getNum());
                //重新存放redis                   //将对象转换成json
                hashOps.put(skuVo.getId()+"",JsonUtils.serialize(redisSkuVo));

                // 当前 操作得 sku 单独存 redis
                stringRedisTemplate.boundValueOps(prifixSelectedSku + userInfo.getId()).set(JsonUtils.serialize(redisSkuVo));

            }else {
                //第一次往redis中存入商品信息

                hashOps.put(skuVo.getId()+"",JsonUtils.serialize(skuVo));

                // 当前 操作得 sku 单独存 redis
                stringRedisTemplate.boundValueOps(prifixSelectedSku + userInfo.getId()).set(JsonUtils.serialize(skuVo));
            }

        }

    }


    /**
     * 去redis获取最新操作的sku信息
     * @param token
     * @return
     */
    @PostMapping("selectedSku")
    public SkuVo selectedSku(@CookieValue("token")String token){
        UserInfo userInfo = this.getUserInfoByToken(token);
        //从redis获取最新的sku信息   json
        String s = stringRedisTemplate.boundValueOps(prifixSelectedSku + userInfo.getId()).get();

        SkuVo skuVo = JsonUtils.parse(s, SkuVo.class);
        return skuVo;
    }







    /**修改购物车
     * @param token
     * @param skuVo
     */
   @RequestMapping("update")
    public void update(@CookieValue("token") String token,@RequestBody SkuVo skuVo){

       UserInfo userInfo = getUserInfoByToken(token);
       //添加购物车
       if(userInfo!=null){
           BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(prifix + userInfo.getId());

           //判断redis得信息
           if(hashOps.hasKey(skuVo.getId()+"")){
               //从redis中获取已存在的购物车信息
               SkuVo redisSkuVo=JsonUtils.parse( hashOps.get(skuVo.getId()+"").toString(),SkuVo.class);
               //修改购物车信息中的数量   修改减少
               redisSkuVo.setNum(skuVo.getNum());
               //重新存放redis                   //将对象转换成json
               hashOps.put(skuVo.getId()+"",JsonUtils.serialize(redisSkuVo));

           }else {
               //第一次往redis中存入商品信息

               hashOps.put(skuVo.getId()+"",JsonUtils.serialize(skuVo));
           }

       }



  }

    /**删除购物车
     * @param token
     * @param id
     */
   @RequestMapping("delete")                                                 //skuid
    public void delete(@CookieValue("token") String token,@RequestParam("id") Long id){
       UserInfo userInfo = getUserInfoByToken(token);
       //添加购物车
       if(userInfo!=null){
           BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.
                   boundHashOps(prifix + userInfo.getId());
            //根据skuid  key删除
           hashOps.delete(id+"");

       }

  }


    /**查询购物车
     * @param token
     * @return
     */
   @RequestMapping("query")
    public List<SkuVo> query(@CookieValue("token") String token){
       UserInfo userInfo = getUserInfoByToken(token);
       List<SkuVo> list=new ArrayList<>();
       //添加购物车
       if(userInfo!=null){
           BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(prifix + userInfo.getId());
           //遍历reids的数据
           Map<Object, Object> map = hashOps.entries();
           map.keySet().forEach(key -> {
               SkuVo skuVo=JsonUtils.parse( hashOps.get(key).toString(),SkuVo.class);
               list.add(skuVo);
           });

       }
              return  list;
  }

    /** 解析token 判断是否登录状态
     * 登陆后根据cookie解析cookie信息
     * @return
     */
  public  UserInfo getUserInfoByToken(String token){
      UserInfo userInfo=new UserInfo();
      try {
           userInfo=JwtUtils.getInfoFromToken(token,JwtProperties.getPublicKey());
      } catch (Exception e) {
          e.printStackTrace();
      }

      return  userInfo;
  }



}
