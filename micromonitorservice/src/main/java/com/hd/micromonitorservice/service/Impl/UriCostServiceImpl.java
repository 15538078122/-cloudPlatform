package com.hd.micromonitorservice.service.Impl;

import com.hd.common.MyPage;
import com.hd.common.vo.UriCostVo;
import com.hd.micromonitorservice.entity.UriCost;
import com.hd.micromonitorservice.service.UriCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: liwei
 * @Description:
 */
@Service
public class UriCostServiceImpl implements UriCostService {
    @Autowired
    private MongoTemplate template;

    @Override
    public MyPage<UriCostVo> getMaxCost2Sec(long pageNum, long pageSize) {
        Query query = new Query();
        //最近10分钟的
        query.addCriteria(Criteria.where("tm").gt(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date((new Date()).getTime() - 10 * 60 * 1000))
                )
        );
        long total=template.count(query, UriCost.class);
        if(total<=pageSize){
            pageNum=1;
            pageSize= total;
        }
        else{
            long actualPage= total/pageSize+(total%pageSize==0?0:1);
            if(pageNum>actualPage){
                pageNum=actualPage;
            }
        }
        query.with(Sort.by(
                Sort.Order.desc("cost")
        ));
        query.skip((pageNum - 1) * pageSize);
        query.limit((int) pageSize);
        //query.getSortObject().append("cost", -1);
        //List<Map> mm = template.findAll(Map.class,"cost");
        List<UriCost> uriCosts = template.find(query, UriCost.class);
        List<UriCostVo> uriCostVos=new UriCostVoConvertUtils().convertToListT2(uriCosts);
//        for(UriCost uriCost:uriCosts){
//            UriCostVo uriCostVo=new UriCostVo();
//            VoConvertUtils.convertObject(uriCost,uriCostVo);
//            uriCostVos.add(uriCostVo);
//        }
        return new MyPage<>(pageNum,pageSize,total,uriCostVos);
    }
    @Override
    public MyPage<UriCostVo> getAverageCost2Sec(long pageNum, long pageSize) {
        //最近10分钟的
        Criteria criteria = Criteria.where("tm").gt(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date((new Date()).getTime() - 10 * 60 * 1000))
                );

        Aggregation agg = Aggregation.newAggregation(Aggregation.match(criteria)
                                                    ,Aggregation.group("uri").first("uri").as("uri")
                                                    );

        AggregationResults<UriCost> results = template.aggregate(agg, "cost",UriCost.class);
        List<UriCost> uriCosts = results.getMappedResults();
        long total=uriCosts.size();

        if(total<=pageSize){
            pageNum=1;
            pageSize= total;
        }
        else{
            long actualPage= total/pageSize+(total%pageSize==0?0:1);
            if(pageNum>actualPage){
                pageNum=actualPage;
            }
        }
        agg = Aggregation.newAggregation(
                Aggregation.match(criteria)
                ,Aggregation.group("uri").first("uri").as("uri").sum("cost").as("totalCost").avg("cost").as("avgcost").count().as("count")
                ,Aggregation.sort(Sort.by(Sort.Order.desc("avgcost")))
                ,Aggregation.skip((pageNum - 1) * pageSize)
                ,Aggregation.limit((int) pageSize)
        );
        results = template.aggregate(agg, "cost",UriCost.class);
        uriCosts = results.getMappedResults();
        List<UriCostVo> uriCostVos= new UriCostVoConvertUtils().convertToListT2(uriCosts);

        return new MyPage<>(pageNum,pageSize,total,uriCostVos);
    }

    @Override
    public UriCost save(UriCost uriCost) {
        return template.save(uriCost);
    }
}
