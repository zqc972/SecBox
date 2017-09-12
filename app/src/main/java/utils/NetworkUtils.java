package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import db.MacInfo;

/**
 * Created by zhaoqc on 17-7-29.
 */

public class NetworkUtils {
    public static boolean isNetworkAvailable(Context context) {
        if(context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mWiFiNetworkInfo != null && mWiFiNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int getNetworkType(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            int type = networkInfo.getType();

            if(type == ConnectivityManager.TYPE_WIFI)
                Log.i("NetworkUtils","getNetworkType(): WIFI");
            else if(type == ConnectivityManager.TYPE_MOBILE) {
                //===============test==================
                int subtype = networkInfo.getSubtype();
                switch (subtype) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        Log.i("NetworkUtils","getNetworkType(): 2G");
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        Log.i("NetworkUtils","getNetworkType(): 3G");
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        Log.i("NetworkUtils","getNetworkType(): 4G");
                        break;
                }
                //=====================================
            } else {
                Log.i("NetworkUtils","getNetworkType: other type");
            }


            return type;
        }
        return -1;
    }

    public static int getIp(Context context) {
        int network_tpye = getNetworkType(context);
        if(network_tpye != -1 && network_tpye == ConnectivityManager.TYPE_MOBILE) {
//            try {
//                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
//                    NetworkInterface intf = en.nextElement();
//                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
//                        InetAddress inetAddress = enumIpAddr.nextElement();
//                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
//                            return inetAddress.getHostAddress();
//                        }
//                    }
//                }
//            } catch (SocketException e) {
//                e.printStackTrace();
//            }
            Log.i("NetworkUtils","getIp(),ip address:");
        } else if(network_tpye != -1 && network_tpye == ConnectivityManager.TYPE_WIFI){
            WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            int ip_address = manager.getDhcpInfo().ipAddress;
            Log.i("NetworkUtils","getIp(),ip address:" + ip_address);
            return ip_address;
        }
        return -1;
    }

    public static int getNetmask(Context context) {
        int network_tpye = getNetworkType(context);
        if(network_tpye != -1 && network_tpye == ConnectivityManager.TYPE_MOBILE) {
            Log.i("NetworkUtils","getNetmask(),mobile network can't get netmask");
        } else if(network_tpye != -1 && network_tpye == ConnectivityManager.TYPE_WIFI){
            WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            int ip_address = manager.getDhcpInfo().netmask;
            Log.i("NetworkUtils","getNetmask(),netmask:" + ip_address);
            return ip_address;
        }
        return -1;
    }

    public static int getGateway(Context context) {
        int network_tpye = getNetworkType(context);
        if(network_tpye != -1 && network_tpye == ConnectivityManager.TYPE_MOBILE) {
            Log.w("NetworkUtils","getGateway(),mobile network can't get gateway");
        } else if(network_tpye != -1 && network_tpye == ConnectivityManager.TYPE_WIFI){
            WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            int ip_address = manager.getDhcpInfo().gateway;
            Log.i("NetworkUtils","getGateway(),gateway :" + ip_address);
            return ip_address;
        }
        return -1;
    }

    public static int getDns1(Context context) {
        int network_tpye = getNetworkType(context);
        if(network_tpye != -1 && network_tpye == ConnectivityManager.TYPE_MOBILE) {
            Log.i("NetworkUtils","getDns1(),mobile network can't get dns1");
        } else if(network_tpye != -1 && network_tpye == ConnectivityManager.TYPE_WIFI){
            WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            int ip_address = manager.getDhcpInfo().dns1;
            Log.i("NetworkUtils","getDns1(),dns1:" + ip_address);
            return ip_address;
        }
        return -1;
    }

    public static int getDns2(Context context) {
        int network_tpye = getNetworkType(context);
        if(network_tpye != -1 && network_tpye == ConnectivityManager.TYPE_MOBILE) {
            Log.i("NetworkUtils","getDns2(),mobile network can't get dns2");
        } else if(network_tpye != -1 && network_tpye == ConnectivityManager.TYPE_WIFI){
            WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            int ip_address = manager.getDhcpInfo().dns2;
            Log.i("NetworkUtils","getDns2(),dns2:" + ip_address);
            return ip_address;
        }
        return -1;
    }

