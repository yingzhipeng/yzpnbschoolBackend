package com.yzpnb.eduservice.service.impl;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.yzpnb.eduservice.entity.EduCourse;
import com.yzpnb.eduservice.entity.EduCourseDescription;
import com.yzpnb.eduservice.entity.vo.CourseAllInfoVo;
import com.yzpnb.eduservice.entity.vo.CourseInfoVo;
import com.yzpnb.eduservice.mapper.EduCourseMapper;
import com.yzpnb.eduservice.service.EduCourseDescriptionService;
import com.yzpnb.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzpnb.service_base_handler.CustomExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-24
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    EduCourseDescriptionService eduCourseDescriptionService;//注入课程简介信息的添加接口，我们添加课程简介的时候需要使用

    @Autowired
    EduCourseMapper eduCourseMapper;
    /**
     * 根据课程id查询课程信息
     */
    @Override
    public CourseInfoVo selectCourseInfo(String id) {
        /**1、根据id查询课程基本信息*/
        EduCourse eduCourse=baseMapper.selectById(id);
        /**2、根据id查询课程简介*/
        EduCourseDescription eduCourseDescription =eduCourseDescriptionService.getById(id);
        /**3、将信息封装到信息汇总对象中*/
        CourseInfoVo courseInfoVo=new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
        courseInfoVo.setDescription(eduCourseDescription.getDescription());

        return courseInfoVo;
    }
    /**
     * 根据id查询出指定课程id的课程id，课程标题，课程价格，总课时，课程封面，课程讲师，课程简介，课程类别一级和二级
     */
    @Override
    public CourseAllInfoVo selectCourseAllInfoVo(String id) {
        return eduCourseMapper.selectCourseAllInfoVo(id);
    }

    /**
     * 分页查询课程信息
     * @param current 当前页
     * @param size  每页记录
     * @return  每页数据
     */
    @Override
    public List<CourseAllInfoVo> limitSelectCourseAllInfoVo(Long current, Long size) {
        //获取每页起始索引
        Long index=(current-1)*size;
        return eduCourseMapper.limitSelectCourseAllInfoVo(index,size);
    }
    /**
     *
     * 修改指定id的发布状态,如果是未发布Draft就改为Normal已发布，如果是已发布Normal，就改为Draft未发布
     */
    @Override
    public void updateStatus(String id) {
        eduCourseMapper.updateStatus(id);
    }


    /**
     * 根据id修改课程信息
     */
    @Override
    public void updateCourseInfoVo(CourseInfoVo courseInfoVo) {

        /**1、修改课程基本信息*/
        EduCourse eduCourse=new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int i=baseMapper.updateById(eduCourse);
        if(i==0){
            throw new CustomExceptionHandler(20001,"修改课程基本信息失败");
        }
        /**2、修改简介*/
        EduCourseDescription eduCourseDescription=new EduCourseDescription();
        eduCourseDescription.setId(courseInfoVo.getId());
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        boolean b=eduCourseDescriptionService.updateById(eduCourseDescription);
        if(!b){
            throw new CustomExceptionHandler(20001,"修改课程简介信息失败");
        }
    }

    /**
     *添加课程基本信息
     * @return
     */
    @Override
    public String insertCourseInfo(CourseInfoVo courseInfoVo) {
        /**1、课程表中添加课程基本信息*/
        //1.1将courseInfoVo中保存的所有信息中挑出课程基本信息添加到基本信息表
        EduCourse eduCourse=new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        //1.2调用baseMapper中添加方法将课程添加
        int index=baseMapper.insert(eduCourse);
        //1.3判断是否添加成功
        if(index<=0){
            throw new CustomExceptionHandler(20001,"添加课程信息失败");//抛出我们的自定义异常
        }
        /**2、课程简介表中添加课程简介*/
        //2.1将courseInfoVo中保存的信息中挑出简介信息添加到简介表中
        EduCourseDescription eduCourseDescription=new EduCourseDescription();
        //BeanUtils.copyProperties(eduCourseDescription,courseInfoVo);与下面的这行代码效果相同，但会自动生成id所以依然需要为其人为设置id
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        //**这两个表因为是1对1的关系，id值应该相同，将自动为Course表生成的id直接赋值给简介表**
        eduCourseDescription.setId(eduCourse.getId());
        //2.2调用接口添加信息
        eduCourseDescriptionService.save(eduCourseDescription);
        return eduCourse.getId();//返回id值
    }


}
