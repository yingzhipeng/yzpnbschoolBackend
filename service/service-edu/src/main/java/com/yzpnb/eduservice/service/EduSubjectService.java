package com.yzpnb.eduservice.service;

import com.yzpnb.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yzpnb.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-05-14
 */
public interface EduSubjectService extends IService<EduSubject> {
    /**查询所有课程，返回树形结构*/
    List<OneSubject> selectAllOneSubject();

    /**根据excel表格添加课程分类*/
    void insertByExcel(MultipartFile file,EduSubjectService eduSubjectService);
}
