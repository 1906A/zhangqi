package com.leyou;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {
    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @Test
    public void test(){


        System.out.println(1111);
    }



    @Test
    public void hash(){

        //hash
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps("ly_carts");


        hashOps.put("skuid_124","{\"titile\":\"华为手机\"}");

        System.out.println(hashOps.get("skuid_124"));



    }

    @Test
    public void hashOps(){

        //hash
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps("ly_carts");

        Map<Object, Object> entries = hashOps.entries();
        entries.forEach((o, o2) -> {

            System.out.println(o+"====================="+entries.get(o));

        });


        //System.out.println(hashOps.get("skuid_123"));



    }

}
