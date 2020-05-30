package com.yzpnb.eduservice.service.impl;

import com.yzpnb.eduservice.entity.EduVideo;
import com.yzpnb.eduservice.mapper.EduVideoMapper;
import com.yzpnb.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-24
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    EduVideoMapper eduVideoMapper;
    /**
     * 根据课程id获取所有小节的视频id
     * @param id
     * @return
     */
    @Override
    public List<String> getVideoSourceIdList(String id) {
        return eduVideoMapper.getVideoSourceIdList(id);
    }
}
