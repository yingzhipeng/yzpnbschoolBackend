package com.yzpnb.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yzpnb.utils.JwtUtils;
import com.yzpnb.common_utils.Result;
import com.yzpnb.eduservice.entity.EduComment;
import com.yzpnb.eduservice.entity.ucenter_member.UcenterMember;
import com.yzpnb.eduservice.feign.FeignToUcenterClient;
import com.yzpnb.eduservice.service.EduCommentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-06
 */
@RestController
@RequestMapping("/eduservice/edu-comment")
@CrossOrigin
public class EduCommentController {

    @Autowired
    private EduCommentService eduCommentService;

    @Autowired
    private FeignToUcenterClient feignToUcenterClient;

    //根据课程id查询评论列表
    @ApiOperation(value = "根据课程id分页查询评论")
    @GetMapping("limitSelectComment/{page}/{limit}")
    public Result limitSelectComment(@ApiParam(name = "page", value = "当前页码", required = true)
                                     @PathVariable Long page,
                                     @ApiParam(name = "limit", value = "每页记录数", required = true)
                                     @PathVariable Long limit,
                                     @ApiParam(name = "courseQuery", value = "课程id", required = false) String courseId) {
        Page<EduComment> pageParam = new Page<>(page, limit);

        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);

        eduCommentService.page(pageParam,wrapper);

        List<EduComment> commentList = pageParam.getRecords();

        Map<String, Object> map = new HashMap<>();

        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());

        return Result.ok().data(map);
    }

    @ApiOperation(value = "添加评论")
    @PostMapping("insertComment")
    public Result save(@RequestBody EduComment comment, HttpServletRequest request) {

        String memberId = JwtUtils.getMemberIdByJwtToken(request);//解析token字符串中的信息，获取id

        if(StringUtils.isEmpty(memberId)) {//如果没有值，让用户先登录
            return Result.error().code(28004).message("请登录");
        }

        comment.setMemberId(memberId);//将用户id添加

        UcenterMember ucenterMember = feignToUcenterClient.selectById(memberId);//调用feign接口，远程调用根据id获取用户信息

        //将用户昵称和头像添加
        comment.setNickname(ucenterMember.getNickname());
        comment.setAvatar(ucenterMember.getAvatar());

        eduCommentService.save(comment);//执行添加操作

        return Result.ok();
        }
}

