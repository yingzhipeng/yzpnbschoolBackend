package com.yzpnb.statistics_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yzpnb.statistics_service.entity.StatisticsDaily;
import com.yzpnb.statistics_service.feign.FeignToUcenterClient;
import com.yzpnb.statistics_service.mapper.StatisticsDailyMapper;
import com.yzpnb.statistics_service.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-08
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private FeignToUcenterClient feignToUcenterClient;//远程调用接口

    /**
     * 根据日期创建统计对象
     * @param day
     */
    @Override
    public void createStatisticsByDay(String day) {
        //删除已存在的统计对象（如果数据更新，需要先删除已经存在的数据，重新获取数据添加）
        QueryWrapper<StatisticsDaily> dayQueryWrapper = new QueryWrapper<>();
        dayQueryWrapper.eq("date_calculated", day);
        baseMapper.delete(dayQueryWrapper);


        //获取统计信息
        Integer registerNum =feignToUcenterClient.registerCount(day);//统计注册人数
        Integer loginNum = RandomUtils.nextInt(100, 200);//TODO 登陆人数
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//TODO 每日播放视频数
        Integer courseNum = RandomUtils.nextInt(100, 200);//TODO 每日新增课程数

        //创建统计对象
        StatisticsDaily daily = new StatisticsDaily();
        daily.setDateCalculated(day);       //设置统计日期
        daily.setRegisterNum(registerNum);  //设置注册人数
        daily.setLoginNum(loginNum);        //设置登陆人数
        daily.setVideoViewNum(videoViewNum);//设置每日播放视频数
        daily.setCourseNum(courseNum);      //设置每日新增课程数

        baseMapper.insert(daily);
    }

    /**
     * 根据起始日期结束日期和查询数据类型得到统计数据
     * @param begin
     * @param end
     * @param type
     * @return
     */
    @Override
    public Map<String, Object> getChartData(String begin, String end, String type) {

        QueryWrapper<StatisticsDaily> dayQueryWrapper = new QueryWrapper<>();
        dayQueryWrapper.select(type, "date_calculated");//指定要查询的字段
        dayQueryWrapper.between("date_calculated", begin, end);//指定查询的条件区间

        List<StatisticsDaily> dayList = baseMapper.selectList(dayQueryWrapper);

        Map<String, Object> map = new HashMap<>();
        List<Integer> dataList = new ArrayList<Integer>();//数据集合用来存储数据
        List<String> timeList = new ArrayList<String>();//日期集合，用来存储日期
        map.put("dataList", dataList);
        map.put("timeList", timeList);


        for (int i = 0; i < dayList.size(); i++) {//遍历查询出的结果

            StatisticsDaily daily = dayList.get(i);//取出集合中对象

            timeList.add(daily.getDateCalculated());//将对象的时间添加到日期集合中

            switch (type) {//看看需要统计的是哪种类型的数据
                case "register_num":    //统计注册人数
                    dataList.add(daily.getRegisterNum());
                    break;
                case "login_num":       //统计登陆人数
                    dataList.add(daily.getLoginNum());
                    break;
                case "video_view_num":  //统计视频播放人数
                    dataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":      //课程新增人数
                    dataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }

        return map;
    }
}
