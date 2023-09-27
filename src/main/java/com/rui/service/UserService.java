package com.rui.service;

import com.rui.mapper.UserMapper;
import com.rui.pojo.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserMapper{
    @Autowired
    UserMapper userMapper;

    @Override
    public String queryPasswordByUid(String Uid) {
        return userMapper.queryPasswordByUid(Uid);
    }

    @Override
    public void addUser(user user) {
         userMapper.addUser(user);
    }

    @Override
    public String queryPasswordByPhone(String phone) {
        return userMapper.queryPasswordByPhone(phone);
    }

    public String queryUidByPhone(String Phone)
    {
        return userMapper.queryUidByPhone(Phone);
    }

    @Override
    public String queryPhoneByUid(String uid) {
        return userMapper.queryPhoneByUid(uid);
    }

    @Override
    public int addHeadUrl(String uid, String headUrl) {
        return userMapper.addHeadUrl(uid,headUrl);
    }

    @Override
    public String queryHeadUrlByUid(String uid) {
        return userMapper.queryHeadUrlByUid(uid);
    }

    @Override
    public user queryUserByPhone(String phone) {
        return userMapper.queryUserByPhone(phone);
    }

    @Override
    public int updateUserInfo(user user) {
        return userMapper.updateUserInfo(user);
    }

    @Override
    public int updateUserPhone(String uid, String newPhone) {
        return userMapper.updateUserPhone(uid,newPhone);
    }

    @Override
    public void deleteUserByUid(String uid) {
        userMapper.deleteUserByUid(uid);
    }
}
