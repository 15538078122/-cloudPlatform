package com.hd.microsysservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.microsysservice.entity.SyUrlMappingEntity;
import com.hd.microsysservice.mapper.SyUrlMappingMapper;
import com.hd.microsysservice.service.SyUrlMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liwei
 * @since 2021-07-08
 */
@Service
public class SyUrlMappingServiceImpl extends ServiceImpl<SyUrlMappingMapper, SyUrlMappingEntity> implements SyUrlMappingService {

    PathMatcher pathMatcher=new AntPathMatcher();

    @Autowired
    SyUrlMappingService syUrlMappingService;

    @Override
    public String getPermissionCode(String method, String uri) {
        List<SyUrlMappingEntity> urls = syUrlMappingService.getUrlTemplateList();
        String toMatchUrl=String.format("%s %s",method.toLowerCase(),uri);
        for(SyUrlMappingEntity syUrlMappingEntity:urls){
            boolean match = pathMatcher.match(syUrlMappingEntity.getUrl(), toMatchUrl);
            if(match){
                return syUrlMappingEntity.getPermCode();
            }
        }
        return null;
    }

    @Override
    @Cacheable(value = "lazyCache", key = "'urlstemplate'")
    public List<SyUrlMappingEntity> getUrlTemplateList() {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.select("url", "perm_code");
        List<SyUrlMappingEntity> list = list(queryWrapper);
        for(SyUrlMappingEntity syUrlMappingEntity:list){
            syUrlMappingEntity.setUrl(syUrlMappingEntity.getUrl().replaceAll("\\{[^}]*\\}","*"));
        }
        return list;
    }
}
