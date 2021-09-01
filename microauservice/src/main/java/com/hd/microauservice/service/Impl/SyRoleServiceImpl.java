package com.hd.microauservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.common.model.TokenInfo;
import com.hd.common.vo.SyMenuBtnVo;
import com.hd.common.vo.SyRoleVo;
import com.hd.microauservice.conf.SecurityContext;
import com.hd.microauservice.entity.SyRoleEntity;
import com.hd.microauservice.entity.SyRolePermEntity;
import com.hd.microauservice.mapper.SyRoleMapper;
import com.hd.microauservice.service.SyRolePermService;
import com.hd.microauservice.service.SyRoleService;
import com.hd.microauservice.utils.EnterpriseVerifyUtil;
import com.hd.microauservice.utils.VoConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wli
 * @since 2021-07-13
 */
@Service
public class SyRoleServiceImpl extends ServiceImpl<SyRoleMapper, SyRoleEntity> implements SyRoleService {

    @Autowired
    SyRolePermService syRolePermService;

    @Autowired
    SyRoleService syRoleService;

    @Override
    public void createRole(SyRoleVo syRoleVo) throws Exception {
        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        QueryWrapper queryWrapper = new QueryWrapper() {{
            eq("name", syRoleVo.getName());
            eq("enterprise_id",syRoleVo.getEnterpriseId());
        }};
        if (getOne(queryWrapper) != null) {
            throw new Exception("角色已存在!");
        }

        SyRoleEntity syRoleEntity=new SyRoleEntity();
        VoConvertUtils.convertObject(syRoleVo,syRoleEntity);
        save(syRoleEntity);
        //更新权限
        if (syRoleVo.getSyMenuBtnVos() != null) {
            syRoleEntity = getOne(queryWrapper);
            updatePerms(syRoleEntity.getId(),syRoleVo.getSyMenuBtnVos());
        }
    }

    @Override
    public void updateRole(SyRoleVo syRoleVo) {
        SyRoleEntity syRoleEntity=new SyRoleEntity();
        VoConvertUtils.convertObject(syRoleVo,syRoleEntity);
        updateById(syRoleEntity);
        //更新权限
        if (syRoleVo.getSyMenuBtnVos() != null) {
            updatePerms(syRoleEntity.getId(),syRoleVo.getSyMenuBtnVos());
        }
    }

    @Override
    public void removeRoleId(Long roleId) {
        removeById(roleId);
        updatePerms(roleId,new ArrayList<>());
    }

    @Override
    public SyRoleVo getRoleDetail(Long roleId) {
        //验证存在模块
        Assert.isTrue(roleId!=null,"id不能为NULL!");
        SyRoleEntity syRoleEntity=syRoleService.getById(roleId);
        Assert.isTrue(syRoleEntity!=null,String.format("角色(id=%s)不存在!",roleId));
        EnterpriseVerifyUtil.verifyEnterId(syRoleEntity.getEnterpriseId());

        SyRoleVo syRoleVo=new SyRoleVo();
        VoConvertUtils.convertObject(syRoleEntity,syRoleVo);
        //查询角色的授权按钮
        List<SyMenuBtnVo> syMenuBtnVos = baseMapper.getRolePermBtn(roleId);
        syRoleVo.setSyMenuBtnVos(syMenuBtnVos);
        return syRoleVo;
    }

    private void updatePerms(Long roleId, List<SyMenuBtnVo> syMenuBtnVos) {
        //删除旧的
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("role_id", roleId);
        syRolePermService.remove(queryWrapper);
        //创建新的
        List<SyRolePermEntity> syRolePermEntities = new ArrayList<>();
        for (SyMenuBtnVo syMenuBtnVo : syMenuBtnVos) {
            SyRolePermEntity syRolePermEntity = new SyRolePermEntity() {{
                setRoleId(roleId);
                setMenuBtnId(syMenuBtnVo.getId());
            }};
            syRolePermEntities.add(syRolePermEntity);
        }
        syRolePermService.saveBatch(syRolePermEntities);
    }
}
