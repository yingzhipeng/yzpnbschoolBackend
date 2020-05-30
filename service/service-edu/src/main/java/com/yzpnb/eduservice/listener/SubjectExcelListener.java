package com.yzpnb.eduservice.listener;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yzpnb.eduservice.entity.EduSubject;
import com.yzpnb.eduservice.entity.excel.SubjectExcel;
import com.yzpnb.eduservice.service.EduSubjectService;
import com.yzpnb.service_base_handler.CustomExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class SubjectExcelListener extends AnalysisEventListener<SubjectExcel> {

    /**
     * 监听器不可以交给spirng管理，因为我们要通过new来使用它的实例，所以我们就不能自动注入我们需要的接口service
     * 所以添加有参构造手动获取service
     * 最后补充无参构造，保证监听器可以被new
     * */
    private EduSubjectService eduSubjectService;//service接口对象

    public SubjectExcelListener() {//无参构造
    }

    public SubjectExcelListener(EduSubjectService eduSubjectService) {//有参构造
        this.eduSubjectService = eduSubjectService;
    }

    @Autowired
    EduSubject eduSubject;//放一个公用对象，免得一直new来new去

    @Override//一行一行读
    public void invoke(SubjectExcel subjectExcel, AnalysisContext analysisContext) {

        if(subjectExcel == null){//如果文件没有内容，这实例为null
            throw new CustomExceptionHandler(20001,"文件内容为空");
        }

        /**添加一级分类 我们需要它添加完的id值*/
        eduSubject=this.existsOneSubject(subjectExcel.getOneSubjectName());//将方法返回值赋值给obj
        if(eduSubject==null){//调用当前（this表示当前类对象）类方法,如果为null表示没有重复数据
            eduSubject=new EduSubject();//一定要新建对象
            eduSubject.setParentId("0");//所有一级分类的parent_id都是0
            eduSubject.setTitle(subjectExcel.getOneSubjectName());//将当前从excel读取的一行内容中，去第一列的值也就是一级目录名字给title
            eduSubjectService.save(eduSubject);//添加操作，完成后，信息都会自动保存到对象中
        }

        /**
         * 走到这里说明已经成功添加了一级分类目录或者查出了数据库已经存在的对象
         * 那么现在eduSubject对象，存储的就是当前一级分类对象，通过get方法即可获取我们想要的信息
         * 而我们二级分类的parent_id是一级父分类的id，所有我们获取这个一级分类的id
         */
        String id=eduSubject.getId();//获取id值,

        /**添加二级分类*/
        if(this.existsTwoSubject(subjectExcel.getTwoSubjectName(),id)==null){
            eduSubject=new EduSubject();//一定要new对象，否则id等值还是一级分类的
            eduSubject.setParentId(id);//将刚刚存储的一级分类的id值给parent_id
            eduSubject.setTitle(subjectExcel.getTwoSubjectName());
            eduSubjectService.save(eduSubject);
        }
    }

    //判断一级分类不能重复添加
    private EduSubject existsOneSubject(String name){
        QueryWrapper<EduSubject> queryWrapper=new QueryWrapper<>();
        //根据当前课程名称查询，如果查出来，表示数据库有重复数据
        queryWrapper.eq("title",name).eq("parent_id","0");
        //根据条件获取数据并返回
        return eduSubjectService.getOne(queryWrapper);
    }
    //判断二级分类不能重复添加
    private EduSubject existsTwoSubject(String name,String parentId){
        QueryWrapper<EduSubject> queryWrapper=new QueryWrapper<>();
        //根据当前课程名称和id（二级分类每个课程id不同）查询
        queryWrapper.eq("title",name).eq("parent_id",parentId);
        //根据条件获取数据并返回
        return eduSubjectService.getOne(queryWrapper);
    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
