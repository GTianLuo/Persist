package com.rui.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.rui.pojo.*;

import java.util.List;

@Mapper
public interface runMapper {
    public void addRunRecord(runRecord runRecord);
    public List<String> queryDistancesByDay(String day,String uid);
    public List<runRecordReturn> queryRunRecordsByUid(String uid);
    public void addRunSumDistance(String uid,String runSumDistance);
    public String queryRunSumDistanceByUid(String uid);
    public void updateRunSumDistanceByUid(String uid,String runSumDistance);
    public void addTarget(String uid,String target);
    public void updateTarget(String uid,String target);
    public String queryTargetByUid(String uid);
}
