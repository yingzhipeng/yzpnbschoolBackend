package com.yzpnb.eduservice.entity.subject;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 一级分类
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OneSubject {
    @ApiModelProperty("一级分类id")
    private String id;
    @ApiModelProperty("一级课程名")
    private String title;
    @ApiModelProperty("多个二级课程包含再一个一级分类")
    private List<TwoSubject> children=new ArrayList<>();
}
