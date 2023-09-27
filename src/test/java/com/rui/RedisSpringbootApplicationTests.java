package com.rui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rui.common.ConstantProperties;
import com.rui.util.AliyunOSSUtils;
import com.rui.util.CodeUtils;
import com.rui.util.DateUtils;
import com.rui.util.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@SpringBootTest
class RedisSpringbootApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    ConstantProperties constantProperties;
    @Autowired
    AliyunOSSUtils aliyunOSSUtils;
    @Autowired
    DataSource dataSource;

    @Test
    void contextLoads() throws JsonProcessingException {
        //   System.out.println("Hello");
        //   Date date=new Date();
        // System.out.println(date.getTime());
        //System.out.println(new CodeUtils().SendCode("+8613181003288"));
      /*  File file=new File("C:\\Users\\XiAoRayL\\Desktop\\2.png");
       String url= aliyunOSSUtils.upload(file);
        System.out.println(url);*/
        //System.out.println(file.getName());
        //if(file==null) System.out.println("NULL!");
        //  new AliyunOSSUtils().upload(file);
       /* SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String[] date2 = simpleDateFormat.format(date).split("\\s");
        System.out.println(Arrays.toString(date2));*/
        //  System.out.println(dataSource.getClass());
  /*      Date date = new Date();
        String a = new DateUtils().dateSplit("2022-4-11 12:00:00");
        System.out.println(a);*/
      //  redisUtils.set("targetUid","1");
      //  redisUtils.lRemove("test");
        CodeUtils.SendCode("13181003288");
    }
   /* @Autowired
    private UserMapper userMapper;
    user user=new user("132","123","123",123,"12","@qq.com","abc");
    @Test
    void contextLoads() throws JsonProcessingException {
      userMapper.addUser(user);
    }*/


}

