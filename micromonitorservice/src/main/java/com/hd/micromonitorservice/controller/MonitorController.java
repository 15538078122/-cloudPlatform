package com.hd.micromonitorservice.controller;

import com.hd.common.PageQueryExpressionList;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.controller.SuperQueryController;
import com.hd.common.model.QueryExpression;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.vo.OperatorVo;
import com.hd.common.vo.SyMonitorVo;
import com.hd.common.vo.UriCostVo;
import com.hd.micromonitorservice.service.SyMonitorService;
import com.hd.micromonitorservice.service.UriCostService;
import com.hd.micromonitorservice.utils.UserCommonUtil;
import com.hd.micromonitorservice.utils.VerifyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liwei
 */
@Api(tags = "服务监视Controller")
@RestController
@Slf4j
public class MonitorController extends SuperQueryController {

    @Autowired
    SyMonitorService syMonitorService;

    @Autowired
    UserCommonUtil userCommonUtil;

    public MonitorController() {
        //mapQueryCols.put("name", "name");
    }

    @ApiOperation(value = "心跳")
    @RequiresPermissions(value = "heartbeat:up", note = "心跳")
    @PostMapping("/heartbeat")
    public RetResult heartbeatUp(String serviceName, String clientId) {
        //String clientId="123";
        syMonitorService.heartbeat(serviceName, clientId);
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
    public RetResult lastReq(int pageNum, int pageSize) {
        return RetResponse.makeRsp(uriCostService.getMaxCost2Sec(pageNum, pageSize));
    }
    @ApiOperation(value = "获取在线用户数")
    @RequiresPermissions(value = "getOnLineUserCount:get", note = "获取在线用户数")
    @GetMapping("/getOnLineUserCount")
    public RetResult getOnLineUserCount() {
        return RetResponse.makeRsp(userCommonUtil.getOnLineUserCount());
    }
    @ApiOperation(value = "最近请求平均耗时列表")
    @RequiresPermissions(value = "lastreqavg:list", note = "最近请求平均耗时列表")
    @GetMapping("/lastreqavg")
    public RetResult lastReqAvg(int pageNum, int pageSize) {
        return RetResponse.makeRsp(uriCostService.getAverageCost2Sec(pageNum, pageSize));
    }

    @ApiOperation(value = "最近操作列表")
    @RequiresPermissions(value = "lastoperator:list", note = "最近操作列表")
    @PostMapping("/lastoperator/list")
    public RetResult lastOperator(@RequestParam("query") String query) {
        PageQueryExpressionList pageQueryExpressionList = VerifyUtil.verifyQueryParam(query, null, "");
        adaptiveQueryColumn(pageQueryExpressionList);
        int pageNum = pageQueryExpressionList.getPageNum();
        int pageSize = pageQueryExpressionList.getPageSize();
        //long lastMinutes = Long.parseLong(pageQueryExpressionList.getQueryExpressionByColumn("lastMinutes").getValue());
        QueryExpression enterpriseId1 = pageQueryExpressionList.getQueryExpressionByColumn("enterpriseId");
        String enterpriseId = enterpriseId1==null?null:enterpriseId1.getValue();
        enterpriseId1 = pageQueryExpressionList.getQueryExpressionByColumn("sTime");
        String sTime=enterpriseId1==null?null:enterpriseId1.getValue();
        enterpriseId1 = pageQueryExpressionList.getQueryExpressionByColumn("eTime");
        String eTime=enterpriseId1==null?null:enterpriseId1.getValue();
        return RetResponse.makeRsp(uriCostService.getOperators(pageNum, pageSize, sTime,eTime,enterpriseId));
    }

    @ApiOperation(value = "swagger 展示model使用")
    @GetMapping("/nouse/showmodels")
    public RetResult nouseshowmodels(@RequestBody SyMonitorVo syMonitorVo, @RequestBody UriCostVo uriCostVo
            , @RequestBody OperatorVo operatorVo) {
        return RetResponse.makeRsp("");
    }

}
