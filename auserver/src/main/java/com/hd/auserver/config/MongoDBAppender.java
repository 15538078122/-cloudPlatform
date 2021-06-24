package com.hd.auserver.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.hd.auserver.utils.SpringUtil;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @Author: liwei
 * @Description:
 */
public class MongoDBAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    @Override
    protected void append(ILoggingEvent eventObject) {
        MongoTemplate mongoTemplate = SpringUtil.getBean(MongoTemplate.class);
        if (mongoTemplate != null) {
            final BasicDBObject doc = new BasicDBObject();
            doc.append("level", eventObject.getLevel().toString());
            //doc.append("logger", eventObject.getLoggerName());
            // doc.append("thread", eventObject.getThreadName());
            doc.append("message", eventObject.getFormattedMessage());
            mongoTemplate.insert(doc, "logs");
        }
    }
}
