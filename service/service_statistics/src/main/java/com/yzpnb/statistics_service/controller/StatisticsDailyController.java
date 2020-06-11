package com.yzpnb.statistics_service.controller;


import com.yzpnb.common_utils.Result;
import com.yzpnb.statistics_service.service.StatisticsDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-08
 */
@RestController
@RequestMapping("/statistics_service/statistics-daily")
@CrossOrigin
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    @ApiOperation("根据日期创建统计对象")
    @PostMapping("{day}")
    public Result createStatisticsByDate(@PathVariable String day) {
        statisticsDailyService.createStatisticsByDay(day);
        return Result.ok();
    }

    @ApiOperation("根据起始日期结束日期和查询数据类型得到统计数据")
    @GetMapping("show-chart/{begin}/{end}/{type}")
    public Result showChart(@ApiParam(name = "begin",value = "起始时间")
                            @PathVariable String begin,
                            @ApiParam(name = "end",value = "结束时间")
                            @PathVariable String end,
                            @ApiParam(name = "type",value = "查询数据类型")
                            @PathVariable String type){

        Map<String, Object> map = statisticsDailyService.getChartData(begin, end, type);
        return Result.ok().data(map);
    }
}

