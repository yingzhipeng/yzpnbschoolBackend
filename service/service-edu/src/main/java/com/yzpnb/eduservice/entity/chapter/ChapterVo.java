package com.yzpnb.eduservice.entity.chapter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 章节实体类
 */
@Data
public class ChapterVo {

    @ApiModelProperty("章节id")
    private String id;

    @ApiModelProperty("章节标题")
    private String title;

    @ApiModelProperty("每章节包含的小节,名称为children，前端需要对应")
    private List<VideoVo> children=new ArrayList<>();
}
