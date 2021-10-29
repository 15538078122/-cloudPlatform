package com.hd.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.google.common.base.CaseFormat;
import com.hd.common.PageQueryExpressionList;
import com.hd.common.model.KeyValuePair;
import com.hd.common.model.QueryExpression;

import java.util.List;

/**
 * @Author: liwei
 * @Description:
 */

public class ParseQueryUtil {
    public static QueryWrapper parseWhereSql(QueryWrapper queryWrapper, List<QueryExpression> expressionList){
        if(expressionList!=null){
            for(QueryExpression condition : expressionList){
                switch (condition.getType()){
                    case "eq": queryWrapper.eq(condition.getCamelColumn(),condition.getValue());break;
                    case "ne": queryWrapper.ne(condition.getCamelColumn(),condition.getValue());break;
                    case "like": queryWrapper.like(condition.getCamelColumn(),condition.getValue());break;
                    case "leftlike": queryWrapper.likeLeft(condition.getCamelColumn(),condition.getValue());break;
                    case "rightlike": queryWrapper.likeRight(condition.getCamelColumn(),condition.getValue());break;
                    case "notlike": queryWrapper.notLike(condition.getCamelColumn(),condition.getValue());break;
                    case "gt": queryWrapper.gt(condition.getCamelColumn(),condition.getValue());break;
                    case "lt": queryWrapper.lt(condition.getCamelColumn(),condition.getValue());break;
                    case "ge": queryWrapper.ge(condition.getCamelColumn(),condition.getValue());break;
                    case "le": queryWrapper.le(condition.getCamelColumn(),condition.getValue());break;
                    default:;
                }
            }
        }

        return queryWrapper;
    }
    public static QueryWrapper parseOrderBySql( QueryWrapper queryWrapper,List<KeyValuePair> orderByList){
        if(orderByList!=null){
            for(KeyValuePair orderBy : orderByList){
                switch (orderBy.getValue()){
                    case "asc":  queryWrapper.orderByAsc(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, orderBy.getKey()));break;
                    case "desc": queryWrapper.orderByDesc(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, orderBy.getKey()));break;
                    default:;
                }
            }
        }

        return queryWrapper;
    }
    public static <T> Page<T> selectPage(PageQueryExpressionList pageQuery, IService iService){
        if(pageQuery==null) {
            pageQuery=new PageQueryExpressionList();
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        parseWhereSql(queryWrapper, pageQuery.getQueryData());
        parseOrderBySql(queryWrapper, pageQuery.getOrderby());
        Page<T> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        Page<T> p = (Page<T>) iService.page(page,queryWrapper);
        return p;
    }
}

