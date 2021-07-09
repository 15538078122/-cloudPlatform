package com.hd.microauservice.service.Impl;

import com.hd.microauservice.entity.SyMenuEntity;
import com.hd.microauservice.mapper.SyMenuMapper;
import com.hd.microauservice.service.SyMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
