package com.hd.gateway.conf;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.hd.common.utils.SpringUtil;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: liwei
 * @Description:
 */
public class MongoDBAppender  extends UnsynchronizedAppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent eventObject) {
        MongoTemplate mongoTemplate = SpringUtil.getBean(MongoTemplate.class);
        if (mongoTemplate != null) {
            final BasicDBObject doc = new BasicDBObject();
            Date date = new Date();
            date.setTime(eventObject.getTimeStamp());
            //System.out.println(new SimpleDateFormat().format(date));
            doc.append("tm", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(date));
            doc.append("level", eventObject.getLevel().toString());
            //doc.append("thread", eventObject.getThreadName());
            doc.append("logger", eventObject.getLoggerName());
            doc.append("msg", eventObject.getFormattedMessage());
            mongoTemplate.insert(doc, "logs");
        }
    }
}
