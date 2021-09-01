package com.hd.microauservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.common.vo.SyMenuBtnVo;
import com.hd.microauservice.entity.SyMenuBtnEntity;
import com.hd.microauservice.mapper.SyMenuBtnMapper;
import com.hd.microauservice.service.SyMenuBtnService;
import com.hd.microauservice.utils.VoConvertUtils;
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
            syMenuBtnVos.add(VoConvertUtils.syMenuBtnToVo(syMenuBtnEntity));
        }
        return  syMenuBtnVos;
    }


}
