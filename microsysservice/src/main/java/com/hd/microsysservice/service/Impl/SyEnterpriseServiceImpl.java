package com.hd.microsysservice.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.common.vo.SyMenuBtnVo;
import com.hd.common.vo.SyMenuVo;
import com.hd.microsysservice.entity.SyEnterpriseEntity;
import com.hd.microsysservice.entity.SyMenuBtnEntity;
import com.hd.microsysservice.entity.SyMenuEntity;
import com.hd.microsysservice.mapper.SyEnterpriseMapper;
import com.hd.microsysservice.service.SyEnterpriseService;
import com.hd.microsysservice.service.SyMenuBtnService;
import com.hd.microsysservice.service.SyMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
public class SyEnterpriseServiceImpl extends ServiceImpl<SyEnterpriseMapper, SyEnterpriseEntity> implements SyEnterpriseService {

    @Autowired
    SyMenuService syMenuService;
    @Autowired
    SyMenuBtnService syMenuBtnService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    public void createEnterprise(SyEnterpriseEntity syEnterpriseEntity) {
        //TODO: 创建企业，并复制root企业的菜单到当前企业
        save(syEnterpriseEntity);
        List<SyMenuVo> rootMenuVos = syMenuService.getAllMenu("root");
        rootMenuVos.forEach((menuvo)->{
            copyMenu(menuvo,syEnterpriseEntity.getEnterpriseId(),null);
        });
    }
    SyMenuService.SyMenuVoConvertUtils syMenuVoConvertUtils=new SyMenuService.SyMenuVoConvertUtils();
    /**
     * 递归复制menu
     * @param menuvo
     */
    private void copyMenu(SyMenuVo menuvo,String enterId,Long parentMenuId) {
        menuvo.setEnterpriseId(enterId);
        menuvo.setParentId(parentMenuId);
        menuvo.setId(null);
        SyMenuEntity syMenuEntity = syMenuVoConvertUtils.convertToT1(menuvo);
        syMenuService.save(syMenuEntity);
        if(menuvo.getType()==0){
            //目录,创建子目录
            List<SyMenuVo> childs = menuvo.getChilds();
            if(childs!=null){
                childs.forEach(m->{copyMenu(m,enterId,syMenuEntity.getId());});
            }

        }
        else {
            //菜单，创建菜单btns
            List<SyMenuBtnVo> btns = menuvo.getBtns();
            if(btns!=null){
                btns.forEach(btn->{
                    btn.setEnterpriseId(enterId);
                    btn.setMenuId(syMenuEntity.getId());
                    btn.setId(null);
                    SyMenuBtnEntity syMenuBtnEntity = syMenuBtnVoConvertUtils.convertToT1(btn);
                    syMenuBtnService.save(syMenuBtnEntity);
                });
            }
        }
    }
    SyMenuBtnService.SyMenuBtnVoConvertUtils syMenuBtnVoConvertUtils=new SyMenuBtnService.SyMenuBtnVoConvertUtils();

}
