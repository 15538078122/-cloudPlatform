package com.hd.common;

import com.hd.common.model.KeyValuePair;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: liwei
 * @Description:
 */

@Setter
@Getter
public class PageQuery<T> implements Serializable {
    // 页数 base 1
    protected Integer pageNum = 1;
    // 每页行数
    protected Integer pageSize = 20;
    // 查询参数
    protected T queryData;
    // 排序
    protected List<KeyValuePair> orderby;
}
