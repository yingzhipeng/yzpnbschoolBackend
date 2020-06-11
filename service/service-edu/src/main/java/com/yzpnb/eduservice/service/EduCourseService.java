package com.yzpnb.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yzpnb.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yzpnb.eduservice.entity.vo.CourseAllInfoVo;
import com.yzpnb.eduservice.entity.vo.CourseApiInfoVo;
import com.yzpnb.eduservice.entity.vo.CourseApiVo;
import com.yzpnb.eduservice.entity.vo.CourseInfoVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-05-24
 */
public interface EduCourseService extends IService<EduCourse> {

    /**
     *添加课程基本信息
     * @return
     */
    String insertCourseInfo(CourseInfoVo courseInfoVo);
    /**
     * 根据课程id查询课程信息
     */
    CourseInfoVo selectCourseInfo(String id);

    /**
     * 分页查询课程信息
     *
     * @param current
     * @param size
     * @return
     */
    List<CourseAllInfoVo> limitSelectCourseAllInfoVo(Long current, Long size);
    /**
     * 根据id修改课程信息
     */
    void updateCourseInfoVo(CourseInfoVo courseInfoVo);
    /**
     * 根据id查询出指定课程id的课程id，课程标题，课程价格，总课时，课程封面，课程讲师，课程简介，课程类别一级和二级
     */
    CourseAllInfoVo selectCourseAllInfoVo(String id);
    /**
     *
     * 修改指定id的发布状态,如果是未发布Draft就改为Normal已发布，如果是已发布Normal，就改为Draft未发布
     */
    void updateStatus(String id);


    /**
     * 条件分页查询
     * @param pageCourse
     * @param courseApiVo
     * @return
     */
    Map<String, Object> selectIfLimitCourse(Page<EduCourse> pageCourse, CourseApiVo courseApiVo);

    /**
     * 根据id获取课程详细信息，包括讲师,并更新浏览量
     * @param id
     * @return
     */
    CourseApiInfoVo selectCourserApiInfoVoById(String id);
}
