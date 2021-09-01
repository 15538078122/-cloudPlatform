package com.hd.microsysservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hd.microsysservice.entity.SyDictEntity;
import com.hd.microsysservice.mapper.SyDictMapper;
import com.hd.microsysservice.service.SyDictItemService;
import com.hd.microsysservice.service.SyDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wli
 * @since 2021-07-30
 */
@Service
public class SyDictServiceImpl extends ServiceImpl<SyDictMapper, SyDictEntity> implements SyDictService {

    @Autowired
    SyDictService syDictService;
    @Autowired
    SyDictItemService syDictItemService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    public void removeDict(Long id) {
        syDictService.removeById(id);
        //删除项的值
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("dict_id",id);
        syDictItemService.remove(queryWrapper);
    }
}
