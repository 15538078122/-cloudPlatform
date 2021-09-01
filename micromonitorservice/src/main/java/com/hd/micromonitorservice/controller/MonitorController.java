package com.hd.micromonitorservice.controller;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.controller.SuperQueryController;
import com.hd.common.model.RequiresPermissions;
import com.hd.micromonitorservice.service.SyMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
