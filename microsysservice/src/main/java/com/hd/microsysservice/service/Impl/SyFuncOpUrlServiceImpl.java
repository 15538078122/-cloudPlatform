package com.hd.microsysservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.common.vo.SyFuncOpUrlVo;
import com.hd.microsysservice.entity.SyFuncOpUrlEntity;
import com.hd.microsysservice.mapper.SyFuncOpUrlMapper;
import com.hd.microsysservice.service.SyFuncOpUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

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

    PathMatcher pathMatcher=new AntPathMatcher();

    @Autowired
    SyFuncOpUrlService syFuncOpUrlService;

    @Override
    public String getPermissionCode(String method, String uri) {
        List<SyFuncOpUrlEntity> urls = syFuncOpUrlService.getUrlTemplateList();
        String toMatchUrl=String.format("%s %s",method.toLowerCase(),uri);
        //先是普通匹配，然后是通配符匹配
        for(SyFuncOpUrlEntity syFuncOpUrlEntity:urls){
            if(syFuncOpUrlEntity.getUrl().indexOf('*')==-1){
                boolean match = (syFuncOpUrlEntity.getUrl().compareTo(toMatchUrl)==0);
                if(match){
                    return syFuncOpUrlEntity.getPermCode();
                }
            }
        }
        //通配符匹配
        for(SyFuncOpUrlEntity syFuncOpUrlEntity:urls){
            if(syFuncOpUrlEntity.getUrl().indexOf('*')!=-1){
                boolean match = pathMatcher.match(syFuncOpUrlEntity.getUrl(), toMatchUrl);
                if(match){
                    return syFuncOpUrlEntity.getPermCode();
                }
            }
        }
        return null;
    }

    @Override
    @Cacheable(value = "lazyCache", key = "'urlstemplate'")
    public List<SyFuncOpUrlEntity> getUrlTemplateList() {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.select("url", "perm_code");
        List<SyFuncOpUrlEntity> list = list(queryWrapper);
        for(SyFuncOpUrlEntity syFuncOpUrlEntity:list){
            syFuncOpUrlEntity.setUrl(syFuncOpUrlEntity.getUrl().replaceAll("\\{[^}]*\\}","*"));
        }
        return list;
    }

    @Override
    public List<SyFuncOpUrlVo> getfunOpUrl(Long funcOprId) {
        return baseMapper.getfunOpUrl(funcOprId);
    }
}
