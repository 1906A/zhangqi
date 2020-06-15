package com.leyou.service;

import com.leyou.dao.UserMapper;
import com.leyou.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public Boolean check(String data, Integer type) {
        User user = new User();
        Boolean result= false;
        //判断校验的是谁 1用户名  2手机
        if(type==1){
            user.setUsername(data);
        }
        else if(type==2){
            user.setPhone(data);
        }

        //根据校验内容去数据库查询

        User selectOne = userMapper.selectOne(user);
        // 用户名存在false 不存在 ture   手机号存在false 不存在 ture
        if(selectOne==null){
            result =  true;
        }

        return  result;
    }
}