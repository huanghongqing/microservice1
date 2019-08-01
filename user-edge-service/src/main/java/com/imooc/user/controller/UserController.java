package com.imooc.user.controller;

import com.imooc.thrift.user.UserInfo;
import com.imooc.user.DTO.UserDTO;
import com.imooc.user.redis.RedisClient;
import com.imooc.user.response.LoginResponse;
import com.imooc.user.response.Response;
import com.imooc.user.thrift.ServiceProvider;
import org.apache.thrift.TException;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Controller
public class UserController {

    @Autowired
    private ServiceProvider serviceProvider;
    @Autowired
    private RedisClient redisClient;

    @RequestMapping(value="/login",method = RequestMethod.POST)
    @ResponseBody
    public Response  login(@RequestParam("username") String username,
                      @RequestParam("password") String password){
        //1 验证用户名密码
        UserInfo userInfo=null;
        try {
             userInfo=serviceProvider.getUserService().getUserByName(username);
        } catch (TException e) {
            e.printStackTrace();
            return  Response.USERNAME_PASSWORD_INVALID;
        }
        if(userInfo==null){
            return Response.USERNAME_PASSWORD_INVALID;
        }
        if(!userInfo.getPassword().equalsIgnoreCase(md5(password))){
            System.out.println("Md5");
            return Response.USERNAME_PASSWORD_INVALID;
        }
        //2 生成token
        String token = genToken();

        //3 缓存用户和token
        redisClient.set(token,toDTO(userInfo),3600);
        return new LoginResponse(token);

    }

    private UserDTO toDTO(UserInfo userInfo){
        UserDTO  userDTO=new UserDTO();
        BeanUtils.copyProperties(userInfo,userDTO);
        return userDTO;
    }

    private String genToken(){

        return randomCode("0123456789abcdefghijklmnopqrstuvwxyz",32);
    }
    private String randomCode(String s,int size){

        StringBuffer result=new StringBuffer(size);
        Random random=new Random(s.length());
        for(int i=0;i<size;i++){
            int loc=random.nextInt(s.length());
            result.append(s.charAt(loc));
        }
        return result.toString();
    }
    private String md5(String password){
        try {
            MessageDigest md5=MessageDigest.getInstance("MD5");
            byte[] md5Bytes=md5.digest(password.getBytes("utf-8"));
            return HexUtils.toHexString(md5Bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
