package com.hd.microauservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.common.model.DataPrivilege;
import com.hd.common.vo.SyOrgVo;
import com.hd.microauservice.conf.SecurityContext;
import com.hd.microauservice.entity.SyOrgEntity;
import com.hd.microauservice.entity.SyUserEntity;
import com.hd.microauservice.mapper.SyOrgMapper;
import com.hd.microauservice.service.SyOrgService;
import com.hd.microauservice.service.SyUserService;
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
 * @since 2021-07-13
 */
@Service
public class SyOrgServiceImpl extends ServiceImpl<SyOrgMapper, SyOrgEntity> implements SyOrgService {

    @Autowired
    SyUserService syUserService;

    @Override
    public List<SyOrgVo> getOrgTree(String enterpriseId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.orderByAsc("path_code");
        queryWrapper.eq("enterprise_id",enterpriseId); //SecurityContext.GetCurTokenInfo().getEnterpriseId());
        List<SyOrgEntity> list = super.list(queryWrapper);

        List<SyOrgVo> listVo = new ArrayList<>();
        for(SyOrgEntity syOrgEntity:list){
            SyOrgVo syOrgVo=new SyOrgVo();
            VoConvertUtils.convertObject(syOrgEntity,syOrgVo);
            listVo.add(syOrgVo);
        }

        List<SyOrgVo> listTree=rearrange(listVo);

        return  listTree;
    }

    @Override
    public List<SyOrgVo> getMyOrgTree() {
        SyUserEntity syUserEntity = syUserService.getOneFromCach(SecurityContext.GetCurTokenInfo().getAccount(), SecurityContext.GetCurTokenInfo().getEnterpriseId());
        SyOrgEntity syOrgEntityCurUser = baseMapper.selectById(syUserEntity.getOrgId());
        DataPrivilege userDataPrivilege = syUserService.getUserDataPrivilege(syUserEntity.getId());

        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.orderByAsc("path_code");
        queryWrapper.eq("enterprise_id", SecurityContext.GetCurTokenInfo().getEnterpriseId());
        if(userDataPrivilege.getValue()==DataPrivilege.LEVEL_ONLY.getValue()){
            queryWrapper.eq("path_code",syOrgEntityCurUser.getPathCode());
        }
        else if(userDataPrivilege.getValue()==DataPrivilege.LEVEL_AND_BELOW.getValue()){
            queryWrapper.likeRight("path_code",syOrgEntityCurUser.getPathCode());
        }
        List<SyOrgEntity> list = super.list(queryWrapper);

        List<SyOrgVo> listVo = new ArrayList<>();
        for(SyOrgEntity syOrgEntity:list){
            SyOrgVo syOrgVo=new SyOrgVo();
            VoConvertUtils.convertObject(syOrgEntity,syOrgVo);
            listVo.add(syOrgVo);
        }

        List<SyOrgVo> listTree=rearrange(listVo);

        return  listTree;
    }

    @Override
    public List<SyOrgVo> getMyOrgTreeWithMen() {
        List<SyOrgVo> syOrgVos=getMyOrgTree();
        //获取部门人员
        getOrgMen(syOrgVos);
        return syOrgVos;
    }

    private void getOrgMen(List<SyOrgVo> syOrgVos) {
        if (syOrgVos==null){
            return;
        }
        for(SyOrgVo syOrgVo:syOrgVos){
            if(syOrgVo.getType()==1){
                syOrgVo.setUsers(syUserService.getOrgUser(syOrgVo.getId()));
            }
            else {
                getOrgMen(syOrgVo.getChilds());
            }
        }
    }

    @Override
    /**
     * 根据数据权限获取当前用户可访问的org
     */
    public List<SyOrgEntity> getMyOrgList() {
        SyUserEntity syUserEntity = syUserService.getOneFromCach(SecurityContext.GetCurTokenInfo().getAccount(), SecurityContext.GetCurTokenInfo().getEnterpriseId());
        SyOrgEntity syOrgEntityCurUser = baseMapper.selectById(syUserEntity.getOrgId());
        DataPrivilege userDataPrivilege = syUserService.getUserDataPrivilege(syUserEntity.getId());

        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.orderByAsc("path_code");
        queryWrapper.eq("enterprise_id", SecurityContext.GetCurTokenInfo().getEnterpriseId());
        if(userDataPrivilege.getValue()==DataPrivilege.LEVEL_ONLY.getValue()){
            queryWrapper.eq("path_code",syOrgEntityCurUser.getPathCode());
            //queryWrapper.isNull("path_code");
        }
        else if(userDataPrivilege.getValue()==DataPrivilege.LEVEL_AND_BELOW.getValue()){
            queryWrapper.likeRight("path_code",syOrgEntityCurUser.getPathCode());
        }
        List<SyOrgEntity> list = super.list(queryWrapper);
        return  list;
    }

    @Override
    public boolean haveTopOrg(String enterpriseId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("enterprise_id", enterpriseId);
        queryWrapper.isNull("parent_id");
        SyOrgEntity syOrgEntity = super.getOne(queryWrapper);
        return  syOrgEntity!=null;
    }

    private List<SyOrgVo> rearrange(List<SyOrgVo> syOrgVos) {
        List<SyOrgVo> topOrg=new ArrayList<>();

        //赋值父节点
        for(SyOrgVo syOrgVo:syOrgVos){
            if(syOrgVo.getParentId()!=null){
                SyOrgVo parent=null;
                for(SyOrgVo syOrgVo2:syOrgVos){
                    if(syOrgVo2.getId().equals(syOrgVo.getParentId())){
                        parent=syOrgVo2;
                        break;
                    }
                }
                if(parent!=null){
                    List<SyOrgVo> childs = parent.getChilds();
                    if(childs==null){
                        childs=new ArrayList<>();
                        parent.setChilds(childs);
                    }
                    childs.add(syOrgVo);
                }
            }
            //顶层节点
            //(syOrgVo.getParentId()==null)
            else{
                topOrg.add(syOrgVo);
            }
        }
        return  topOrg;
    }
}
