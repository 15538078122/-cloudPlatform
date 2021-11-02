package com.hd.microsysservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.common.vo.SyUpgradeVo;
import com.hd.microsysservice.conf.SecurityContext;
import com.hd.microsysservice.entity.SysUpgradeEntity;
import com.hd.microsysservice.mapper.SysUpgradeMapper;
import com.hd.microsysservice.service.SysUpgradeService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wli
 * @since 2021-09-22
 */
@Service
public class SysUpgradeServiceImpl extends ServiceImpl<SysUpgradeMapper, SysUpgradeEntity> implements SysUpgradeService {

    SyUpgradeVoConvertUtils syUpgradeVoConvertUtils=new SyUpgradeVoConvertUtils();

    @Override
    public void uploadAppVersion(SyUpgradeVo syUpgradeVo) {
        //版本名称是否已存在
        SysUpgradeEntity sysUpgradeEntity =   getOne(new QueryWrapper(){{
           eq("type",syUpgradeVo.getType());
           eq("version_name",syUpgradeVo.getVersionName());
            if(SecurityContext.GetCurTokenInfo().getEnterpriseId().compareTo("root")!=0){
                eq("enterprise_id", SecurityContext.GetCurTokenInfo().getEnterpriseId());
                syUpgradeVo.setEnterpriseId(SecurityContext.GetCurTokenInfo().getEnterpriseId());
            }
            else {
                Assert.notNull(syUpgradeVo.getEnterpriseId(),String.format("%s不能为空!","企业Id"));
                eq("enterprise_id",syUpgradeVo.getEnterpriseId());
            }
        }});
        Assert.isNull(sysUpgradeEntity,String.format("版本%s已存在!",syUpgradeVo.getVersionName()));
        Assert.notNull(syUpgradeVo.getVersion(),String.format("缺少版本号%s!","version"));
        //保存，自增version
        Integer versiong = baseMapper.getMaxVersionByType(syUpgradeVo.getType());
        versiong=(versiong == null?0:versiong);
        Assert.isTrue(syUpgradeVo.getVersion()>versiong,String.format("版本号%s太低,当前版本号%s!",syUpgradeVo.getVersion(),versiong));
        //syUpgradeVo.setVersion(versiong+1);
        syUpgradeVo.setCreateDate(new Date());
        save(syUpgradeVoConvertUtils.convertToT1(syUpgradeVo));
    }

    @Override
    public SyUpgradeVo getAppVersion() {
//        try {
//            Thread.sleep(21000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        SysUpgradeEntity sysUpgradeEntity =   getOne(new QueryWrapper(){{
             last("limit 0,1");
             orderByDesc("version");
             eq("enterprise_id", SecurityContext.GetCurTokenInfo().getEnterpriseId());

        }});
        //返回最新版本
        //Assert.isTrue(sysUpgradeEntity!=null,"系统无更新!");
        if(sysUpgradeEntity==null)
       {
           sysUpgradeEntity=new SysUpgradeEntity(){{
            setVersion(0);
        }};
       }
        return syUpgradeVoConvertUtils.convertToT2(sysUpgradeEntity);
    }
}
