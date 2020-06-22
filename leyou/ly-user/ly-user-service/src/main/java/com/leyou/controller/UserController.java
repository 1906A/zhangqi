package com.leyou.controller;

import com.leyou.pojo.User;
import com.leyou.service.UserService;
import com.leyou.utils.CodeUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    /**实现用户数据的校验 手机号 用户名的唯一校验
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public Boolean checkUserData(@PathVariable("data") String data, @PathVariable(value = "type") Integer type) {
        System.out.println("校验"+data+"====="+type);

        Boolean b=userService.check(data,type);
        return b;
    }


    /**根据用户输入的手机号生成随机验证码
     * @param phone
     */
    @PostMapping("/code")
    public void code(@RequestParam("phone") String phone){

        System.out.println("校验："+phone);

        //生成一个六位数随机码 code
        String code = CodeUtils.messageCode(6);

        //调用短信服务发送验证码 phone code 使用rabbitmq发送短信
        Map<String,String> map=new HashMap<>();
        map.put("phone",phone);
        map.put("code",code);

        //临时注销掉
       amqpTemplate.convertAndSend("sms.changes","sms.send",map);


        //发送短信后存放redis 放验证码 code
        //有效期5分钟
        stringRedisTemplate.opsForValue().set("lysms_"+phone,code,5, TimeUnit.MINUTES);





    }

    /**用户注册
     * @param user
     * @param code
     */
    @PostMapping("/register")
    public void register(@Valid User user , String code){

        System.out.println("用户注册:"+user.getUsername()+"code："+code);

        if(user!=null){
            //从redis中获取code
            String redisCode = stringRedisTemplate.opsForValue().get("lysms_" + user.getPhone());
            //判断code验证码是否一致
            if(code.equals(redisCode)){
                userService.add(user);
            }

        }





    }


    /**查询用户 根据用户名 密码
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    public User query(@RequestParam("username")String username,@RequestParam("password")String password){

        System.out.println("校验username："+username+"password："+password);
        //1：根据用户名查询用户信息
        User user  = userService.findusername(username);
        if(user!=null){
            //2：比对密码
            String newPassword = DigestUtils.md5Hex(password + user.getSalt());
            System.out.println("newPassword:"+newPassword);
            System.out.println("password:"+user.getPassword());
            if(user.getPassword().equals(newPassword)){
                return user;
            }
        }
        return null;
    }


    /**登录校验
     * @param username
     * @param password
     * @return
     */
    @PostMapping("login")
    public String login(@RequestParam("username")String username, @RequestParam("password")String password){

        String result="1";

        User user=userService.findusername(username);

        if(user!=null){

            //比对密码
            String md5Hex = DigestUtils.md5Hex(password + user.getSalt());

            if(md5Hex.equals(user.getPassword())){

                result ="0";
            }


        }

       return  result;

    }

}
