package com.hd.microauservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hd.common.vo.SyMenuBtnVo;
import com.hd.common.vo.SyMenuVo;
import com.hd.microauservice.conf.SecurityContext;
import com.hd.microauservice.entity.SyMenuEntity;
import com.hd.microauservice.mapper.SyMenuMapper;
import com.hd.microauservice.service.SyMenuBtnService;
import com.hd.microauservice.service.SyMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.microauservice.utils.VoConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<SyMenuVo> getCurrentUserMenu() {
        //TODO: 根据权限获取菜单列表
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("enterprise_id", SecurityContext.GetCurTokenInfo().getCompanyCode());
        queryWrapper.orderByAsc("path_code");

        List<SyMenuEntity> list = super.list(queryWrapper);

        List<SyMenuVo> listVo = new ArrayList<>();
        for(SyMenuEntity syMenuEntity:list){
            SyMenuVo syMenuVo = VoConvertUtils.syMenuToVo(syMenuEntity);
            List<SyMenuBtnVo> btns = syMenuBtnService.getBtnsByMenuId(syMenuVo.getId());
            if(btns.size()>0){
                syMenuVo.setBtns(btns);
            }
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
}
