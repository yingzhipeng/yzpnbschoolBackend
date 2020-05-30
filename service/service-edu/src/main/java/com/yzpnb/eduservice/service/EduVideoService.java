package com.yzpnb.eduservice.service;

import com.yzpnb.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-05-24
 */
public interface EduVideoService extends IService<EduVideo> {

    /**
     * 根据课程id获取所有小节的视频id
     * @param id
     * @return
     */
    List<String> getVideoSourceIdList(String id);
}
