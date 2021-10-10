package com.hd.micromonitorservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    public void heartbeat(String serviceName,String clientId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("service_name",serviceName);
        queryWrapper.eq("client_id",clientId);
        SyMonitorEntity syMonitorEntity = getOne(queryWrapper);
        if(syMonitorEntity==null){
            queryWrapper=new QueryWrapper();
            queryWrapper.eq("service_name",serviceName);
            queryWrapper.isNull("client_id");
            String showName = getOne(queryWrapper).getShowName();
            syMonitorEntity=new SyMonitorEntity(){{
                setServiceName(serviceName);
                setClientId(clientId);
                setShowName(showName);
                setHeartbeatTm(new Date());
            }};
            save(syMonitorEntity);
        }
        else {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", syMonitorEntity.getId());
            updateWrapper.set("heartbeat_tm", new Date());
            update(updateWrapper);
        }


    }

    @Override
    public List<SyMonitorVo> listServiceHeartbeat() {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.isNotNull("client_id");
        queryWrapper.orderByAsc("show_name");
        List<SyMonitorEntity> syMonitorEntities=list(queryWrapper);
        List<SyMonitorVo> syMonitorVos=new ArrayList<>();
        for(SyMonitorEntity syMonitorEntity:syMonitorEntities){
            SyMonitorVo syMonitorVo=new SyMonitorVo();
            VoConvertUtils.copyObjectProperties(syMonitorEntity,syMonitorVo);
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

    @Override
    public void removeOnStart() {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.isNotNull("client_id");
        remove(queryWrapper);
    }
}
