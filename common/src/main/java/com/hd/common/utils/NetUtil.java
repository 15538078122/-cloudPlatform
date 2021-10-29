package com.hd.common.utils;

/**
 * @Author: liwei
 * @Description:
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

public class NetUtil
{
    private static Logger logger = LoggerFactory.getLogger(NetUtil.class);
    public static ArrayList<String> getLocalIpAddr()
    {
        ArrayList<String> ipList = new ArrayList<String>();
        InetAddress[] addrList;
        try
        {
            Enumeration interfaces=NetworkInterface.getNetworkInterfaces();
            while(interfaces.hasMoreElements())
            {
                NetworkInterface ni=(NetworkInterface)interfaces.nextElement();
                Enumeration ipAddrEnum = ni.getInetAddresses();
                while(ipAddrEnum.hasMoreElements())
                {
                    InetAddress addr = (InetAddress)ipAddrEnum.nextElement();
                    if (addr.isLoopbackAddress() == true)
                    {
                        continue;
                    }

                    String ip = addr.getHostAddress();
                    if (ip.indexOf(":") != -1)
                    {
                        //skip the IPv6 addr
                        continue;
                    }

                    logger.debug("Interface: " + ni.getName()
                            + ", IP: " + ip);
                    ipList.add(ip);
                }
            }

            Collections.sort(ipList);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("Failed to get local ip list. " + e.getMessage());
            throw new RuntimeException("Failed to get local ip list");
        }

        return ipList;
    }

    public static void getLocalIpAddr(Set<String> set)
    {
        ArrayList<String> addrList = getLocalIpAddr();
        set.clear();
        for (String ip : addrList)
        {
            set.add(ip);
        }
    }
    static String noDefaultIpAddr="";
    public static String getNoDefaultIpAddr()
    {
        if(!noDefaultIpAddr.isEmpty()){
            return  noDefaultIpAddr;
        }

        //ArrayList<String> addrList = getLocalIpAddr();
        HashSet<String> addrSet = new HashSet<String>();
        getLocalIpAddr(addrSet);
        for (String ip : addrSet)
        {
           if(ip.compareTo("127.0.0.1")!=0){
               noDefaultIpAddr =  ip;
               return noDefaultIpAddr;
           }
        }
        noDefaultIpAddr = "127.0.0.1";
        return noDefaultIpAddr;
    }
}
