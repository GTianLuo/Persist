package com.rui.util;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
@Service
public class DateUtils {
    public  boolean isSameDay(String runTime){
        String[] date1 = runTime.split("\\s");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String[] date2 = simpleDateFormat.format(date).split("\\s");

        return false;
    }
    /*
    * * 分割日期组合成几月几日
     */
    public  String dateSplit(String runTime){
        String[] date= runTime.split("\\s");
        String day=date[0];
        return day;
    }
}
