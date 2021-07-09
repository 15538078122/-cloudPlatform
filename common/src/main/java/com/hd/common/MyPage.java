package com.hd.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Author: liwei
 * @Description:    自定义返回page
 */
@Data
@AllArgsConstructor
public class MyPage<T> {
    private long pageNum;
    private long pageSize;
    private long total;
    private List<T> records;
}
