package com.hd.gateway.utils;

import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: liwei
 * @Description:
 */
public class LogUtil {
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
}
