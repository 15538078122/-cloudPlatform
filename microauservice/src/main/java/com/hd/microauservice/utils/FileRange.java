package com.hd.microauservice.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Objects;

/**
 * @Author: liwei
 * @Description:
 */
public class FileRange {
    public   long start;
    public long end;
    public long length;
    public long total;

    /**
     * Range段构造方法.
     *
     * @param start range起始位置.
     * @param end   range结束位置.
     * @param total range段的长度.
     */
    public FileRange(long start, long end, long total) {
        this.start = start;
        this.end = end;
        this.length = end - start + 1;
        this.total = total;
    }

    public static long sublong(String value, int beginIndex, int endIndex) {
        String substring = value.substring(beginIndex, endIndex);
        return (substring.length() > 0) ? Long.parseLong(substring) : -1;
    }

    public static void copy(RandomAccessFile randomAccessFile, OutputStream output, long fileSize, long start, long length) throws IOException {
        byte[] buffer = new byte[4096];
        int read = 0;
        long transmitted = 0;
        if (fileSize == length) {
            randomAccessFile.seek(start);
            //需要下载的文件长度与文件长度相同，下载整个文件.
            while ((transmitted + read) <= length && (read = randomAccessFile.read(buffer)) != -1){
                output.write(buffer, 0, read);
                transmitted += read;
            }
            //处理最后不足buff大小的部分
            if(transmitted < length){
                read = randomAccessFile.read(buffer, 0, (int)(length - transmitted));
                output.write(buffer, 0, read);
            }
        } else {
            randomAccessFile.seek(start);
            long toRead = length;

            //如果需要读取的片段，比单次读取的4096小，则使用读取片段大小读取
            if(toRead < buffer.length){
                output.write(buffer, 0, randomAccessFile.read(new byte[(int) toRead]));
                return;
            }
            while ((read = randomAccessFile.read(buffer)) > 0) {
                toRead -= read;
                if (toRead > 0) {
                    output.write(buffer, 0, read);
                } else {
                    output.write(buffer, 0, (int) toRead + read);
                    break;
                }
            }

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        FileRange range = (FileRange) o;
        return start == range.start &&
                end == range.end &&
                length == range.length &&
                total == range.total;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return Objects.hash(prime, start, end, length, total);
    }
}
