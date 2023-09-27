package com.rui.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.rui.pojo.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    public String queryPasswordByUid(String Uid);
    public void addUser(user user);
    public String queryPasswordByPhone(String phone);
    public String queryUidByPhone(String Phone);
    public String queryPhoneByUid(String uid);
    public int addHeadUrl(String uid,String headUrl);
    public String queryHeadUrlByUid(String uid);
    public user queryUserByPhone(String phone);
    public int updateUserInfo(user user);
    public int updateUserPhone(String uid,String newPhone);
    public void deleteUserByUid(String uid);
}
