package com.hd.common.utils;

import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @Author: liwei
 * @Description:
 */
public class LicenseUtil {
    private static String getMac() {
        try {
            Enumeration<NetworkInterface> el = NetworkInterface
                    .getNetworkInterfaces();
            while (el.hasMoreElements()) {
                byte[] mac = el.nextElement().getHardwareAddress();
                if (mac == null)
                    continue;
                String hexstr = StringUtil.byteArrToHex(mac);
                return StringUtil.getSplitString(hexstr, "-", 2).toUpperCase();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }



    public static String getMachineCode() throws Exception{
        Set<String> result = new HashSet<>();
        String mac = getMac();
        result.add(mac);
        Properties props = System.getProperties();
        String javaVersion = props.getProperty("java.version");
        result.add(javaVersion);
        String javaVMVersion = props.getProperty("java.vm.version");
        result.add(javaVMVersion);
        String osVersion = props.getProperty("os.version");
        result.add(osVersion);

        return result.toString();
    }
}
