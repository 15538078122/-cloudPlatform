package com.hd.microsysservice.service.Impl;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.common.model.TokenInfo;
import com.hd.common.vo.SyAttachVo;
import com.hd.microsysservice.conf.SecurityContext;
import com.hd.microsysservice.entity.SyAttachEntity;
import com.hd.microsysservice.mapper.SyAttachMapper;
import com.hd.microsysservice.service.SyAttachService;
import com.hd.microsysservice.utils.VerifyUtil;
import com.hd.microsysservice.utils.FileRange;
import com.hd.microsysservice.utils.VoConvertUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wli
 * @since 2021-07-30
 */
@Service
public class SyAttachServiceImpl extends ServiceImpl<SyAttachMapper, SyAttachEntity> implements SyAttachService {

    @Value("${config.attach-path}")
    String attachPath;

    @Autowired
    IdentifierGenerator identifierGenerator;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    @Override
    public void attachUpload(HttpServletRequest request, HttpServletResponse response, String guid, Integer chunk, MultipartFile file) throws IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        Assert.isTrue(isMultipart, String.format("没有上传文档数据!"));

        if (chunk == null) {
            chunk = 0;
        }
        // 临时目录用来存放所有分片文件
        String tempFileDir = attachPath + guid;
        File parentFileDir = new File(tempFileDir);
        if (!parentFileDir.exists()) {
            parentFileDir.mkdirs();
        }
        // 分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台
        File destFile = new File(parentFileDir, guid + "_" + chunk + ".part");
        if(destFile.exists()){
            destFile.delete();
        }
        File tempPartFile = new File(parentFileDir, guid + "_" + chunk + ".part");
        FileUtils.copyInputStreamToFile(file.getInputStream(), tempPartFile);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    @Override
    public Long attachMerge(String guid, Integer chunks, SyAttachVo syAttachVo, List<Integer> chunksExist) throws IOException {
        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        syAttachVo.setEnterpriseId(tokenInfo.getEnterpriseId());
        syAttachVo.setUploadBy(Long.parseLong(tokenInfo.getId()));
        syAttachVo.setUploadTime(new Date());
        // 得到 destFile 就是最终的文件
        String fileName = syAttachVo.getFileName();
        String fileNewName = fileName.substring(fileName.lastIndexOf("."));
        //时间格式化格式
        Date currentTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        //获取当前时间并作为时间戳
        String timeStamp = simpleDateFormat.format(currentTime);
        //拼接新的文件名
        fileNewName = timeStamp + fileName;

        simpleDateFormat = new SimpleDateFormat("yyyyMM");
        timeStamp = simpleDateFormat.format(currentTime);
        syAttachVo.setFileNewName(timeStamp + "/" + fileNewName);

        File parentFileDir = new File(attachPath + guid);
        if (parentFileDir.isDirectory()) {
            //分片目录存在
            //目标文件如果存在，先删除
            File destFile = new File(attachPath + timeStamp, fileNewName);
            if (destFile.exists()) {
                destFile.delete();
            }

            //检查存在的文件trunks
            for (int i = 0; i < chunks; i++) {
                File partFile = new File(parentFileDir, guid + "_" + i + ".part");
                if (partFile.exists()) {
                    chunksExist.add(i);
                }
            }
            if (chunksExist.size() != chunks) {
                return -1L;
            }
            //先得到文件的上级目录，并创建上级目录，在创建文件
            destFile.getParentFile().mkdir();
            destFile.createNewFile();

            //for (int i = 0; i < parentFileDir.listFiles().length; i++)
            for (int i = 0; i < chunks; i++) {
                File partFile = new File(parentFileDir, guid + "_" + i + ".part");
                FileOutputStream destfos = new FileOutputStream(destFile, true);
                //遍历"所有分片文件"到"最终文件"中
                FileUtils.copyFile(partFile, destfos);
                destfos.close();
            }
            // 删除临时目录中的分片文件
            FileUtils.deleteDirectory(parentFileDir);
            //保存
            syAttachVo.setFileSize(destFile.length());
            SyAttachEntity syAttachEntity = new SyAttachEntity();
            VoConvertUtils.copyObjectProperties(syAttachVo, syAttachEntity);
            save(syAttachEntity);
            return syAttachEntity.getId();
        } else {
            return -1L;
        }
    }

