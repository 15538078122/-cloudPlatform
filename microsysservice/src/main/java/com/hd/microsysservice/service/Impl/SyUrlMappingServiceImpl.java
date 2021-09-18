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

}
