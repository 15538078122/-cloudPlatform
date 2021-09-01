package com.hd.micromonitorservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.common.vo.SyMonitorVo;
import com.hd.micromonitorservice.entity.SyMonitorEntity;
import com.hd.micromonitorservice.mapper.SyMonitorMapper;
import com.hd.micromonitorservice.service.SyMonitorService;
import com.hd.micromonitorservice.utils.VoConvertUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wli
 * @since 2021-09-01
 */
@Service
public class SyMonitorServiceImpl extends ServiceImpl<SyMonitorMapper, SyMonitorEntity> implements SyMonitorService {

    @Override
    public void heartbeat(String serviceName) {
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("service_name",serviceName);
        updateWrapper.set("heartbeat_tm",new Date());
        update(updateWrapper);
    }

    @Override
    public List<SyMonitorVo> listServiceHeartbeat() {
        List<SyMonitorEntity> syMonitorEntities=list();
        List<SyMonitorVo> syMonitorVos=new ArrayList<>();
        for(SyMonitorEntity syMonitorEntity:syMonitorEntities){
            SyMonitorVo syMonitorVo=new SyMonitorVo();
            VoConvertUtils.convertObject(syMonitorEntity,syMonitorVo);
            if(((new Date()).getTime() - syMonitorVo.getHeartbeatTm().getTime())>1000*35){
                //35s
                syMonitorVo.setOnLine(false);
            }
            else {
                syMonitorVo.setOnLine(true);
            }
            syMonitorVos.add(syMonitorVo);
        }
        return syMonitorVos;
    }
}
