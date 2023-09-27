package com.rui.mapper;
import com.rui.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper
public interface communityMapper {
   void addCommunity(community community);
   List<community> queryCommunityList();
   void deleteCommuinty(String communityUid);
   //void addContentByCommunityUid(String communityUid,String content);
   void addPictureByCommunityUid(String communityUid,String pictureUrl);
   String queryCommunityUidByUidAndBeganTime(String uid,String beganTime);
}
