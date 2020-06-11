package com.yzpnb.cmsservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yzpnb.cmsservice.entity.CrmBanner;
import com.yzpnb.cmsservice.service.CrmBannerService;
import com.yzpnb.common_utils.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController//以Rest风格注入前端控制器
@RequestMapping(value="/cmsservice/crm-banner/after-end/")//请求映射
@CrossOrigin
public class CrmBannerController {

    @Autowired
    private CrmBannerService crmBannerService;

    /**
     * 查询
     */

    @ApiOperation(value = "根据id获取Banner")
    @GetMapping("selectById/{id}")
    public Result selectById(@ApiParam(name = "id",value = "BannerID")
                            @PathVariable String id) {
        CrmBanner banner = crmBannerService.getById(id);
        return Result.ok().data("banner", banner);
    }

    @ApiOperation(value = "获取Banner分页列表")
    @GetMapping("limit/{page}/{limit}")
    public Result index(@ApiParam(name = "page", value = "当前页码", required = true)
                       @PathVariable Long page,
                        @ApiParam(name = "limit", value = "每页记录数", required = true)
                       @PathVariable Long limit) {
        Page<CrmBanner> pageParam = new Page<>(page, limit);
        crmBannerService.page(pageParam);
        return Result.ok().data("banners", pageParam.getRecords());
    }

    /**
     * 修改
     */
    @ApiOperation(value = "根据id修改Banner")
    @PutMapping("updateById")
    public Result updateById(@RequestBody CrmBanner banner) {
        crmBannerService.updateById(banner);
        return Result.ok();
    }

    /**
     * 添加
     */
    @ApiOperation(value = "新增Banner")
    @PostMapping("insert")
    public Result insert(@RequestBody CrmBanner banner) {
        crmBannerService.save(banner);
        return Result.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "根据id删除Banner")
    @DeleteMapping("deleteById/{id}")
    public Result deleteById(@PathVariable String id) {
        crmBannerService.removeById(id);
        return Result.ok();
    }
}
