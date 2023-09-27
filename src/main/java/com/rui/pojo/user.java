package com.rui.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class user {
    // tokenId
   // private Long id;
    // 用户账号
    private String phone;
    // 用户密码
    private String password;
    // 用户名
    private String userName;
    //刷新时间
   // private int buildTime;
    //token令牌
   // private String token;

    private Integer age;
    private String gender;
    private String email;
    private String uid;
    private String headUrl;
}
