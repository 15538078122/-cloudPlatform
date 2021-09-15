package com.hd.microsysservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.common.vo.SyFuncOperatorVo;
import com.hd.common.vo.SyFunctionVo;
import com.hd.microsysservice.entity.SyFunctionEntity;
import com.hd.microsysservice.mapper.SyFunctionMapper;
import com.hd.microsysservice.service.SyFuncOperatorService;
import com.hd.microsysservice.service.SyFunctionService;
import com.hd.microsysservice.utils.VoConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
public class SyFunctionServiceImpl extends ServiceImpl<SyFunctionMapper, SyFunctionEntity> implements SyFunctionService {

    @Autowired
    SyFuncOperatorService syFuncOperatorService;

    @Override
    public List<SyFunctionVo> getFuncTree() {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.orderByAsc("path_code");

        List<SyFunctionEntity> list = super.list(queryWrapper);

        List<SyFunctionVo> listVo = new ArrayList<>();
        for(SyFunctionEntity syFunctionEntity:list){
            SyFunctionVo syFunctionVo=new SyFunctionVo();
            VoConvertUtils.copyObjectProperties(syFunctionEntity,syFunctionVo);
            List<SyFuncOperatorVo> oprs = syFuncOperatorService.getOprsByFuncId(syFunctionVo.getId());
            if(oprs.size()>0){
                syFunctionVo.setOprs(oprs);
            }
            listVo.add(syFunctionVo);
        }

        List<SyFunctionVo> listTree=rearrange(listVo);

        return  listTree;
    }
    private List<SyFunctionVo> rearrange(List<SyFunctionVo> syFuncVos) {
        List<SyFunctionVo> topFunc=new ArrayList<>();

        //赋值父节点
        for(SyFunctionVo syFuncVo:syFuncVos){
            if(syFuncVo.getParentId()!=null){
                SyFunctionVo parent=null;
                for(SyFunctionVo syFuncVo2:syFuncVos){
                    if(syFuncVo2.getId().equals(syFuncVo.getParentId())){
                        parent=syFuncVo2;
                        break;
                    }
                }
                if(parent!=null){
                    List<SyFunctionVo> childs = parent.getChilds();
                    if(childs==null){
                        childs=new ArrayList<>();
                        parent.setChilds(childs);
                    }
                    childs.add(syFuncVo);
                }
            }
            //查找顶层节点
            if(syFuncVo.getParentId()==null){
                topFunc.add(syFuncVo);
            }
        }
        return  topFunc;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    public void deleteFunc(Long funcId) throws Exception {
        //移除
        SyFunctionEntity syFunctionEntity = getById(funcId);
        deleteFuncRecursion(syFunctionEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    public void updateFunc(Long funcId, SyFunctionVo syFuncVo) {
        SyFunctionEntity syFunctionEntityOld = getById(syFuncVo.getId());
        Assert.notNull(syFunctionEntityOld,String.format("菜单(id=%s)不存在!",syFuncVo.getId()));
        //更新关联编码
        SyFunctionEntity syFunctionEntityNew = new SyFunctionEntity();
        VoConvertUtils.copyObjectProperties(syFuncVo,syFunctionEntityNew);
        if(syFunctionEntityOld.getPathCode().compareTo(syFunctionEntityNew.getPathCode())!=0){
           recurUpdatePathCode(syFunctionEntityNew);
        }
        updateById(syFunctionEntityNew);
    }

    private void recurUpdatePathCode(SyFunctionEntity syFunctionEntity) {
        if(syFunctionEntity.getType()==0){
            //如果是目录，更新子级pathcode
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("parent_id",syFunctionEntity.getId());
            List<SyFunctionEntity> syFunctionEntities = list(queryWrapper);
            for(SyFunctionEntity item : syFunctionEntities){
                SyFunctionEntity syFunctionEntityNew=new SyFunctionEntity(){{
                    setId(item.getId());
                    setType(item.getType());
                }};
                syFunctionEntityNew.setPathCode(String.format("%s.%s",syFunctionEntity.getPathCode(),item.getLevelCode()));
                updateById(syFunctionEntityNew);
                recurUpdatePathCode(syFunctionEntityNew);
            }
        }
    }

    private void deleteFuncRecursion(SyFunctionEntity syFunctionEntity) throws Exception {
        removeById(syFunctionEntity.getId());
        if(syFunctionEntity.getType()==0){
            //目录，删除子目录
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("parent_id",syFunctionEntity.getId());
            List<SyFunctionEntity> syFunctionEntities = list(queryWrapper);
            for(SyFunctionEntity item : syFunctionEntities){
                deleteFuncRecursion(item);
            }
        }
        else {
            //功能，删除操作
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("func_id",syFunctionEntity.getId());
//            List<SyFuncOperatorEntity> syFuncOperatorEntities = syFuncOperatorService.list(queryWrapper);
//            for(SyFuncOperatorEntity item : syFuncOperatorEntities){
//                syFuncOperatorService.removeById(item.getId());
//            }
            syFuncOperatorService.remove(queryWrapper);
        }
    }
}
