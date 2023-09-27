package com.rui.controller;

import com.rui.common.ServerResponse;
import com.rui.mapper.UserMapper;
import com.rui.pojo.user;
import com.rui.service.UserService;
import com.rui.util.AliyunOSSUtils;
import com.rui.util.RedisUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RestController
public class UserController {
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
    /*
     * * 头像上传
     */
    @RequestMapping("/register/user/upLoadHead")
    public ServerResponse<Boolean> HeadUpLoad(String uid, MultipartFile file){
        try{
        //String phone = userMapper.queryPhoneByUid(uid);
        String url = aliyunOSSUtils.uploadByMul(file);
        userMapper.addHeadUrl(uid,url);
        log.info("[Success]头像添加成功");
        return ServerResponse.createBySuccessMsgData(url,true);
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
    /*
     * * 修改头像
     */
    @RequestMapping("/user/updateHead")
    public ServerResponse<Boolean> HeadUpdate(String uid, MultipartFile file){
        try{
            String headUrl=userMapper.queryHeadUrlByUid(uid);
            if(headUrl==null)
                return HeadUpLoad(uid,file);
        aliyunOSSUtils.deleteBlog(headUrl);
        log.info("[Success]删除头像成功");
        String url = aliyunOSSUtils.uploadByMul(file);
        log.info("[Success]装载头像成功");
        userMapper.addHeadUrl(uid,url);
        return ServerResponse.createBySuccessMsgData(url,true);
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
    /*
     * * 修改账号（电话）
     */
    @RequestMapping("/user/updatePhone")
    public ServerResponse<Boolean> PhoneUpdate(String newPhone,String uid){
        try{
            if(userService.updateUserPhone(newPhone,uid)!=0){
                log.info("[Success]修改账号成功");
                return ServerResponse.createBySuccessMsg("修改成功");
            }
        log.info("[Error]修改账号失败");
        return ServerResponse.createByErrorMsg("修改失败");
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
    /*
     * * 更新信息
     */
    @RequestMapping("/user/updateInfo")
    public ServerResponse<Boolean> updateUserInfo(user user){
        try{
            userService.updateUserInfo(user);
        log.info("[Success]用户信息修改成功");
        return ServerResponse.createBySuccessMsg("修改成功");
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
    /*
    * * 退出登录
     */
    @RequestMapping("/user/quitLogin")
    public ServerResponse<Boolean> quitLogin(String uid){
        try{
        boolean delete = redisUtils.delete(uid);
        if(delete){
            log.info("[Success]退出成功");
            return ServerResponse.createBySuccessMsg("退出成功");
        }
        log.info("[Error]退出失败");
        return ServerResponse.createByErrorMsg("退出失败");
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
    /*
    * * 注销账户
     */
    @RequestMapping("/user/deleteUser")
    public ServerResponse<Boolean> deleteUser(String uid){
        try {
            userService.deleteUserByUid(uid);
            redisUtils.delete(uid);
            log.info("[Success]删除成功");
            return ServerResponse.createBySuccessMsg("删除成功");
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
}
