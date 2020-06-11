package com.yzpnb.eduservice.entity.chapter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 小节实体类
 */
@Data
public class VideoVo {

    @ApiModelProperty("小节id")
    private String id;

    @ApiModelProperty("小节标题")
    private String title;

    @ApiModelProperty("小节视频id")
    private String videoSourceId;

    @ApiModelProperty("是否免费")
    private Boolean isFree;
}
