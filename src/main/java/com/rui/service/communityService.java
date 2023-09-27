package com.rui.service;
import com.rui.mapper.communityMapper;
import com.rui.pojo.community;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class communityService implements communityMapper{
    @Autowired
    communityMapper communityMapper;
    @Override
    public void addCommunity(community community) {
        communityMapper.addCommunity(community);
    }

    @Override
    public List<community> queryCommunityList() {
        return communityMapper.queryCommunityList();
    }

    @Override
    public void deleteCommuinty(String communityUid) {
        communityMapper.deleteCommuinty(communityUid);
    }

    @Override
    public void addPictureByCommunityUid(String communityUid, String pictureUrl) {
        communityMapper.addPictureByCommunityUid(communityUid,pictureUrl);
    }

    @Override
    public String queryCommunityUidByUidAndBeganTime(String uid, String beganTime) {
        return communityMapper.queryCommunityUidByUidAndBeganTime(uid,beganTime);
    }
}
