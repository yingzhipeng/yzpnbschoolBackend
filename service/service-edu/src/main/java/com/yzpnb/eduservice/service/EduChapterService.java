package com.yzpnb.eduservice.service;

import com.yzpnb.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yzpnb.eduservice.entity.chapter.ChapterVo;
import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-05-24
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> selectChapterVideoByCourseId(String courseId);

    void deleteChapterVideo(String id);
}
