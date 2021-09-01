package com.hd.micromonitorservice.service;

import com.hd.common.vo.SyMonitorVo;
import com.hd.micromonitorservice.entity.SyMonitorEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wli
 * @since 2021-09-01
 */
public interface SyMonitorService extends IService<SyMonitorEntity> {
        void  heartbeat(String serviceName);
        List<SyMonitorVo> listServiceHeartbeat();
}
