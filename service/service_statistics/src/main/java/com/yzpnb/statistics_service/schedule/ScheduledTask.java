package com.yzpnb.statistics_service.schedule;

import com.yzpnb.statistics_service.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    @Scheduled(cron = "0 0 1 * * ?")//每日凌晨一点执行
    public void task1(){
        //获取前一天的时间
        Calendar now =Calendar.getInstance();//反射对象
        now.setTime(new Date());             //设置时间为当前时间
        now.set(Calendar.DATE,(now.get(Calendar.DATE))-1);//设置DATE的值为当前DATE-1，也就是当前时间的前一天了
        Date time = now.getTime();//获取时间对象
        //格式化时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String day = sdf.format(time);//将前一天的时间格式化为字符串

        //统计数据
        statisticsDailyService.createStatisticsByDay(day);
    }
}
