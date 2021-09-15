package com.hd.micromonitorservice.controller;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.controller.SuperQueryController;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.vo.SyMonitorVo;
import com.hd.common.vo.UriCostVo;
import com.hd.micromonitorservice.service.SyMonitorService;
import com.hd.micromonitorservice.service.UriCostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liwei
 */
@Api(tags = "服务监视Controller")
@RestController
@Slf4j
public class MonitorController extends SuperQueryController {

    @Autowired
    SyMonitorService syMonitorService;

    public MonitorController() {
        //mapQueryCols.put("name", "name");
    }

    @ApiOperation(value = "心跳")
    @RequiresPermissions(value = "heartbeat:up", note = "心跳")
    @GetMapping("/heartbeat/{serviceName}")
    public RetResult heartbeatUp(@PathVariable("serviceName") String serviceName) {
         syMonitorService.heartbeat(serviceName);
        return RetResponse.makeRsp("心跳成功");
    }
    @ApiOperation(value = "心跳列表")
    @RequiresPermissions(value = "heartbeat:list", note = "心跳列表")
    @GetMapping("/heartbeat")
    public RetResult heartbeatList() {
        return RetResponse.makeRsp(syMonitorService.listServiceHeartbeat());
    }
    @Autowired
    UriCostService uriCostService;

    @ApiOperation(value = "最近请求列表")
    @RequiresPermissions(value = "lastreq:list", note = "最近请求列表")
    @GetMapping("/lastreq")
    public RetResult lastReq(int pageNum,int pageSize) {
        return RetResponse.makeRsp(uriCostService.getMaxCost2Sec(pageNum,pageSize));
    }
    @ApiOperation(value = "最近请求平均耗时列表")
    @RequiresPermissions(value = "lastreqavg:list", note = "最近请求平均耗时列表")
    @GetMapping("/lastreqavg")
    public RetResult lastReqAvg(int pageNum,int pageSize) {
        return RetResponse.makeRsp(uriCostService.getAverageCost2Sec(pageNum,pageSize));
    }
    @ApiOperation(value = "swagger 展示model使用")
    @GetMapping("/nouse/showmodels")
    public RetResult nouseshowmodels(@RequestBody  SyMonitorVo syMonitorVo, @RequestBody UriCostVo uriCostVo) {
        return RetResponse.makeRsp("");
    }

}
