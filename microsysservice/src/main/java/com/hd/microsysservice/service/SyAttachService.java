package com.hd.microsysservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hd.common.vo.SyAttachVo;
import com.hd.microsysservice.entity.SyAttachEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wli
 * @since 2021-07-30
 */
public interface SyAttachService extends IService<SyAttachEntity> {

    void attachUpload(HttpServletRequest request, HttpServletResponse response, String guid, Integer chunk, MultipartFile file) throws IOException;

    /**
     *
     * @param guid
     * @param fileName
     * @param chunks
     * @param syAttachVo
     * @param chunksExist
     * @return
     * @throws Exception
     */
     Boolean attachMerge(String guid, Integer chunks, SyAttachVo syAttachVo,List<Integer> chunksExist) throws Exception;

    void downloadAttach(Long attachId, HttpServletResponse response, String range);

    void removeAttach(String attachId);
}
