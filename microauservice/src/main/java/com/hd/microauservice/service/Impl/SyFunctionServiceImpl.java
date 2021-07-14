package com.hd.microauservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hd.common.vo.SyFuncOperatorVo;
import com.hd.common.vo.SyFunctionVo;
import com.hd.common.vo.SyMenuBtnVo;
import com.hd.common.vo.SyMenuVo;
import com.hd.microauservice.entity.SyFunctionEntity;
import com.hd.microauservice.entity.SyMenuEntity;
import com.hd.microauservice.mapper.SyFunctionMapper;
import com.hd.microauservice.service.SyFuncOperatorService;
import com.hd.microauservice.service.SyFunctionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.microauservice.service.SyMenuBtnService;
import com.hd.microauservice.utils.VoConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
            VoConvertUtils.convertObject(syFunctionEntity,syFunctionVo);
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
}
