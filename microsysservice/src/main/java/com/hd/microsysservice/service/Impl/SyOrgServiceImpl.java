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
import com.hd.microsysservice.utils.VerifyUtil;
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
            VoConvertUtils.copyObjectProperties(syOrgEntity,syOrgVo);
            listVo.add(syOrgVo);
        }

        List<SyOrgVo> listTree=rearrange(listVo);

        return  listTree;
    }

    @Override
    public List<SyOrgVo> getMyOrgTree(Boolean includeDel) {
        SyUserEntity syUserEntity = syUserService.getUserByAccount(SecurityContext.GetCurTokenInfo().getAccount(), SecurityContext.GetCurTokenInfo().getEnterpriseId());
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
        if(!includeDel){
            queryWrapper.eq("delete_flag", 0);
        }
        List<SyOrgEntity> list = super.list(queryWrapper);

        List<SyOrgVo> listVo = new ArrayList<>();
        for(SyOrgEntity syOrgEntity:list){
            SyOrgVo syOrgVo=new SyOrgVo();
            VoConvertUtils.copyObjectProperties(syOrgEntity,syOrgVo);
            listVo.add(syOrgVo);
        }

        List<SyOrgVo> listTree=null;
        if(includeDel){
            listTree=listVo;
        }
        else {
            listTree = rearrange(listVo);
        }
        return  listTree;
    }

    @Override
    public List<SyOrgVo> getMyOrgTreeWithMen() {
        List<SyOrgVo> syOrgVos=getMyOrgTree(false);
        //获取部门人员
        getOrgMen(syOrgVos);
        return syOrgVos;
    }
    //user都填充到child
    @Override
    public void moveUserToChild(List<SyOrgVo> syOrgVos){
        if (syOrgVos==null){
            return;
        }
        for(SyOrgVo syOrgVo:syOrgVos){
            List<SyUserVo> users = syOrgVo.getUsers();
            if(users!=null){
                List<SyOrgVo> userForOrg=new ArrayList<>();
                for(SyUserVo syUserVo:users){
                    SyOrgVo syOrgVo1=new SyOrgVo(){{
                        setName(syUserVo.getName());
                        setId(syUserVo.getId());
                        setType((short) -1);
                    }};
                    userForOrg.add(syOrgVo1);
                }
                List<SyOrgVo> childs = syOrgVo.getChilds();
                if(childs!=null){
                    userForOrg.addAll(childs);
                }
                syOrgVo.setChilds(userForOrg);
                syOrgVo.setUsers(null);
            }
            moveUserToChild(syOrgVo.getChilds());
        }
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
        SyUserEntity syUserEntity = syUserService.getUserByAccount(SecurityContext.GetCurTokenInfo().getAccount(), SecurityContext.GetCurTokenInfo().getEnterpriseId());
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
    public Long createOrg(SyOrgVo syOrgVo) throws Exception {
        VerifyUtil.verifyEnterId(syOrgVo.getEnterpriseId());
        //TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        String pathCode=syOrgVo.getLevelCode();
        if(syOrgVo.getParentId()==null){
            //每个企业只能创建一个顶级
            if(haveTopOrg(syOrgVo.getEnterpriseId())){
                throw new Exception("顶级部门已存在!");
            }
            syOrgVo.setPathCode(syOrgVo.getLevelCode());
        }
        else {
            //判断部门名称重复
            QueryWrapper queryWrapper=new QueryWrapper(){{
                eq("name", syOrgVo.getName());
                eq("enterprise_id",syOrgVo.getEnterpriseId());
                eq("delete_flag",0);
            }};
            Assert.isTrue(null==getOne(queryWrapper),"部门名称不能重复!");
            DataPrivilege userDataPrivilege = syUserService.getUserDataPrivilege(Long.parseLong(SecurityContext.GetCurTokenInfo().getId()));
            Assert.isTrue(userDataPrivilege.getValue() < DataPrivilege.LEVEL_ONLY.getValue(),"数据权限不足!");
            //获取父编码
            String parentPathCode = getById(syOrgVo.getParentId()).getPathCode();
            pathCode=String.format("%s.%s",parentPathCode,pathCode);
        }

        String finalPathCode = pathCode;

        QueryWrapper queryWrapper=new QueryWrapper(){{
            eq("path_code", finalPathCode);
            eq("enterprise_id",syOrgVo.getEnterpriseId());
            ne("id",syOrgVo.getId());
        }};
        Assert.isTrue(null==getOne(queryWrapper),"路径编码不能重复!");

        SyOrgEntity syOrgEntity=new SyOrgEntity();
        syOrgVo.setPathCode(pathCode);
        VoConvertUtils.copyObjectProperties(syOrgVo,syOrgEntity);
        save(syOrgEntity);
        return syOrgEntity.getId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    public void delOrg(Long orgId) throws Exception {
        SyOrgEntity syOrgEntity=getById(orgId);
        VerifyUtil.verifyEnterId(syOrgEntity.getEnterpriseId());
        String pathCode=syOrgEntity.getLevelCode();
        if(syOrgEntity.getParentId()==null){
            //顶级不能删除
                throw new Exception("顶级部门不能删除!");
        }
        //部门下是否有人
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("org_id",syOrgEntity.getId());
        queryWrapper.eq("delete_flag", 0);
        Assert.isTrue(syUserService.list(queryWrapper).size()==0,"部门下有人员，不能删除!");

        //是否有子部门
        queryWrapper=new QueryWrapper();
        queryWrapper.eq("parent_id",syOrgEntity.getId());
        queryWrapper.eq("delete_flag", 0);
        Assert.isTrue(list(queryWrapper).size()==0,"部门下有子部门，不能删除!");

        deleteOrgRecursion(syOrgEntity);
    }

    @Override
    public List<SyUserVo> getMyOrgMen() {
        List<SyOrgVo> syOrgVos = getMyOrgTreeWithMen();
        List<SyUserVo> syUserVos =getMyOrgMenRecursion(syOrgVos);

        return syUserVos;
    }
    private  List<SyUserVo> getMyOrgMenRecursion( List<SyOrgVo> syOrgVos){
        List<SyUserVo> syUserVos =new ArrayList<SyUserVo>();
        for(SyOrgVo syOrgVo:syOrgVos){
            if(syOrgVo.getUsers()!=null){
                syUserVos.addAll(syOrgVo.getUsers());
            }
            if(syOrgVo.getChilds()!=null){
                syUserVos.addAll(getMyOrgMenRecursion(syOrgVo.getChilds()));
            }
        }
        return  syUserVos;
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
                else{
                    //如果父不存在，id不为null，也认为是顶级部门
                    topOrg.add(syOrgVo);
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
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    public void updateOrg(SyOrgVo syOrgVo) throws Exception {
        VerifyUtil.verifyEnterId(syOrgVo.getEnterpriseId());
        //syOrgVo.setEnterpriseId(SecurityContext.GetCurTokenInfo().getenterpriseId());
        SyOrgEntity syOrgEntityOld = getById(syOrgVo.getId());
        Assert.notNull(syOrgEntityOld,String.format("部门(id=%s)不存在!",syOrgEntityOld.getId()));

        String pathCode="";
        if(syOrgVo.getParentId()==null){
            //每个企业只能创建一个顶级
            if(haveTopOrg(syOrgVo.getEnterpriseId())){
                throw new Exception("顶级部门已存在!");
            }
            pathCode=syOrgVo.getLevelCode();
        }
        else {
            //判断部门名称重复
            QueryWrapper queryWrapper=new QueryWrapper(){{
                eq("name", syOrgVo.getName());
                eq("enterprise_id",syOrgVo.getEnterpriseId());
                eq("delete_flag",0);
                ne("id",syOrgVo.getId());
            }};
            Assert.isTrue(null==getOne(queryWrapper),"部门名称不能重复!");
            DataPrivilege userDataPrivilege = syUserService.getUserDataPrivilege(Long.parseLong(SecurityContext.GetCurTokenInfo().getId()));
            Assert.isTrue(userDataPrivilege.getValue() < DataPrivilege.LEVEL_ONLY.getValue(),"数据权限不足!");
            //获取父编码
            String parentPathCode = getById(syOrgVo.getParentId()).getPathCode();
            pathCode=String.format("%s.%s",parentPathCode,syOrgVo.getLevelCode());
        }
        syOrgVo.setPathCode(pathCode);


        //判断部门的父亲部门不可以是部门现有的子级部门
        Assert.isTrue(syOrgVo.getPathCode().indexOf(syOrgEntityOld.getPathCode())!=0
                ||syOrgVo.getPathCode().compareTo(syOrgEntityOld.getPathCode())==0,"不能选择当前部门的子级部门!");

        QueryWrapper queryWrapper=new QueryWrapper(){{
            eq("path_code", syOrgVo.getPathCode());
            eq("enterprise_id",syOrgVo.getEnterpriseId());
            ne("id",syOrgVo.getId());

        }};
        Assert.isTrue(null==getOne(queryWrapper),"路径编码不能重复!");

        SyOrgEntity syOrgEntityNew=new SyOrgEntity();
        VoConvertUtils.copyObjectProperties(syOrgVo,syOrgEntityNew);
        updateById(syOrgEntityNew);

        //更新关联编码
        if(syOrgEntityOld.getPathCode()==null || (syOrgEntityOld.getPathCode().compareTo(syOrgEntityNew.getPathCode())!=0) ){
            recurUpdatePathCode(syOrgEntityNew);
        }
    }

    private void recurUpdatePathCode(SyOrgEntity syOrgEntity) {
            //如果是目录，更新子级pathcode
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("parent_id",syOrgEntity.getId());
            List<SyOrgEntity> syOrgEntities = list(queryWrapper);
            for(SyOrgEntity item : syOrgEntities){
                SyOrgEntity syOrgEntityNew=new SyOrgEntity(){{
                    setId(item.getId());
                }};
                syOrgEntityNew.setPathCode(String.format("%s.%s",syOrgEntity.getPathCode(),item.getLevelCode()));
                updateById(syOrgEntityNew);
                recurUpdatePathCode(syOrgEntityNew);
            }
        }
}
