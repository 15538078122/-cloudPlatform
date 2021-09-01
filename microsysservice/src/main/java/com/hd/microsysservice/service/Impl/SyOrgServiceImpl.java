package com.hd.microsysservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.common.model.DataPrivilege;
import com.hd.common.vo.SyOrgVo;
import com.hd.common.vo.SyUserVo;
import com.hd.microsysservice.conf.SecurityContext;
import com.hd.microsysservice.entity.SyOrgEntity;
import com.hd.microsysservice.entity.SyUserEntity;
import com.hd.microsysservice.mapper.SyOrgMapper;
import com.hd.microsysservice.service.SyOrgService;
import com.hd.microsysservice.service.SyUserService;
import com.hd.microsysservice.utils.EnterpriseVerifyUtil;
import com.hd.microsysservice.utils.VoConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
        queryWrapper.eq("delete_flag", 0);
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
        queryWrapper.eq("delete_flag", 0);
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
            //if(syOrgVo.getType()==1){
                syOrgVo.setUsers(syUserService.getOrgUser(syOrgVo.getId()));
            //}
            //else {
                getOrgMen(syOrgVo.getChilds());
            //}
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
        queryWrapper.eq("delete_flag", 0);
        queryWrapper.isNull("parent_id");
        SyOrgEntity syOrgEntity = super.getOne(queryWrapper);
        return  syOrgEntity!=null;
    }

    @Override
    public void createOrg(SyOrgVo syOrgVo) throws Exception {
        EnterpriseVerifyUtil.verifyEnterId(syOrgVo.getEnterpriseId());
        //TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        if(syOrgVo.getParentId()==null){
            //每个企业只能创建一个顶级
            if(haveTopOrg(syOrgVo.getEnterpriseId())){
                throw new Exception("顶级部门已存在!");
            }
        }

        SyOrgEntity syOrgEntity=new SyOrgEntity();
        VoConvertUtils.convertObject(syOrgVo,syOrgEntity);
        save(syOrgEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    public void delOrg(Long orgId) {
        SyOrgEntity syOrgEntity=getById(orgId);
        EnterpriseVerifyUtil.verifyEnterId(syOrgEntity.getEnterpriseId());
        deleteOrgRecursion(syOrgEntity);
    }

    @Override
    public List<SyUserVo> getMyOrgMen() {
        List<SyOrgVo> syOrgVos = getMyOrgTreeWithMen();
        List<SyUserVo> syUserVos =new ArrayList<SyUserVo>();
        for(SyOrgVo syOrgVo:syOrgVos){
            if(syOrgVo.getUsers()!=null){
                syUserVos.addAll(syOrgVo.getUsers());
            }
        }
        return syUserVos;
    }

    private void deleteOrgRecursion(SyOrgEntity syOrgEntity) {
        removeById(syOrgEntity.getId());
        //删除部门下的人
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("org_id",syOrgEntity.getId());
        queryWrapper.eq("delete_flag", 0);
        syUserService.remove(queryWrapper);

        //删除子部门
        queryWrapper=new QueryWrapper();
        queryWrapper.eq("parent_id",syOrgEntity.getId());
        List<SyOrgEntity> syOrgEntities = list(queryWrapper);
        for(SyOrgEntity item : syOrgEntities){
            deleteOrgRecursion(item);
        }
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
