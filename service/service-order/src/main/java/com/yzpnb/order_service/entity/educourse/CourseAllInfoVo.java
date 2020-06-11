package com.yzpnb.order_service.entity.educourse;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class CourseAllInfoVo {

    @ApiModelProperty(value = "课程id")
    private String id;

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "课程价格")
    private BigDecimal price;

    @ApiModelProperty(value = "总课时")
    private Integer lessonNum;

    @ApiModelProperty(value = "课程封面")
    private String cover;

    @ApiModelProperty(value = "课程发布状态")
    private String status;

    @ApiModelProperty(value = "讲师名")
    private String teacherName;

    @ApiModelProperty(value = "课程简介")
    private String description;

    @ApiModelProperty("一级分类")
    private String oneTitle;

    @ApiModelProperty("二级分类")
    private String twoTitle;
}
