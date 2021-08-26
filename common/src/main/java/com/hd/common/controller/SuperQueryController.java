package com.hd.common.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hd.common.PageQueryExpressionList;
import com.hd.common.utils.ParseQueryUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: liwei
 * @Description:
 */
public abstract  class SuperQueryController {

    public Map<String,String> mapQueryCols;
    public SuperQueryController(){
        mapQueryCols=new HashMap<String,String>();
    }

    /**
     * 查询vo属性和entity 属性 不一致时转化
     * @param pageQuery
     */
    protected void adaptiveQueryColumn(PageQueryExpressionList pageQuery){
        if(pageQuery.getQueryData()!=null){
            pageQuery.getQueryData().forEach(
                    pg -> {
                        if(mapQueryCols.containsKey(pg.getColumn())){
                            pg.setColumn(mapQueryCols.get(pg.getColumn()));
                        }
                    }
            );
        }
        if(pageQuery.getOrderby()!=null) {
            pageQuery.getOrderby().forEach(
                    ordy -> {
                        if (mapQueryCols.containsKey(ordy.getKey())) {
                            ordy.setKey(mapQueryCols.get(ordy.getKey()));
                        }
                    }
            );
        }
    }
    protected <T> Page<T> selectPage(PageQueryExpressionList pageQuery, IService iService){
        if(pageQuery==null) {
            pageQuery=new PageQueryExpressionList();
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        ParseQueryUtil.parseWhereSql(queryWrapper, pageQuery.getQueryData());
        ParseQueryUtil.parseOrderBySql(queryWrapper, pageQuery.getOrderby());
        Page<T> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        Page<T> p = (Page<T>) iService.page(page,queryWrapper);
        return p;
    }
}
