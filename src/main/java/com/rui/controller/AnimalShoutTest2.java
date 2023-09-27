package com.rui.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class AnimalShoutTest2 {
  public static void main(String[] args) throws ParseException {
    Scanner sc=new Scanner(System.in);
    String one = sc.nextLine();
    String tmp=sc.nextLine();
    String[] a=tmp.split(" ");
    System.out.println(Arrays.toString(a));
    String two=a[0];
    String three=a[1];
    three=three.trim();
    System.out.println(three);
    if(check(one)==false){
      System.out.println(one+"无效！");
    }else{
      SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
      Date date=sd.parse(one);
      if(bissextile(date.getYear())==true){
        System.out.println(one+"是闰年");
      }
      GregorianCalendar gc=(GregorianCalendar) Calendar.getInstance();
      gc.setTime(date);
      System.out.println(one+"是当年第"+gc.get(Calendar.DAY_OF_YEAR)+"天,当月第"+gc.get(Calendar.DAY_OF_MONTH)+"天,当周第"+gc.get(Calendar.DAY_OF_WEEK)+"天。");
    }
    if(check(two)==false){
      System.out.println(two+"或"+three+"中有不合法的日期");
    }
    else {
      if(check(three)==false){
        System.out.println(two+"或"+three+"中有不合法的日期");
      }else {
        String d1=two;
        String d2=three;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = simpleDateFormat.parse(d1);
        Date date2 = simpleDateFormat.parse(d2);
        int l = (int) (date2.getTime() - date1.getTime()) / (1000 * 24 * 60 * 60);
        if(l<0){
          System.out.println(two+"早于"+three+",不合法！");
        }else{
          GregorianCalendar gc1=(GregorianCalendar) Calendar.getInstance();
          gc1.setTime(date1);

          System.out.println(two+"与"+three+"之间相差"+l+"天,所在月份相差");
        }
      }
    }
  }
  static boolean check (String str) {
    SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");//括号内为日期格式，y代表年份，M代表年份中的月份（为避免与小时中的分钟数m冲突，此处用M），d代表月份中的天数
    try {
      sd.setLenient(false);//此处指定日期/时间解析是否不严格，在true是不严格，false时为严格
      sd.parse(str);//从给定字符串的开始解析文本，以生成一个日期
    }
    catch (Exception e) {
      return false;
    }
    return true;
  }
 static boolean bissextile(int year){  //创建boolean类型的方法
    if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0){  //平闰年判断算法
      return true;
    }
    else{
      return false;
    }
  }

}
