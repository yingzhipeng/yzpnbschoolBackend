package com.yzpnb.eduservice.mapper;

import com.yzpnb.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 课程视频 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-05-24
 */
public interface EduVideoMapper extends BaseMapper<EduVideo> {

    List<String> getVideoSourceIdList(String id);
}
