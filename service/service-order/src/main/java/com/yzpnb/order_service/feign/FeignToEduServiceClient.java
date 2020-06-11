package com.yzpnb.order_service.feign;

import com.yzpnb.order_service.entity.educourse.CourseAllInfoVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-dev")
@Component
public interface FeignToEduServiceClient {

    @ApiOperation("根据课程id获取课程信息")
    @GetMapping("/eduservice/edu-course/selectCourseAllInfoVoApiById")
    public CourseAllInfoVo selectCourseAllInfoVoApiById(@ApiParam(name = "id",value = "课程id")
                                                        @RequestParam(value = "id") String id);
}
