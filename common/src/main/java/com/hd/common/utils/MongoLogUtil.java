package com.hd.common.utils;

import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: liwei
 * @Description:
 */
public class MongoLogUtil {
    public static void logCostTime(String uri,long cost){
        MongoTemplate mongoTemplate = SpringUtil.getBean(MongoTemplate.class);
        if (mongoTemplate != null) {
            final BasicDBObject doc = new BasicDBObject();
            Date date = new Date();
            doc.append("tm", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(date));
            doc.append("uri", uri);
            doc.append("cost", cost);
            mongoTemplate.insert(doc, "cost");
        }
    }

    public static void logOp(String enterpriseId,String operModul,String operType,String operDesc,String remoteHost
            ,String requestURI,String account,String params){
        MongoTemplate mongoTemplate = SpringUtil.getBean(MongoTemplate.class);
        if (mongoTemplate != null) {
            final BasicDBObject doc = new BasicDBObject();
            Date date = new Date();
            doc.append("tm", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(date));
            doc.append("enterId",enterpriseId);
            doc.append("operModul", operModul);
            doc.append("operType", operType);
            doc.append("operDesc", operDesc);
            doc.append("remoteHost", remoteHost);
            doc.append("requestURI", requestURI);
            doc.append("account", account);
            doc.append("params", params);
            mongoTemplate.insert(doc, "operators");
        }
    }

    public static void logErrorRequest(String enterpriseId,String operModul,String operType,String operDesc
            ,String remoteHost, String requestURI, String account, String params, String stackTraceToString) {
        MongoTemplate mongoTemplate = SpringUtil.getBean(MongoTemplate.class);
        if (mongoTemplate != null) {
            final BasicDBObject doc = new BasicDBObject();
            Date date = new Date();
            doc.append("tm", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(date));
            doc.append("enterId",enterpriseId);
            doc.append("operModul", operModul);
            doc.append("operType", operType);
            doc.append("operDesc", operDesc);
            doc.append("remoteHost", remoteHost);
            doc.append("requestURI", requestURI);
            doc.append("account", account);
            doc.append("params", params);
            doc.append("err", stackTraceToString);
            mongoTemplate.insert(doc, "operators");
        }
    }
}
