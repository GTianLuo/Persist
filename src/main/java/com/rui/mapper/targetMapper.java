package com.rui.mapper;
import com.rui.pojo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface targetMapper {
      void addTarget(target target);
      void deleteTarget(String targetUid);
      List<target> getTargetList(String uid);
      target queryTargetByTargetUid(String targetUid);
      void updateIsDoneTrueByTargetUid(String targetUid);
      void updateIsDoneFalseByTargetUid(String targetUid);
}
