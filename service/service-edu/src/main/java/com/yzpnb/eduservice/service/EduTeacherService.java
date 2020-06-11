package com.yzpnb.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yzpnb.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-05-07
 */
public interface EduTeacherService extends IService<EduTeacher> {

    /**
     * 分页查询讲师，返回所有数据
     * @param pageTeacher
     * @return
     */
    Map<String, Object> limitSelectTeacher(Page<EduTeacher> pageTeacher);
}
