package com.yzpnb.statistics_service.service;

import com.yzpnb.statistics_service.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-08
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    /**
     * 根据日期创建统计对象
     * @param day
     */
    void createStatisticsByDay(String day);

    /**
     * 根据起始日期结束日期和查询数据类型得到统计数据
     * @param begin
     * @param end
     * @param type
     * @return
     */
    Map<String, Object> getChartData(String begin, String end, String type);
}
