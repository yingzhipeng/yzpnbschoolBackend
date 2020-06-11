package com.yzpnb.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yzpnb.eduservice.entity.EduTeacher;
import com.yzpnb.eduservice.mapper.EduTeacherMapper;
import com.yzpnb.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-07
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    /**
     * 分页查询讲师，返回所有数据
     * @param pageTeacher
     * @return
     */
    @Override
    public Map<String, Object> limitSelectTeacher(Page<EduTeacher> pageTeacher) {
        QueryWrapper<EduTeacher> wrapper=new QueryWrapper<>();
        wrapper.orderByDesc("id");//根据id降序排列

        baseMapper.selectPage(pageTeacher, wrapper);

        Map<String,Object> map=new HashMap<>();
        map.put("allLimitTeacher", pageTeacher.getRecords());   //获取所有分页讲师
        map.put("current", pageTeacher.getCurrent());           //获取当前页
        map.put("pages", pageTeacher.getPages());               //页码
        map.put("size", pageTeacher.getSize());                 //获取每页记录数
        map.put("total", pageTeacher.getTotal());               //获取总记录
        map.put("hasNext", pageTeacher.hasNext());              //是否有下一页
        map.put("hasPrevious", pageTeacher.hasPrevious());      //是否有上一页

        return map;
    }
}
