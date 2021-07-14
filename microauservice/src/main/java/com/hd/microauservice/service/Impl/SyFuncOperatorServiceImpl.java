package com.hd.microauservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hd.common.vo.SyFuncOperatorVo;
import com.hd.common.vo.SyMenuBtnVo;
import com.hd.microauservice.entity.SyFuncOperatorEntity;
import com.hd.microauservice.entity.SyMenuBtnEntity;
import com.hd.microauservice.mapper.SyFuncOperatorMapper;
import com.hd.microauservice.service.SyFuncOperatorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class SyFuncOperatorServiceImpl extends ServiceImpl<SyFuncOperatorMapper, SyFuncOperatorEntity> implements SyFuncOperatorService {

    @Override
    public List<SyFuncOperatorVo> getOprsByFuncId(Long id) {
        QueryWrapper<SyFuncOperatorEntity> queryWrapper=new QueryWrapper();
        queryWrapper.eq("func_id",id);
        List<SyFuncOperatorEntity> syFuncOperatorEntities = this.baseMapper.selectList(queryWrapper);
        List<SyFuncOperatorVo> syFuncOperatorVos=new ArrayList<>();
        for (SyFuncOperatorEntity syFuncOperatorEntity : syFuncOperatorEntities){
            SyFuncOperatorVo syFuncOperatorVo=new SyFuncOperatorVo();
            VoConvertUtils.convertObject(syFuncOperatorEntity,syFuncOperatorVo);
            syFuncOperatorVos.add(syFuncOperatorVo);
        }
        return  syFuncOperatorVos;
    }
}
