package com.leyou.service;

import com.leyou.dao.UserMapper;
import com.leyou.pojo.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

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

    public void add(User user) {

        //盐值 uuid 生成
        String salt = UUID.randomUUID().toString().substring(0, 32);

        String md5 = getPsw(user.getPassword(), salt);

        user.setPassword(md5);

        user.setCreated(new Date());

        user.setSalt(salt);
        userMapper.insert(user);
    }


    /** 生成m5加密的密码
     * @param password
     * @param salt
     * @return
     */
    public String getPsw(String password,String salt){

        String md5Hex = DigestUtils.md5Hex(password + salt);


        return  md5Hex;
    }

    public User findusername(String username) {

        User user = new User();
        user.setUsername(username);

        return  userMapper.selectOne(user);

    }
}