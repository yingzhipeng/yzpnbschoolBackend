package com.yzpnb.eduservice.entity.subject;

        import io.swagger.annotations.ApiModelProperty;
        import lombok.Data;

/**
 * 二级分类
 */
@Data
public class TwoSubject {
    @ApiModelProperty("二级分类id")
    private String id;
    @ApiModelProperty("二级课程名")
    private String title;
}