    @Override
    public void downloadAttach(Long attachId, HttpServletResponse response, String range) {
        SyAttachEntity syAttachEntity = getById(attachId);
        Assert.isTrue(syAttachEntity != null, String.format("文档Id:%s不存在!", attachId));
        String filePath = attachPath + syAttachEntity.getFileNewName();

        File file = new File(filePath);
        String filename = syAttachEntity.getFileName();
        // file.getName();
        long length = file.length();
        FileRange full = new FileRange(0, length - 1, length);
        List<FileRange> ranges = new ArrayList<>();
        //处理Range
        try {
            if (!file.exists()) {
                String msg = "需要下载的文件不存在：" + file.getAbsolutePath();
                log.error(msg);
                throw new RuntimeException(msg);
            }
            if (file.isDirectory()) {
                String msg = "需要下载的文件的路径对应的是一个文件夹：" + file.getAbsolutePath();
                log.error(msg);
                throw new RuntimeException(msg);
            }
            dealRanges(full, range, ranges, response, length);
        } catch (Exception e) {
            throw new RuntimeException("文件下载异常：" + e.getMessage());
        }
        // 如果浏览器支持内容类型，则设置为“内联”，否则将弹出“另存为”对话框. attachment inline
        String disposition = "attachment";

        // 将需要下载的文件段发送到客服端，准备流.
        try (RandomAccessFile input = new RandomAccessFile(file, "r");
             ServletOutputStream output = response.getOutputStream()) {
            //最后修改时间
            FileTime lastModifiedObj = Files.getLastModifiedTime(file.toPath());
            long lastModified = LocalDateTime.ofInstant(lastModifiedObj.toInstant(),
                    ZoneId.of(ZoneId.systemDefault().getId())).toEpochSecond(ZoneOffset.UTC);
            //初始化response.
            response.reset();
            response.setBufferSize(20480);
            response.setHeader("Content-type", "application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", disposition + ";filename=" +
                    URLEncoder.encode(filename, StandardCharsets.UTF_8.name()));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("ETag", URLEncoder.encode(filename, StandardCharsets.UTF_8.name()));
            response.setDateHeader("Last-Modified", lastModified);
            response.setDateHeader("Expires", System.currentTimeMillis() + 604800000L);
            //输出Range到response
            outputRange(response, ranges, input, output, full, length);
            output.flush();
            response.flushBuffer();
        } catch (Exception e) {
            throw new RuntimeException("文件下载异常：" + e.getMessage());
        }
    }

    @Override
    public void removeAttach(String attachId) {
        SyAttachEntity syAttachEntity = getById(attachId);
        Assert.isTrue(syAttachEntity != null, String.format("文档Id:%s不存在!", attachId));
        VerifyUtil.verifyEnterId(syAttachEntity.getEnterpriseId());
        String filePath = attachPath + syAttachEntity.getFileNewName();
        File file = new File(filePath);
        file.delete();
        removeById(attachId);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    @Override
    public Long attachUploadOne(HttpServletRequest request, HttpServletResponse response, SyAttachVo syAttachVo, MultipartFile file) {

        Number number = identifierGenerator.nextId(null);
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        Assert.isTrue(isMultipart, String.format("没有上传文档数据!"));
        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        syAttachVo.setEnterpriseId(tokenInfo.getEnterpriseId());
        syAttachVo.setUploadBy(Long.parseLong(tokenInfo.getId()));
        syAttachVo.setUploadTime(new Date());
        syAttachVo.setFileName(file.getOriginalFilename());
        syAttachVo.setFileSize(file.getSize());
        // 得到 destFile 就是最终的文件
        String fileName = syAttachVo.getFileName();
        String fileNewName = fileName.substring(fileName.lastIndexOf("."));
        //时间格式化格式
        Date currentTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        //获取当前时间并作为时间戳
        String timeStamp = simpleDateFormat.format(currentTime);
        //拼接新的文件名
        fileNewName = timeStamp + fileName;

        simpleDateFormat = new SimpleDateFormat("yyyyMM");
        timeStamp = simpleDateFormat.format(currentTime);
        syAttachVo.setFileNewName(timeStamp + "/" + fileNewName);

        try {
            File destFile = new File(attachPath + timeStamp, fileNewName);
            if (destFile.exists()) {
                destFile.delete();
            }
            FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);
            //保存
            SyAttachEntity syAttachEntity = new SyAttachEntity();
            VoConvertUtils.copyObjectProperties(syAttachVo, syAttachEntity);
            save(syAttachEntity);
            return syAttachEntity.getId();
        } catch (Exception ex) {
            return -1L;
        }
    }

