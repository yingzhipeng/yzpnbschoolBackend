package com.yzpnb.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yzpnb.eduservice.entity.EduSubject;
import com.yzpnb.eduservice.entity.excel.SubjectExcel;
import com.yzpnb.eduservice.entity.subject.OneSubject;
import com.yzpnb.eduservice.entity.subject.TwoSubject;
import com.yzpnb.eduservice.listener.SubjectExcelListener;
import com.yzpnb.eduservice.mapper.EduSubjectMapper;
import com.yzpnb.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-14
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
    /**查询所有课程，返回树形结构*/
    @Override
    public List<OneSubject> selectAllOneSubject() {
        //1、查询所有一级分类 parent_id 等于0
        QueryWrapper<EduSubject> queryOneWrapper=new QueryWrapper<>();
        queryOneWrapper.eq("parent_id","0");//eq表示=
        List<EduSubject> oneSubjects = baseMapper.selectList(queryOneWrapper);//baseMapper 是我们的Mapper继承的接口封装好的对象
        //2、查询所有二级分类 parent_id 不等于0
        QueryWrapper<EduSubject> queryTwoWrapper=new QueryWrapper<>();
        queryTwoWrapper.ne("parent_id","0");//ne表示不等于
        List<EduSubject> twoSubjects = baseMapper.selectList(queryTwoWrapper);//baseMapper 是我们的Mapper继承的接口封装好的对象
        //3、封装一级分类
        List<OneSubject> list=new ArrayList<>();
        for (EduSubject eduOneSubject:oneSubjects) {//遍历所有一级分类
            /**1、创建一级分类对象，将所有一级分类集合中的每个对象的数据，赋值给一级分类对象*/
            OneSubject oneSubject=new OneSubject();
//            oneSubject.setId(eduSubject.getId());
//            oneSubject.setTitle(eduSubject.getTitle());
            BeanUtils.copyProperties(eduOneSubject,oneSubject);//这个是spring boot提供的工具类中的方法，封装了我上面注释的两条代码的实现逻辑
            /**2、遍历二级分类*/
            for (EduSubject eduTwoSubject:twoSubjects) {
                TwoSubject twoSubject=new TwoSubject();
                BeanUtils.copyProperties(eduTwoSubject,twoSubject);//将当前遍历对象的相应属性封装到二级分类对象中
                /**如果二级分类的parent_id等于当前一级分类的id就添加到此一级分类*/
                if(eduTwoSubject.getParentId().equals(eduOneSubject.getId())){//使用equals比较，因为==号比较的是内存地址，而我们要比的仅仅是字面值
                    oneSubject.getChildren().add(twoSubject);//将这个二级分类添加到当前一级目录的集合中
                }
            }
            /**3、将每个一级分类对象添加到集合中*/
            list.add(oneSubject);
        }
        return list;//将集合返回
    }

    /**根据excel表格添加课程分类*/
    @Override
    public void insertByExcel(MultipartFile file, EduSubjectService eduSubjectService) {

        try{
            EasyExcel.read(file.getInputStream(), SubjectExcel.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
