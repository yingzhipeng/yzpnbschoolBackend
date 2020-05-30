package com.yzpnb.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yzpnb.eduservice.entity.EduChapter;
import com.yzpnb.eduservice.entity.EduVideo;
import com.yzpnb.eduservice.entity.chapter.ChapterVo;
import com.yzpnb.eduservice.entity.chapter.VideoVo;
import com.yzpnb.eduservice.mapper.EduChapterMapper;
import com.yzpnb.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzpnb.eduservice.service.EduVideoService;
import com.yzpnb.service_base_handler.CustomExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;
    @Override
    public List<ChapterVo> selectChapterVideoByCourseId(String courseId) {

        /**1、根据课程id查询对应所有章节**/
        QueryWrapper<EduChapter> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        List<EduChapter> eduChapters = baseMapper.selectList(queryWrapper);

        /**2、根据课程id和当前查询的章节id查出对应小节，然后封装到章节中**/
        //2.1先查询出当前课程id对应的所有小节
        QueryWrapper<EduVideo> queryWrapper2=new QueryWrapper<>();
        queryWrapper2.eq("course_id",courseId);
        List<EduVideo> eduVideos=eduVideoService.list(queryWrapper2);
        //2.2遍历集合，判断小节的章节id（chapter_id）是否等于查询到的章节id进行封装

        List<ChapterVo> list=new ArrayList<>();//保存最终封装号的对象

        for(EduChapter eduChapter:eduChapters)//遍历所有章节（总）
        {
            ChapterVo chapterVo=new ChapterVo();//创建我们封装用的章节对象
            BeanUtils.copyProperties(eduChapter,chapterVo);//使用工具类将章节（总）对象中我们需要的信息封装到章节对象中

            for (EduVideo eduVideo :eduVideos){//遍历小节（总）对象

                if(eduVideo.getChapterId().equals(chapterVo.getId())){//判断当前小节(总)对象的章节id是否等于章节对象的id(一定用equals)
                    VideoVo videoVo =new VideoVo();//创建小节对象
                    BeanUtils.copyProperties(eduVideo,videoVo);//封装信息
                    chapterVo.getChildren().add(videoVo);//将此小节封装当此章节中
                }
                //if结束
            }
            //内循环结束，将对象封装
            list.add(chapterVo);
        }

        return list;
    }

    /**
     * 删除章节，如果有小节，则不能删除章节
     */
    @Override
    public void deleteChapterVideo(String id) {
        /**1、根据章节id查小节*/
        QueryWrapper<EduVideo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("chapter_id",id);
        int count = eduVideoService.count(queryWrapper);//返回当前查询结果的记录个数
        if(count>0){//如果不是0表示有小节
            throw new CustomExceptionHandler(20001,"章节中包含小节，请先删除小节");
        }else{
            baseMapper.deleteById(id);
        }

    }
}
