package com.rui.controller;

import com.rui.common.ServerResponse;
import com.rui.mapper.UserMapper;
import com.rui.util.RedisUtils;
import com.rui.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.rui.pojo.*;
import com.rui.util.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
/*
@author XiAoRayL
 */
@Log4j2
@RestController
public class loginController {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    AliyunOSSUtils aliyunOSSUtils;
    private final Logger logger=Logger.getLogger("login");
    /*
    * * 测试接口
     */
    @RequestMapping("/test")
    public ServerResponse<Boolean> test(){
        return ServerResponse.createBySuccessMsg("权限测试成功！");
    }
    /*
    * * 处理没有权限
     */
    @RequestMapping("/login403")
    public ServerResponse<Boolean> login403(){
        return ServerResponse.createByErrorMsg("没有权限");
    }
    /*
     * * 密码登录
     */
    @RequestMapping("/login/byPassword")
    public ServerResponse<user> loginByPassword(@RequestParam("phone") String phone, @RequestParam("password") String password, HttpServletResponse response){
      try{
          String uid=userService.queryUidByPhone(phone);
          if(uid==null){
              log.info("[Warn]该用户不存在");
              return ServerResponse.createByErrorMsg("该用户不存在");
          }
        //根据uid查对应的密码
        log.info("[Success]查询到对应的uid:"+uid);
        String passwordByUid = userMapper.queryPasswordByUid(uid);
        //passwordByUid="abc";
        user user = userMapper.queryUserByPhone(phone);
        if(passwordByUid.equals(password))
        {

           // result.put("uid",uid);
            String token=new TokenUtils().getToken(uid);
            log.info("[Success]token生成成功");
            //System.out.println(token);
            redisUtils.set(uid,token,14*24*60*60);
            response.addHeader("Access-Control-Expose-Headers","token");
            response.addHeader("token",token);
            log.info("[Success]登录成功");
            return ServerResponse.createBySuccessMsgData("登录成功",user);
        }
        else {
            log.info("[Warn]该用户密码错误");
            return ServerResponse.createByErrorMsg("登录失败");
        }
      }catch (Exception e){
          log.info("服务器异常！");
          System.out.println(e);
          return ServerResponse.createByErrorMsg("服务器异常");
      }
    }
    /*
    * * 注册添加信息接口
     */
    @RequestMapping("/register/addInfo")
    public ServerResponse<Boolean> register(user user){
       try{
           String uid=userMapper.queryUidByPhone(user.getPhone());
        if(uid!=null) {
            log.info("[error]账号已经存在");
            return ServerResponse.createByErrorMsgData("账号已经存在", false);
        }
        userMapper.addUser(user);
        log.info("[Success]注册成功");
        String qUid=userMapper.queryUidByPhone(user.getPhone());
        return ServerResponse.createBySuccessMsgData(qUid,true);
       }catch (Exception e){
           log.info("服务器异常！");
           System.out.println(e);
           return ServerResponse.createByErrorMsg("服务器异常");
       }
    }
    /*
    * * 登录验证码验证接口
     */
    @RequestMapping("/login/byCode")
    public ServerResponse<user> LoginCodeTest(@RequestParam("phone") String phone, @RequestParam("code") String code, HttpServletResponse response){
        try{
            String uid=userService.queryUidByPhone(phone);
            if(uid==null){
                String redisCode=redisUtils.get(phone);
                if(redisCode!=null && redisCode.equals(redisCode)){
                    return ServerResponse.createBySuccessMsg("未注册验证码正确");
                }
                return ServerResponse.createByErrorMsg("未注册验证码不正确");
            }
            String RedisCode= redisUtils.get(phone);
        if(RedisCode!=null && RedisCode.equals(code)) {
            log.info("[Success]验证码匹配成功");
            //String uid=userService.queryUidByPhone(phone);
            String token=new TokenUtils().getToken(uid);
           // System.out.println(token);
            redisUtils.set(uid,token,14*24*60*60);
            response.addHeader("Access-Control-Expose-Headers","token");
            response.addHeader("token",token);
            log.info("[Success]token设置成功");
            user user = userMapper.queryUserByPhone(phone);
            return ServerResponse.createBySuccessMsgData("已注册验证码正确", user);
        }
        log.info("[Error]验证码匹配失败");
        return ServerResponse.createByErrorMsg("已注册验证码不正确");
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
    /*
    * * 注册验证码验证接口
     */
    @RequestMapping("/register/codeTest")
    public ServerResponse<Boolean> CodeTest(String phone,String code){
        try{
            if(userMapper.queryUidByPhone(phone)!=null) {

                log.info("[Error]此账号已经存在不允许注册");
                return ServerResponse.createByErrorMsgData("该账号已经存在", false);
            }
        String RedisCode= redisUtils.get(phone);
        if(RedisCode!=null && RedisCode.equals(code)) {
            log.info("[Success]验证码验证成功");
            return ServerResponse.createBySuccessMsgData("验证成功", true);
        }
        log.info("[Error]验证码验证失败");
        return ServerResponse.createByErrorMsgData("验证失败",false);
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }

    /*
    * * 验证码发送接口
     */
    @RequestMapping("/register/codeSend")
    public ServerResponse<Boolean> CodeSend(String phone){
        try{
            if(redisUtils.get(phone)!=null) {
                log.info("[Warn]两分钟内重复发送验证码信息");
                return ServerResponse.createByErrorMsg("两分钟内不能重复发送");
            }
        logger.info("[Success]=====>验证码开始发送");
        String VCode = CodeUtils.SendCode(phone);
        redisUtils.set(phone,VCode,120);
        log.info("[Success]验证码发送成功");
        return ServerResponse.createByErrorMsg("发送成功");
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
 /*   *//*
    * * 头像上传
     *//*
    @RequestMapping("/register/user/upLoadHead")
    public ServerResponse<Boolean> HeadUpLoad(String uid, MultipartFile file){
        //String phone = userMapper.queryPhoneByUid(uid);
        String url = aliyunOSSUtils.uploadByMul(file);
        userMapper.addHeadUrl(uid,url);
        return ServerResponse.createBySuccessMsgData(url,true);
    }
    *//*
    * * 修改头像
     *//*
    @RequestMapping("/user/updateHead")
    public ServerResponse<Boolean> HeadUpdate(String uid, MultipartFile file){
        aliyunOSSUtils.deleteBlog(userMapper.queryHeadUrlByUid(uid));
        log.info("[Success]删除头像成功");
        String url = aliyunOSSUtils.uploadByMul(file);
        log.info("[Success]装载头像成功");
        userMapper.addHeadUrl(uid,url);
        return ServerResponse.createBySuccessMsgData(url,true);
    }*/
   /* *//*
    * * 修改账号（电话）
     *//*
    @RequestMapping("/user/updatePhone")
    public ServerResponse<Boolean> PhoneUpdate(String newPhone,String uid){
             if(userService.updateUserPhone(newPhone,uid)!=0){
                 log.info("[Success]修改账号成功");
                 return ServerResponse.createBySuccessMsg("修改成功");
             }
             log.info("[Error]修改账号失败");
             return ServerResponse.createByErrorMsg("修改失败");

    }
    *//*
    * * 更新信息
     *//*
    @RequestMapping("/user/updateInfo")
    public ServerResponse<Boolean> updateUserInfo(user user){
        userService.updateUserInfo(user);
        log.info("用户信息修改成功");
        return ServerResponse.createBySuccessMsg("修改成功");
    }*/
    @RequestMapping("/testImg")
    public ServerResponse<String> testImg(MultipartFile[] file){
        List<String> res=new ArrayList<>();
        System.out.println("abc");
        for(MultipartFile i:file){
            String s = aliyunOSSUtils.uploadByMul(i);
            res.add(s);
        }

        return ServerResponse.createBySuccessMsgData("上传成功",res.toString());
    }
}
