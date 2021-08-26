package com.hd.microauservice.service.Impl;

import com.hd.microauservice.entity.SyFuncOpUrlEntity;
import com.hd.microauservice.mapper.SyFuncOpUrlMapper;
import com.hd.microauservice.service.SyFuncOpUrlService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
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
public class SyFuncOpUrlServiceImpl extends ServiceImpl<SyFuncOpUrlMapper, SyFuncOpUrlEntity> implements SyFuncOpUrlService {

    @Override
    @Cacheable(value = "userpermission", key = "''+#userId")
    public List<String> selectUserPerm(Long userId) {
        List<String> perms=new ArrayList<>();
        List<SyFuncOpUrlEntity> syFuncOpUrlEntities = baseMapper.selectUserPerm(userId);
        for(SyFuncOpUrlEntity item:syFuncOpUrlEntities){
            perms.add(item.getPermCode());
        }
        return perms;
    }
}