    /**
     * 处理请求中的Range(多个range或者一个range，每个range范围)
     *
     * @param range    :
     * @param ranges   :
     * @param response :
     * @param length   :
     * @author kevin
     * @date 2021/1/17
     */
    private void dealRanges(FileRange full, String range, List<FileRange> ranges, HttpServletResponse response,
                            long length) throws Exception {
        if (range != null) {
            // Range 头的格式必须为 "bytes=n-n,n-n,n-n...". 如果不是此格式, 返回 416.
            if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
                //response.setHeader("Content-Range", "bytes */" + length);
                //response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                throw new Exception("Range 头的格式必须为 \"bytes=n-n,n-n,n-n...\"！");
            }

            // 处理传入的range的每一段.
            for (String part : range.substring(6).split(",")) {
                part = part.split("/")[0];
                // 对于长度为100的文件，以下示例返回:
                // 50-80 (50 to 80), 40- (40 to length=100), -20 (length-20=80 to length=100).
                int delimiterIndex = part.indexOf("-");
                long start = FileRange.sublong(part, 0, delimiterIndex);
                long end = FileRange.sublong(part, delimiterIndex + 1, part.length());

                //如果未设置起始点，则计算的是最后的 end 个字节；设置起始点为 length-end，结束点为length-1
                //如果未设置结束点，或者结束点设置的比总长度大，则设置结束点为length-1
                if (start == -1) {
                    start = length - end;
                    end = length - 1;
                } else if (end == -1 || end > length - 1) {
                    end = length - 1;
                }

                // 检查Range范围是否有效。如果无效，则返回416.
                if (start > end) {
                    //response.setHeader("Content-Range", "bytes */" + length);
                    //response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                    throw new Exception("Range 头的格式范围无效！");
                }
                // 添加Range范围.
                //ranges.add(new FileRange(start, end, end - start + 1));
                ranges.add(new FileRange(start, end, length));
            }
        } else {
            //如果未传入Range，默认下载整个文件
            ranges.add(full);
        }
    }

    /**
     * output写流输出到response
     *
     * @param response :
     * @param ranges   :
     * @param input    :
     * @param output   :
     * @param full     :
     * @param length   :
     * @author kevin
     * @date 2021/1/17
     */
    private void outputRange(HttpServletResponse response, List<FileRange> ranges, RandomAccessFile input,
                             ServletOutputStream output, FileRange full, long length) throws IOException {
        if (ranges.isEmpty() || ranges.get(0) == full) {
            // 返回整个文件.
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Range", "bytes " + full.start + "-" + full.end + "/" + full.total);
            response.setHeader("Content-length", String.valueOf(full.length));
            response.setStatus(HttpServletResponse.SC_OK);
            FileRange.copy(input, output, length, full.start, full.length);
        } else if (ranges.size() == 1) {
            // 返回文件的一个分段.
            FileRange r = ranges.get(0);
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Range", "bytes " + r.start + "-" + r.end + "/" + r.total);
            response.setHeader("Content-length", String.valueOf(r.length));
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            // 复制单个文件分段.
            FileRange.copy(input, output, length, r.start, r.length);
        } else {
            // 返回文件的多个分段.
            response.setContentType("multipart/byteranges; boundary=MULTIPART_BYTERANGES");
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

            // 复制多个文件分段.
            for (FileRange r : ranges) {
                //为每个Range添加MULTIPART边界和标题字段
                output.println();
                output.println("--MULTIPART_BYTERANGES");
                output.println("Content-Type: application/octet-stream;charset=UTF-8");
                output.println("Content-length: " + r.length);
                output.println("Content-Range: bytes " + r.start + "-" + r.end + "/" + r.total);
                // 复制多个需要复制的文件分段当中的一个分段.
                FileRange.copy(input, output, length, r.start, r.length);
            }
            // 以MULTIPART文件的边界结束.
            output.println();
            output.println("--MULTIPART_BYTERANGES--");
        }
    }
}


//<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
//<html xmlns="http://www.w3.org/1999/xhtml">
//<head>
//<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
//<link href="css/webuploader.css" rel="external nofollow" rel="stylesheet" type="text/css" />
//<script type="text/javascript" src="jquery-1.10.1.min.js"></script>
//<script type="text/javascript" src="dist/webuploader.min.js"></script>
//</head>
//<body>
//<div id="uploader">
//<div class="btns">
//<div id="picker">选择文件</div>
//<button id="startBtn" class="btn btn-default">开始上传</button>
//</div>
//</div>
//</body>
//<script type="text/javascript">
//        var GUID = WebUploader.Base.guid();//一个GUID
//        var uploader = WebUploader.create({
//        // swf文件路径
//        swf: 'dist/Uploader.swf',
//        // 文件接收服务端。
//        server: 'http://localhost:8080/api/upload/part',
//        formData:{
//        guid : GUID
//        },
//        pick: '#picker',
//        chunked : true, // 分片处理
//        chunkSize : 1 * 1024 * 1024, // 每片1M,
//        chunkRetry : false,// 如果失败，则不重试
//        threads : 1,// 上传并发数。允许同时最大上传进程数。
//        resize: false
//        });
//        $("#startBtn").click(function () {
//        uploader.upload();
//        });
////当文件上传成功时触发。
//        uploader.on( "uploadSuccess", function( file ) {
//        $.post('http://localhost:8080/api/upload/merge', { guid: GUID, fileName: file.name}, function (data) {
//        if(data.code == 200){
//        alert('上传成功!');
//        }
//        });
//        });
//</script>
//</html>


