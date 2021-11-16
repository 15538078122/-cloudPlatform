package com.hd.microsysservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.common.model.TokenInfo;
import com.hd.common.vo.SyMenuBtnVo;
import com.hd.common.vo.SyMenuVo;
import com.hd.microsysservice.conf.SecurityContext;
import com.hd.microsysservice.entity.SyMenuBtnEntity;
import com.hd.microsysservice.entity.SyMenuEntity;
import com.hd.microsysservice.mapper.SyMenuMapper;
import com.hd.microsysservice.service.SyMenuBtnService;
import com.hd.microsysservice.service.SyMenuService;
import com.hd.microsysservice.utils.VerifyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 每项具备url的菜单都有一个隐含的menu_btn，代表查看本页面，使sy_role_perm表统一关联到sy_menu_btn表 服务实现类
 * </p>
 *
 * @author wli
 * @since 2021-07-09
 */
@Service
public class SyMenuServiceImpl extends ServiceImpl<SyMenuMapper, SyMenuEntity> implements SyMenuService {

    @Autowired
    SyMenuBtnService syMenuBtnService;
    @Autowired
    SyMenuService syMenuService;
    @Autowired
    RedisTemplate redisTemplate;

    SyMenuService.SyMenuVoConvertUtils syMenuVoConvertUtils=new SyMenuService.SyMenuVoConvertUtils();

    @Override
    public List<SyMenuVo> getAllMenu(String enterpriseId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("enterprise_id", enterpriseId);
        queryWrapper.orderByAsc("path_code");

        List<SyMenuEntity> list = super.list(queryWrapper);

        List<SyMenuVo> listVo = new ArrayList<>();
        for(SyMenuEntity syMenuEntity:list){
            SyMenuVo syMenuVo = syMenuVoConvertUtils.convertToT2(syMenuEntity);
            List<SyMenuBtnVo> btns = syMenuBtnService.getBtnsByMenuId(syMenuVo.getId(),true);
            if(btns.size()>0){
                syMenuVo.setBtns(btns);
            }
            listVo.add(syMenuVo);
        }

        List<SyMenuVo> listTree=rearrange(listVo);
        return  listTree;
    }

    @Override
    public List<SyMenuVo> getCurrentUserMenu() {
        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        List<SyMenuVo> listTree =syMenuService.getCurrentUserMenu(tokenInfo.getId(),tokenInfo.getEnterpriseId());
        return  listTree;
    }

    @Override
    @Cacheable(value = "userMenu", key = "#userId", unless = "#result == null")
    public List<SyMenuVo> getCurrentUserMenu(String userId, String enterpriseId) {
        //TODO: 根据权限获取菜单列表
        List<SyMenuEntity> list =baseMapper.selectUserMenu(userId,enterpriseId);
        List<SyMenuVo> listVo = new ArrayList<>();
//        for(SyMenuEntity syMenuEntity:list){
//            SyMenuVo syMenuVo = syMenuVoConvertUtils.convertToT2(syMenuEntity);
//            List<SyMenuBtnVo> btns = syMenuBtnService.getUserMenuBtns(Long.parseLong(userId),syMenuEntity.getId());
//            if(btns.size()>0){
//                syMenuVo.setBtns(btns);
//            }
//            listVo.add(syMenuVo);
//        }
        List<Long> menuIds=new ArrayList<>();
        for(SyMenuEntity syMenuEntity:list) {
            menuIds.add(syMenuEntity.getId());
        }
        List<SyMenuBtnVo> btnsAll = syMenuBtnService.getUserAllMenuBtns(Long.parseLong(userId),menuIds);
        for(SyMenuEntity syMenuEntity:list) {
            SyMenuVo syMenuVo = syMenuVoConvertUtils.convertToT2(syMenuEntity);
            List<SyMenuBtnVo> btns=new ArrayList<>();
             for(SyMenuBtnVo syMenuBtnVo:btnsAll){
                 if(syMenuBtnVo.getMenuId().equals(syMenuEntity.getId())){
                     btns.add(syMenuBtnVo);
                 }
             }
            syMenuVo.setBtns(btns);
            listVo.add(syMenuVo);
        }

        List<SyMenuVo> listTree=rearrange(listVo);
        return  listTree;
    }


