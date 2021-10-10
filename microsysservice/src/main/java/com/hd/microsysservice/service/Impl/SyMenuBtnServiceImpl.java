package com.hd.microsysservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.common.vo.SyMenuBtnVo;
import com.hd.microsysservice.entity.SyMenuBtnEntity;
import com.hd.microsysservice.mapper.SyMenuBtnMapper;
import com.hd.microsysservice.service.SyMenuBtnService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wli
 * @since 2021-07-12
 */
@Service
public class SyMenuBtnServiceImpl extends ServiceImpl<SyMenuBtnMapper, SyMenuBtnEntity> implements SyMenuBtnService {

    SyMenuBtnService.SyMenuBtnVoConvertUtils syMenuBtnVoConvertUtils=new SyMenuBtnService.SyMenuBtnVoConvertUtils();

    @Override
    public List<SyMenuBtnVo> getUserMenuBtns(Long userId, Long menuId) {
        List<SyMenuBtnEntity> syMenuBtnEntities = baseMapper.getUserMenuBtns(userId, menuId);
        return syMenuBtnVoConvertUtils.convertToListT2(syMenuBtnEntities);
    }
    @Override
    public List<SyMenuBtnVo> getUserAllMenuBtns(Long userId, List<Long> menuIds) {
        List<SyMenuBtnEntity> syMenuBtnEntities = baseMapper.getUserAllMenuBtns(userId, menuIds);
        return syMenuBtnVoConvertUtils.convertToListT2(syMenuBtnEntities);
    }

    @Override
    public List<SyMenuBtnVo> getBtnsByMenuId(Long menuId,Boolean isAll) {
        QueryWrapper<SyMenuBtnEntity> queryWrapper=new QueryWrapper();
        queryWrapper.eq("menu_id",menuId);
        if(!isAll){
            queryWrapper.eq("is_visible",1);
        }
        List<SyMenuBtnEntity> syMenuBtnEntities = this.baseMapper.selectList(queryWrapper);
        List<SyMenuBtnVo> syMenuBtnVos=new ArrayList<>();
        for (SyMenuBtnEntity syMenuBtnEntity : syMenuBtnEntities){
            syMenuBtnVos.add(syMenuBtnVoConvertUtils.convertToT2(syMenuBtnEntity));
        }
        return  syMenuBtnVos;
    }


}
