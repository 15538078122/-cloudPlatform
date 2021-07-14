package com.hd.common.utils;
import java.io.IOException;
import java.lang.reflect.Type;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
/**
 * @Author: liwei
 * @Description:
 */

public class LongToStringSerializer implements ObjectSerializer {

    public static final LongToStringSerializer instance = new LongToStringSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
            throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
            return;
        }
        String strVal = object.toString();
        out.writeString(strVal);
    }
}