    private List<SyMenuVo> rearrange(List<SyMenuVo> syMenuVos) {
        List<SyMenuVo> topMenu=new ArrayList<>();

        //赋值父节点
        for(SyMenuVo syMenuVo:syMenuVos){
            if(syMenuVo.getParentId()!=null){
                SyMenuVo parent=null;
                for(SyMenuVo syMenuVo2:syMenuVos){
                    if(syMenuVo2.getId().equals(syMenuVo.getParentId())){
                        parent=syMenuVo2;
                        break;
                    }
                }
                if(parent!=null){
                    List<SyMenuVo> childs = parent.getChilds();
                    if(childs==null){
                        childs=new ArrayList<>();
                        parent.setChilds(childs);
                    }
                    childs.add(syMenuVo);
                }
            }
            //查找顶层节点
             if(syMenuVo.getParentId()==null){
                topMenu.add(syMenuVo);
             }
        }
        return  topMenu;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    public void createMenu(SyMenuEntity syMenuEntity) {
        //首先设置pathcode
        if(syMenuEntity.getParentId()==null){
            syMenuEntity.setPathCode(syMenuEntity.getLevelCode());
        }
        else {
            String parentPathCode = getById(syMenuEntity.getParentId()).getPathCode();
            syMenuEntity.setPathCode(String.format("%s.%s",parentPathCode,syMenuEntity.getLevelCode()));
        }
        save(syMenuEntity);
        if(syMenuEntity.getType()==1){
            //如果是菜单，先创建菜单的默认list按钮，有此按钮的授权才会显示其对应的菜单
//            QueryWrapper queryWrapper = new QueryWrapper();
//            queryWrapper. eq("path_code", syMenuEntity.getPathCode());
//            syMenuEntity = baseMapper.selectOne(queryWrapper);
            SyMenuBtnEntity syMenuBtnEntity=new SyMenuBtnEntity();
            syMenuBtnEntity.setMenuId(syMenuEntity.getId());
            syMenuBtnEntity.setIsVisible(false);
            syMenuBtnEntity.setEnterpriseId(syMenuEntity.getEnterpriseId());
            syMenuBtnEntity.setName("list");
            syMenuBtnService.save(syMenuBtnEntity);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    public void deleteMenu(Long menuId) {
        //移除菜单及其子菜单、对应的按钮
        SyMenuEntity syMenuEntity = getById(menuId);
        deleteMenuRecursion(syMenuEntity);
    }

//    @Override
//    public void update(SyMenuEntity syMenuEntity) {
//        //首先设置pathcode
//        if(syMenuEntity.getParentId()==null){
//            syMenuEntity.setPathCode(syMenuEntity.getLevelCode());
//        }
//        else {
//            String parentPathCode = getById(syMenuEntity.getParentId()).getPathCode();
//            syMenuEntity.setPathCode(String.format("%s.%s",parentPathCode,syMenuEntity.getLevelCode()));
//        }
//        updateById(syMenuEntity);
//    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    public void updateMenu(Long menuId, SyMenuVo syMenuVo) throws Exception {
        VerifyUtil.verifyEnterId(syMenuVo.getEnterpriseId());
        SyMenuEntity syMenuEntityOld= getById(syMenuVo.getId());
        if(syMenuEntityOld==null){
            throw  new Exception(String.format("菜单(id=%s)不存在!",syMenuVo.getId()));
        }
        //更新关联编码
        SyMenuEntity syMenuEntityNew = syMenuVoConvertUtils.convertToT1(syMenuVo);
        if(syMenuEntityOld.getPathCode().compareTo(syMenuEntityNew.getPathCode())!=0){
            recurUpdatePathCode(syMenuEntityNew);
        }
        updateById(syMenuEntityNew);

        Set<String> keys = redisTemplate.keys(String.format("%s::%s", "userMenu", "*"));
        redisTemplate.delete(keys);
    }

    private void recurUpdatePathCode(SyMenuEntity syMenuEntity) {
        if(syMenuEntity.getType()==0){
            //如果是目录，更新子级pathcode
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("parent_id",syMenuEntity.getId());
            List<SyMenuEntity> syMenuEntities = list(queryWrapper);
            for(SyMenuEntity item : syMenuEntities){
                SyMenuEntity syMenuEntityNew=new SyMenuEntity(){{
                   setId(item.getId());
                   setIsVisible(item.getIsVisible());
                   setType(item.getType());
                }};
                syMenuEntityNew.setPathCode(String.format("%s.%s",syMenuEntity.getPathCode(),item.getLevelCode()));
                updateById(syMenuEntityNew);
                recurUpdatePathCode(syMenuEntityNew);
            }
        }
    }

    private void deleteMenuRecursion(SyMenuEntity syMenuEntity){
        removeById(syMenuEntity.getId());
        if(syMenuEntity.getType()==0){
            //目录，删除子目录
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("parent_id",syMenuEntity.getId());
            List<SyMenuEntity> syMenuEntities = list(queryWrapper);
            for(SyMenuEntity item : syMenuEntities){
                deleteMenuRecursion(item);
            }
        }
        else {
            //菜单，删除子按钮
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("menu_id",syMenuEntity.getId());
            List<SyMenuBtnEntity> syMenuBtnEntities = syMenuBtnService.list(queryWrapper);
            for(SyMenuBtnEntity item : syMenuBtnEntities){
                syMenuBtnService.removeById(item.getId());
            }
        }
    }
}
