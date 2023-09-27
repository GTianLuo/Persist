package com.rui.service;
import com.rui.mapper.targetMapper;
import com.rui.pojo.target;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class targetService implements targetMapper {
@Autowired
targetMapper targetMapper;
    @Override
    public void addTarget(target target) {
        targetMapper.addTarget(target);
    }

    @Override
    public void deleteTarget(String targetUid) {
      targetMapper.deleteTarget(targetUid);
    }

    @Override
    public List<target> getTargetList(String uid) {
        return targetMapper.getTargetList(uid);
    }

    @Override
    public target queryTargetByTargetUid(String targetUid) {
        return targetMapper.queryTargetByTargetUid(targetUid);
    }

    @Override
    public void updateIsDoneTrueByTargetUid(String targetUid) {
        targetMapper.updateIsDoneTrueByTargetUid(targetUid);
    }

    @Override
    public void updateIsDoneFalseByTargetUid(String targetUid) {
         targetMapper.updateIsDoneFalseByTargetUid(targetUid);
    }
}