    public static String getMac(Context context) {
        int network_tpye = getNetworkType(context);
        String mac = null;
        if(network_tpye != -1 && network_tpye == ConnectivityManager.TYPE_MOBILE) {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                            mac = intf.getHardwareAddress().toString();
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
            Log.i("NetworkUtils","getMac(),mac address:" + mac);
        } else if(network_tpye != -1 && network_tpye == ConnectivityManager.TYPE_WIFI){
//            ==========   can not use while android sdk >= 23  ==========
//            WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
//            WifiInfo wifiInfo = manager.getConnectionInfo();
//            mac = wifiInfo.getMacAddress();
//            ============================================================
            Enumeration<NetworkInterface> interfaces = null;
            try {
                interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface netWork = interfaces.nextElement();
                    // 如果存在硬件地址并可以使用给定的当前权限访问，则返回该硬件地址（通常是 MAC）。
                    byte[] by = netWork.getHardwareAddress();
                    if (by == null || by.length == 0) {
                        continue;
                    }
                    StringBuilder builder = new StringBuilder();
                    for (byte b : by) {
                        builder.append(String.format("%02X:", b));
                    }
                    if (builder.length() > 0) {
                        builder.deleteCharAt(builder.length() - 1);
                    }

                    // 从路由器上在线设备的MAC地址列表，可以印证设备Wifi的 name 是 wlan0
                    if (netWork.getName().equals("wlan0")) {
                        mac = builder.toString();
                        Log.d("TEST_BUG", " interfaceName ="+netWork.getName()+", mac="+mac);
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
            Log.i("NetworkUtils","getMac(),mac address:" + mac);
        }
        return mac;
    }

    public static String IpToString(int ipAddress) {
        return new StringBuilder()
                .append((ipAddress & 0xff)).append('.')
                .append((ipAddress >> 8) & 0xff).append('.')
                .append((ipAddress >> 16) & 0xff).append('.')
                .append(((ipAddress >> 24)& 0xff))
                .toString();
    }

    /**
     * 计算子网主机数目
     *
     * @param int_net_mask
     * @return
     */
    public static int countHost(int int_net_mask) {
        int n = ~int_net_mask;
        return ((n >> 24) & 0xff) + (((n >> 16) & 0xff) << 8)
                + (((n >> 8) & 0xff) << 16) + ((n & 0xff) << 24);
    }

    public static int getFirstIp(int ip , int netmask) {
        return getNextIp(ip & netmask);
    }

    /**
     * 查找下一个IP
     *
     * @param int_ip
     * @return
     */
    public static int getNextIp(int int_ip) {
        int next_ip = -1;
        byte[] ip_byte = intIpToByte(int_ip);
        int i = ip_byte.length - 1;

        while (i >= 0 && ip_byte[i] == (byte) 0xff) {
            ip_byte[i] = 0;
            i--;
        }
        if (i >= 0)
            ip_byte[i]++;
        else
            return next_ip;
        next_ip = byteIpToInt(ip_byte);
        return next_ip;
    }

    /**
     * Int类型IP转成byte数组
     *
     * @param int_ip
     * @return
     */
    public static byte[] intIpToByte(int int_ip) {
        byte[] ip_byte = new byte[4];

        ip_byte[0] = (byte) (int_ip & 0xff);
        ip_byte[1] = (byte) (0xff & int_ip >> 8);
        ip_byte[2] = (byte) (0xff & int_ip >> 16);
        ip_byte[3] = (byte) (0xff & int_ip >> 24);

        return ip_byte;
    }

    /**
     * byte数组Ip转成Int类型Ip
     *
     * @param ip_byte
     * @return
     */
    public static int byteIpToInt(byte[] ip_byte) {
        return ((ip_byte[3] & 0xff) << 24) + ((ip_byte[2] & 0xff) << 16)
                + ((ip_byte[1] & 0xff) << 8) + (ip_byte[0] & 0xff);
    }

    public static String getMacFromArpTable(String ip) {
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader("/proc/net/arp");
            br = new BufferedReader(fr);
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                //排除杂项
                if(line.contains("00:00:00:00:00:00"))
                    continue;

                String[] temp = line.split("\\s+");
                boolean flag = false;
                for(String s : temp) {
                    if(s.contains(".") && s.equals(ip))
                        flag = true;
                    if(s.contains(":") && flag )
                        return s;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "未能成功获取mac地址";
    }

    public static String getManufacturerByMac(String macAddress) {
        String mac = macAddress.toUpperCase();
        mac = mac.replace(":","");
        mac = mac.substring(0,6);
        MacInfo info = DataSupport.where("mac = ?",mac).findFirst(MacInfo.class);
        if(info != null) {
            Log.i("Manufacturer",info.getManufacturer());
            return info.getManufacturer();
        }
        else
            return "未知制造商";
    }
}
