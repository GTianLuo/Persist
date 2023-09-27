package com.rui.service;

import com.rui.mapper.runMapper;
import com.rui.pojo.runRecord;
import com.rui.pojo.runRecordReturn;
import com.rui.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class runService implements runMapper {
    @Autowired
    runMapper runMapper;
    @Autowired
    DateUtils dateUtils;
    @Override
    public void addRunRecord(runRecord runRecord) {
        String day = dateUtils.dateSplit(runRecord.getRunWhen());
        runRecord.setDay(day);
        runMapper.addRunRecord(runRecord);
    }

    @Override
    public List<String> queryDistancesByDay(String day,String uid) {
        String dayNow =dateUtils.dateSplit(day);
        return runMapper.queryDistancesByDay(dayNow,uid);
    }

    @Override
    public List<runRecordReturn> queryRunRecordsByUid(String uid) {
        return runMapper.queryRunRecordsByUid(uid);
    }

    @Override
    public void addRunSumDistance(String uid, String runSumDistance) {
        runMapper.addRunSumDistance(uid,runSumDistance);
    }

    @Override
    public String queryRunSumDistanceByUid(String uid) {
       return runMapper.queryRunSumDistanceByUid(uid);
    }

    @Override
    public void updateRunSumDistanceByUid(String uid, String runSumDistance) {
        runMapper.updateRunSumDistanceByUid(uid,runSumDistance);
    }

    @Override
    public void addTarget(String uid, String target) {
        runMapper.addTarget(uid,target);
    }

    @Override
    public void updateTarget(String uid, String target) {
       runMapper.updateTarget(uid,target);
    }

    @Override
    public String queryTargetByUid(String uid) {
        return runMapper.queryTargetByUid(uid);
    }
}
