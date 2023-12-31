package com.rui.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.w3c.dom.ls.LSOutput;

import java.security.Key;
import java.util.Date;

public class TokenUtils {
    private final String tokenKey="cereshuzhitingnizhenbangcereshuzhitingnizhenbang";
    byte[] bytes=tokenKey.getBytes();
    private final Key key= Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenKey));
    private String Token(String userId, Date date) {
        System.out.println(bytes.length);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(date) // 设置签发时间
                .setExpiration(new Date(date.getTime() + 1000 * 60 * 60*24*14))
                .claim("userId",String.valueOf(userId) ) // 设置内容
                .signWith(key); // 签名，需要算法和key
        String jwt = builder.compact();
        return jwt;
    }
    public String getToken(String userId){
        Date date=new Date();
        int nowTime =(int) (date.getTime());
        System.out.println(nowTime);
        String TokenStr=Token(userId,date);
        return TokenStr;
    }
}
