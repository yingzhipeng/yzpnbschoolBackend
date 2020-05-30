package com.yzpnb.eduservice.mapper;

import com.yzpnb.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yzpnb.eduservice.entity.vo.CourseAllInfoVo;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-05-24
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {


    /**
     * #查询出指定课程id的课程id，课程标题，课程价格，总课时，课程封面，课程讲师，课程简介，课程类别一级和二级
     */
    public CourseAllInfoVo selectCourseAllInfoVo(String id);
    /**
     *
     * 修改指定id的发布状态,如果是未发布Draft就改为Normal已发布，如果是已发布Normal，就改为Draft未发布
     */
    public void updateStatus(String id);

    /**
     * 分页查询课程信息
     * @param index 起始索引
     * @param size  每页记录数
     * @return
     */
    public List<CourseAllInfoVo> limitSelectCourseAllInfoVo(Long index, Long size);
}
