package com.yzpnb.cmsservice.controller.api;

import com.yzpnb.cmsservice.entity.CrmBanner;
import com.yzpnb.cmsservice.service.CrmBannerService;
import com.yzpnb.common_utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/cmsservice/crm-banner/api/")
@Api("网站首页Banner列表")
@CrossOrigin //跨域
public class CrmBannerApiController {

    @Autowired
    private CrmBannerService crmBannerService;

    @Cacheable(value="banners",key="'selectAllBanner'")//查询时，将结果放在缓存中
    @ApiOperation(value = "获取首页所有banner")
    @GetMapping("selectAllBanner")
    public Result selectAllBanner() {
        List<CrmBanner> list = crmBannerService.list();
        return Result.ok().data("bannerList", list);
    }

}
