package com.hd.microsysservice.controller;

import com.hd.common.RetCode;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.controller.SuperQueryController;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.vo.SyAttachVo;
import com.hd.microsysservice.service.SyAttachService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liwei
 */
@Api(tags = "文档Controller")
@RestController
@Slf4j
public class AttachController extends SuperQueryController {

    @Autowired
    SyAttachService syAttachService;

    public AttachController() {
        //mapQueryCols.put("name", "name");
    }
    @ApiOperation(value = "上传单个文件")
    @RequiresPermissions(value = "attach:uploadone", note = "上传单个文件")
    @PostMapping("/attach/uploadone")
    public RetResult attachUploadOne(HttpServletRequest request, HttpServletResponse response, @Validated SyAttachVo syAttachVo, MultipartFile file) {
        try {

            Long id=syAttachService.attachUploadOne(request, response, syAttachVo, file);
            if(id==-1){
                return RetResponse.makeRsp(RetCode.FAIL.code,"上传失败!");
            }
            return RetResponse.makeRsp("上传成功",id.toString());
        } catch (Exception e) {
            return RetResponse.makeErrRsp("上传失败:"+e.getMessage());
        }
    }

    @ApiOperation(value = "上传分片")
    @RequiresPermissions(value = "attach:upload", note = "上传分片")
    @PostMapping("/attach/upload")
    public RetResult attachUpload(HttpServletRequest request, HttpServletResponse response, String guid, Integer chunk, MultipartFile file) {
        try {
            syAttachService.attachUpload(request, response, guid, chunk, file);
        } catch (Exception e) {
            return RetResponse.makeErrRsp("上传失败:"+e.getMessage());
        }
        return RetResponse.makeRsp("上传成功");
    }
    @ApiOperation(value = "合并分片文档")
    @RequiresPermissions(value = "attach:merge", note = "合并分片文档")
    @PostMapping("/attach/merge")
    public RetResult attachMerge(String guid, Integer chunks,@Validated SyAttachVo syAttachVo) {
        try {
            List<Integer> chunksExist=new ArrayList<>();
            Long id=syAttachService.attachMerge(guid, chunks,syAttachVo,chunksExist);
            if(id==-1){
                return RetResponse.makeRsp(RetCode.FAIL.code,"合并文档失败!",chunksExist);
            }
            return RetResponse.makeRsp("合并成功",id.toString());
        } catch (Exception e) {
            return RetResponse.makeRsp(RetCode.FAIL.code,"合并文档失败!",new ArrayList<Integer>());
        }
    }

    @ApiOperation(value = "分片下载文档")
    @RequiresPermissions(value = "attach:get", note = "分片下载文档")
    @GetMapping("/attach/{id}")
    public void downloadAttach(@PathVariable("id") Long attachId,HttpServletResponse response
           ,@RequestHeader(name = "Range", required = false) String range) throws InterruptedException {
        log.debug("range---"+range);
        //Thread.sleep(5000*90);
       syAttachService.downloadAttach(attachId,response, range);
    }

    @ApiOperation(value = "删除文档")
    @RequiresPermissions(value = "attach:get", note = "删除文档")
    @DeleteMapping("/attach/{id}")
    public RetResult removeAttach(@PathVariable("id") String attachId) {
        syAttachService.removeAttach(attachId);
        return RetResponse.makeRsp("删除文档成功");
    }

}
