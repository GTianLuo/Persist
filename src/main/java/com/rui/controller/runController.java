package com.rui.controller;

import com.rui.common.ServerResponse;
import com.rui.service.runService;
import com.rui.util.DateUtils;
import com.rui.util.RedisUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rui.pojo.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@Log4j2
public class runController {
   @Autowired
    runService runService;
   @Autowired
    RedisUtils redisUtils;
   @Autowired
    DateUtils dateUtils;
    /*
    * * 上传跑步记录
     */
    @RequestMapping("/run/addRunRecord")
    public ServerResponse<Boolean> addRunRecord(runRecord runRecord){
        try {
            runService.addRunRecord(runRecord);
            String uid = runRecord.getUid();
            String runSumDistance = runService.queryRunSumDistanceByUid(uid);
            if (runSumDistance == null) {
                String distance = runRecord.getDistance();
                runService.addRunSumDistance(uid, distance);
                //  String runSumDistanceUid=uid+"runSumDistance";
                //  redisUtils.setList(runSumDistanceUid,runRecord);
                //  String runRecordUid=uid+"runRecord";
                // redisUtils.setList(uid,runRecord);

            } else {
                String distance = runRecord.getDistance();
                float num1 = Float.parseFloat(distance);
                float num2 = Float.parseFloat(runSumDistance);
                float finalNum = num1 + num2;
                DecimalFormat df = new DecimalFormat("0.00");
                runService.updateRunSumDistanceByUid(uid, df.format(finalNum));
            }
            String runWhen = runRecord.getRunWhen();
            String day = dateUtils.dateSplit(runWhen);

            String runRecordDayUid = uid + day + "runRecord";
            String runSumDistanceUid = uid + day + "runSumDistance";
            String runSumTimeUid = uid + day + "runTime";
            runRecordReturn runRecordReturn = new runRecordReturn(runRecord.getRunWhen(), runRecord.getRunTime(), runRecord.getDistance());
            redisUtils.setList(runRecordDayUid, runRecordReturn);
            String redisSumDistance = redisUtils.get(runSumDistanceUid);
            if (redisSumDistance == null) {
                redisUtils.set(runSumDistanceUid, runRecord.getDistance());
            } else {
                float num1 = Float.parseFloat(redisSumDistance);
                float num2 = Float.parseFloat(runRecord.getDistance());
                float finalNum = num1 + num2;
                DecimalFormat df = new DecimalFormat("0.00");
                redisUtils.set(runSumDistanceUid, df.format(finalNum));
            }
            String redisRunTime = redisUtils.get(runSumTimeUid);
            if (redisRunTime == null) {
                redisUtils.set(runSumTimeUid, runRecord.getRunTime());
            } else {
                float num1 = Float.parseFloat(redisRunTime);
                float num2 = Float.parseFloat(runRecord.getRunTime());
                float finalNum = num1 + num2;
                DecimalFormat df = new DecimalFormat("0.0");
                redisUtils.set(runSumTimeUid, df.format(finalNum));
            }
            log.info("[Success]添加成功");
            return ServerResponse.createBySuccessMsg("添加成功");
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
    /*
    * * 查询跑步记录
     */
    @RequestMapping("/run/queryRunRecordsByUid")
    public ServerResponse<List<runRecordReturn>> queryRunRecordsByUid(String uid){
        try{
        List<runRecordReturn> runRecords = runService.queryRunRecordsByUid(uid);
        log.info("[Success]查询到对应的跑步列表");
        return ServerResponse.createBySuccessMsgData("查询成功",runRecords);
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
    /*
    * * 计算当日跑步总和
     */
    @RequestMapping("/run/sumDistance")
    public ServerResponse<Boolean> getSumDistances(String uid){
        try{
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> distances = runService.queryDistancesByDay(simpleDateFormat.format(date), uid);
        if(distances==null) return ServerResponse.createByErrorMsg("没有跑步记录");
        float f=0;
        for(String i:distances){
            f+=Float.parseFloat(i);
        }
        return ServerResponse.createBySuccessMsg(Float.toString(f));
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }//废弃接口
    /*
    * * 添加里程目标的接口
     */
    //每次添加记录更新总和 而不是每次查取总和
    /*
    * * 更新首页的跑步目标功能
     */
    @RequestMapping("/run/addSportTarget")
    public ServerResponse<Boolean> addTarget(String uid,String target){
        try{
          String runTarget=runService.queryTargetByUid(uid);
          if(runTarget==null){
              runService.addTarget(uid,target);
              log.info("[Success]首页跑步目标添加成功");
          } else{
              runService.updateTarget(uid,target);
              log.info("[Success]首页跑步目标更新成功");
          }
          return ServerResponse.createBySuccessMsg("添加目标成功");
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
    /*
    * * 更新每日跑步目标
     */
    @RequestMapping("/run/updatePlanTarget")
    public ServerResponse<Boolean> updatePlanTarget(String uid,String day,String target){
        try{
        String planTargetUid=uid+day+"runTarget";
        redisUtils.set(planTargetUid,target);
        log.info("[Success]"+day+"目标添加成功");
        return ServerResponse.createBySuccessMsg("目标更新成功");
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
    /*
    * * 获得某一天的跑步目标
     */
    @RequestMapping("/run/getPlanTarget")
    public ServerResponse<String> getPlanTarget(String uid,String day){
        try{
        String runTargetDayUid=uid+day+"runTarget";
        String runTarget=redisUtils.get(runTargetDayUid);
        if(runTarget==null) return ServerResponse.createBySuccessMsgData("未进行设置","30");
        else return ServerResponse.createBySuccessMsgData("已进行设置",runTarget);
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
    /*
    * * 查询某一天的跑步记录(Redis)
     */
    @RequestMapping("run/getPlanRunRecord")
    public ServerResponse<List<Object>> getPlanRunRecord(String uid, String day){
        try{
        String runRecordDayUid=uid+day+"runRecord";
        log.info("对应的uid"+runRecordDayUid);
        List<Object> res = redisUtils.getAllList(runRecordDayUid);
        log.info("[Success]解析到的跑步数据"+res.toString());
        return ServerResponse.createBySuccessMsgData("查询成功",res);
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
    /*
    * * 得到某天跑步的总和 并且我会永远爱我的舍友，我是420男同
     */
    @RequestMapping("run/getPlanDaySumDistance")
    public ServerResponse<String> getPlanDaySumDistance(String uid,String day){
        try{
        String runSumDistanceUid=uid+day+"runSumDistance";
        String runSumDistance=redisUtils.get(runSumDistanceUid);
        if(runSumDistance==null){
            log.info("[Success]查询跑步总和");
            return ServerResponse.createBySuccessMsgData("查询成功","0");
        }
        else {
            log.info("[Success]查询跑步总和");
            return ServerResponse.createBySuccessMsgData("查询成功",runSumDistance);
        }
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
    /*
    * * 查询跑步目标（数据库）
     */
    @RequestMapping("run/getSportTarget")
    public ServerResponse<String> getTarget(String uid){
        try{
       String runTarget=runService.queryTargetByUid(uid);
       if(runTarget==null) {
           log.info("[Success]目标未进行设置");
           return ServerResponse.createByErrorMsg("未设置目标");
       }
       else{
           log.info("[Success]总目标查询成功");
           return ServerResponse.createBySuccessMsgData("查询成功",runTarget);
       }
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
    /*
    * * 获取跑步总里程
0    */
    @RequestMapping("/run/getRunSumDistance")
    public ServerResponse<String> getRunSumDistance(String uid){
        try{
            log.info("开始查询跑步总里程");
        String runSumDistance = runService.queryRunSumDistanceByUid(uid);
        log.info("[Success]查询成功");
        if(runSumDistance==null)
            return ServerResponse.createBySuccessMsgData("查询成功","0");
        else
            return ServerResponse.createBySuccessMsgData("查询成功",runSumDistance);
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }
    /*
    * * 获取某一天的跑步总时间
     */
    @RequestMapping("/run/getPlanDaySumTime")
    public ServerResponse<String> getPlanDaySumTime(String uid,String day){
        try{
            log.info("开始查询"+day+"跑步总时间");
        String runSumTimeUid=uid+day+"runTime";
        String runSumTime=redisUtils.get(runSumTimeUid);
        if(runSumTime==null)
            return ServerResponse.createBySuccessMsgData("查询成功","0");
        else
            return ServerResponse.createBySuccessMsgData("查询成功",runSumTime);
        }catch (Exception e){
            log.info("服务器异常！");
            System.out.println(e);
            return ServerResponse.createByErrorMsg("服务器异常");
        }
    }

}
