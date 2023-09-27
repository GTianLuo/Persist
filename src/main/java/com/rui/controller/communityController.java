package com.rui.controller;

import com.rui.common.ResponseCode;
import com.rui.common.ServerResponse;
import com.rui.util.AliyunOSSUtils;
import com.rui.util.RedisUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.rui.pojo.*;
import com.rui.service.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RestController
public class communityController {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    communityService communityService;
    @Autowired
    AliyunOSSUtils aliyunOSSUtils;
    @RequestMapping("/community/addCommunity")
    public ServerResponse<Boolean>  addCommuinty(community community, MultipartFile[] pictures){
          try {
              communityService.addCommunity(community);

              String communityRedisUid = communityService.queryCommunityUidByUidAndBeganTime(community.getUid(), community.getBeganTime()) + "communityPictures";
              for (MultipartFile file : pictures) {
                  String s = aliyunOSSUtils.uploadByMul(file);
                  redisUtils.setList(communityRedisUid, s);
              }
              //List<String> pictureUrl=new ArrayList<>();
              log.info("[Success]社区信息添加成功");
              return ServerResponse.createBySuccessMsg("添加成功");
          }catch (Exception e){
              System.out.println(e);
              log.info("服务器异常");
            return  ServerResponse.createByErrorMsg("服务器异常");
          }
    }
    @RequestMapping("/community/deleteCommunity")
    public ServerResponse<Boolean> deleteCommunity(String communityUid){
        try{
            communityService.deleteCommuinty(communityUid);
            redisUtils.delete(communityUid+"communityPictures");
            log.info("[Success]删除社区成功");
            return ServerResponse.createBySuccessMsg("删除成功");
        }catch (Exception e){
            log.info("服务器异常");
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
    @RequestMapping("/community/queryCommunityList")
    public ServerResponse<List<community>> queryCommunityList(){
        List<community> communities = communityService.queryCommunityList();
        for(community i:communities){
            i.setPicture(redisUtils.getAllList(i.getCommunityUid()+"communityPictures"));
        }
        return ServerResponse.createBySuccessMsgData("查询成功",communities);
    }
}
