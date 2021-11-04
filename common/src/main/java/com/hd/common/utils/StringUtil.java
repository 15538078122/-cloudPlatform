package com.hd.common.utils;

/**
 * @Author: liwei
 * @Description:
 */
public class StringUtil {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String byteArrToHex(byte... bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    public static String getSplitString(String str, String splitChar, int countPerGroup) {
        StringBuffer stringBuffer=new StringBuffer();
        for(int i=0;i<str.length(); i+=countPerGroup){
            if(i>0){
                stringBuffer.append(splitChar);
            }
            if(i+countPerGroup<str.length())
            {
                stringBuffer.append(str.substring(i,i+countPerGroup));
            }
            else {
                stringBuffer.append(str.substring(i,str.length()));
            }
        }
        return stringBuffer.toString();
    }
}
