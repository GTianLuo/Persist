package com.rui.controller;

import com.rui.common.ServerResponse;
import com.rui.service.targetService;
import com.rui.util.DateUtils;
import com.rui.util.RedisUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rui.pojo.target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@Log4j2
public class targetController {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    targetService targetService;
    @Autowired
    DateUtils dateUtils;
   /*
   * *添加目标
    */
    @RequestMapping("/target/addTarget")
    public ServerResponse<Boolean> addTarget(target target){
        try{
            String tUid=redisUtils.get("targetUid");
            target.setTargetUid(tUid);
            long a=Long.parseLong(tUid);
            redisUtils.set("targetUid",String.valueOf(a+1));
            targetService.addTarget(target);
            log.info("[Success]目标添加成功");
            return ServerResponse.createBySuccessMsg("[Success]目标添加成功");
        }catch (Exception e){
            System.out.println(e);
            log.info("[Error]服务器异常");
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
    @RequestMapping("/target/addDailyTarget")
    public ServerResponse<Boolean> addDailyTarget(target target){
       try {
           System.out.println(target);
           String tUid=redisUtils.get("targetUid");
           target.setTargetUid(tUid);
           long a=Long.parseLong(tUid);
           redisUtils.set("targetUid",String.valueOf(a+1));
           String day = dateUtils.dateSplit(target.getBeganTime());
           System.out.println(day);
           if(redisUtils!=null) System.out.println("Redis非空");
           redisUtils.setList(target.getUid() + "target" + day, target);
           redisUtils.expire(target.getUid()+"target"+day,1);
           return ServerResponse.createBySuccessMsg("添加成功");
       }catch (Exception e){
           System.out.println(e);
           log.info("[Error]服务器异常");
           return ServerResponse.createByErrorMsg("服务器异常");
       }
    }
    @RequestMapping("/target/deleteTarget")
    public ServerResponse<Boolean> deleteTarget(String targetUid){
        try{
            targetService.deleteTarget(targetUid);
            log.info("[Success]删除成功");
            return ServerResponse.createBySuccessMsg("删除成功");
        }catch (Exception e){
            log.info("[Error]服务器异常");
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
 //   @RequestMapping("/target/deleteDailyTarget")
  //  public ServerResponse<Boolean> deleteDailyTarget()
    @RequestMapping("/target/getTargetList")
    public ServerResponse<List<target>> getTargetList(String uid){
          try{
              List<target> targets=targetService.getTargetList(uid);
              log.info("[Success]目标查询成功");
              return ServerResponse.createBySuccessMsgData("查询成功",targets);
          } catch (Exception e){
              log.info("[Error]服务器异常");
              return ServerResponse.createByErrorMsg("服务器异常");
          }
    }
    @RequestMapping("/target/getDailyTargetList")
    public ServerResponse<List<Object>> getDailyTargetList(String uid,String day){
        try {
            List<Object> targets=redisUtils.getAllList(uid+"target"+day);
            return ServerResponse.createBySuccessMsgData("查询成功",targets);
        }catch (Exception e){
            log.info("[Error]服务器异常");
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
    @RequestMapping("/target/isTargetInRange")
    public ServerResponse<List<Object>> isTargetInRange(String uid,String day){
        List<target> targetList = targetService.getTargetList(uid);
        List<Object> res=new ArrayList<>();
        for(target i:targetList){
            String[] began=dateUtils.dateSplit(i.getBeganTime()).split("-");
            String[] end=dateUtils.dateSplit(i.getEndTime()).split("-");
            String[] today=day.split("-");
            int beganMonth=Integer.parseInt(began[1]);
            int beganDay=Integer.parseInt(began[2]);
            int todayMonth=Integer.parseInt(today[1]);
            int todayDay=Integer.parseInt(today[2]);
            int endMonth=Integer.parseInt(end[1]);
            int endDay=Integer.parseInt(end[2]);
            if( beganMonth<todayMonth || (beganMonth==todayMonth && beganDay<=todayDay) )
                if(endMonth>todayMonth || (endMonth==todayMonth && endDay>=todayDay))
                    res.add(i);
        }
        return ServerResponse.createBySuccessMsgData("查询成功",res);
    }
    @RequestMapping("/target/changeIsDone")
    public ServerResponse<Boolean> changeIsDone(String targetUid){
        target target = targetService.queryTargetByTargetUid(targetUid);
        if(target.isDone()==true) targetService.updateIsDoneFalseByTargetUid(targetUid);
        else targetService.updateIsDoneTrueByTargetUid(targetUid);
        return ServerResponse.createBySuccessMsg("修改成功");
    }
    @RequestMapping("/target/changeDailyIsDone")
    public ServerResponse<Boolean> changeDailyIsDone(String uid,String targetUid,String day){
        List<Object> targets=redisUtils.getAllList(uid+"target"+day);
        Collections.reverse(targets);
        log.info("[Success]列表取出并且反转成功");
        redisUtils.lRemove(uid+"target"+day);
        for(Object i: targets){
            target j=(target)i;
            if(j.getTargetUid().equals(targetUid)){
                if(j.isDone()==true) j.setDone(false);
                else j.setDone(true);
                redisUtils.setList(uid+"target"+day,j);
                continue;
            }
            redisUtils.setList(uid+"target"+day,i);
        }


        return ServerResponse.createBySuccessMsg("修改成功");
    }
}
