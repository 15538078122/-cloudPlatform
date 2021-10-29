package com.hd.microsysservice.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import com.hd.common.MyPage;
import com.hd.common.PageQueryExpressionList;
import com.hd.common.model.KeyValuePair;
import com.hd.common.model.QueryExpression;
import com.hd.common.vo.SyDictItemVo;
import com.hd.microsysservice.entity.SyDictItemEntity;
import com.hd.microsysservice.mapper.SyDictItemMapper;
import com.hd.microsysservice.service.SyDictItemService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wli
 * @since 2021-07-30
 */
@Service
public class SyDictItemServiceImpl extends ServiceImpl<SyDictItemMapper, SyDictItemEntity> implements SyDictItemService {

    @Override
    public MyPage<SyDictItemVo> dictItembycode(String code,String enterpriseId,String ordrby,Integer pageNum,Integer pageSize){
        //自定义分页查询
        Page page=new Page(pageNum,pageSize,true);
        List<SyDictItemVo> syDictItemVoList = baseMapper.dictItembycode(enterpriseId,code,ordrby,page);
        return new MyPage<>(page.getCurrent(),page.getSize(),page.getTotal(),syDictItemVoList);
    }
    @Override
    public MyPage<SyDictItemVo> dictItembycode(PageQueryExpressionList pageQueryExpressionList) {
        //提取code
        String code="";
        String enterpriseId="";
        for(QueryExpression condition : pageQueryExpressionList.getQueryData()){
            if(condition.getColumn().compareTo("code")==0){
                code=condition.getValue();
            }
            if(condition.getColumn().compareTo("enterpriseId")==0){
                enterpriseId = condition.getValue();
            }
        }
        String orderby="";
        //提取order
        for(KeyValuePair keyValuePair : pageQueryExpressionList.getOrderby()){
            if(orderby.compareTo("")!=0){
                orderby+=",";
            }
            orderby+=String.format("%s %s", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, keyValuePair.getKey()),keyValuePair.getValue());
        }
        if(orderby.compareTo("")!=0){
            orderby="order by "+orderby;
        }
        //自定义分页查询
        MyPage<SyDictItemVo> syDictItemVoMyPage =  dictItembycode(code,enterpriseId,orderby,pageQueryExpressionList.getPageNum(),pageQueryExpressionList.getPageSize());

        return syDictItemVoMyPage;
    }
}
