package com.hd.microsysservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.common.model.TokenInfo;
import com.hd.common.vo.SyMenuBtnVo;
import com.hd.common.vo.SyRoleVo;
import com.hd.microsysservice.conf.SecurityContext;
import com.hd.microsysservice.entity.SyRoleEntity;
import com.hd.microsysservice.entity.SyRolePermEntity;
import com.hd.microsysservice.mapper.SyRoleMapper;
import com.hd.microsysservice.service.SyRolePermService;
import com.hd.microsysservice.service.SyRoleService;
import com.hd.microsysservice.service.SyUserRoleService;
import com.hd.microsysservice.utils.VerifyUtil;
import com.hd.microsysservice.utils.VoConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @Autowired
    RedisTemplate redisTemplate;

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
        VoConvertUtils.copyObjectProperties(syRoleVo,syRoleEntity);
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
        VoConvertUtils.copyObjectProperties(syRoleVo,syRoleEntity);
        updateById(syRoleEntity);
        //更新权限
        if (syRoleVo.getSyMenuBtnVos() != null) {
            updatePerms(syRoleEntity.getId(),syRoleVo.getSyMenuBtnVos());
        }
        Set<String> keys = redisTemplate.keys(String.format("%s::%s", "userMenu", "*"));
        redisTemplate.delete(keys);
    }

    @Autowired
    SyUserRoleService syUserRoleService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    public void removeRoleId(Long roleId) {
        //如果角色被引用，不能删除
        QueryWrapper queryWrapper=new QueryWrapper(){{
           eq("role_id",roleId);
        }};
        Assert.isTrue(syUserRoleService.list(queryWrapper).size()==0,"角色使用中!");
        removeById(roleId);
        updatePerms(roleId,new ArrayList<>());
    }

    @Override
    public SyRoleVo getRoleDetail(Long roleId) {
        //验证存在模块
        Assert.isTrue(roleId!=null,"id不能为NULL!");
        SyRoleEntity syRoleEntity=syRoleService.getById(roleId);
        Assert.isTrue(syRoleEntity!=null,String.format("角色(id=%s)不存在!",roleId));
        VerifyUtil.verifyEnterId(syRoleEntity.getEnterpriseId());

        SyRoleVo syRoleVo=new SyRoleVo();
        VoConvertUtils.copyObjectProperties(syRoleEntity,syRoleVo);
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
