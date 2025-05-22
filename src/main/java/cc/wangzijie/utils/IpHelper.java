package cc.wangzijie.utils;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class IpHelper {
    public static String LOCAL_IP;
    public static String HOST_NAME;
    private static final String[] HEADERS = new String[]{"x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR", "X-Real-IP"};
    private static final String NETMASK = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    private static final Pattern IP_PATTERN = Pattern.compile("^(?:" + NETMASK + "\\.){3}" + NETMASK + "$");
    public static final String LOCALHOST_IP = "127.0.0.1";

    private static boolean isNotUnknown(String checkIp) {
        return StringUtils.isNotEmpty(checkIp) && !"unknown".equalsIgnoreCase(checkIp);
    }

    private static String getMultistageReverseProxyIp(String ip) {
        if (ip != null && ip.indexOf(",") > 0) {
            String[] ips = ip.trim().split(",");

            for(String subIp : ips) {
                if (isNotUnknown(subIp)) {
                    ip = subIp;
                    break;
                }
            }
        }
        return ip;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;

        for(String header : HEADERS) {
            String currentIp = request.getHeader(header);
            if (isNotUnknown(currentIp)) {
                ip = currentIp;
                break;
            }
        }

        if (null == ip) {
            ip = request.getRemoteAddr();
        }

        if (null == ip) {
            return "";
        } else {
            return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : getMultistageReverseProxyIp(ip);
        }
    }

    public static boolean isLocalIp(String ip) {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while(networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = networkInterfaces.nextElement();
                Enumeration<InetAddress> e2 = ni.getInetAddresses();

                while(e2.hasMoreElements()) {
                    InetAddress ia = e2.nextElement();
                    if (!(ia instanceof Inet6Address)) {
                        String address = ia.getHostAddress();
                        if (null != address && address.contains(ip)) {
                            return true;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            log.error("==== isLocalIp ==== check failed!", e);
        }

        return false;
    }

    static {
        StringBuilder ip = new StringBuilder();

        try {
            InetAddress inetAddr = InetAddress.getLocalHost();
            HOST_NAME = inetAddr.getHostName();
            byte[] addr = inetAddr.getAddress();

            for(int i = 0; i < addr.length; ++i) {
                if (i > 0) {
                    ip.append(".");
                }

                ip.append(addr[i] & 255);
            }
        } catch (UnknownHostException e) {
            ip = new StringBuilder("unknown");
            log.error(e.getMessage());
        } finally {
            LOCAL_IP = ip.toString();
        }

    }


    public static void main(String[] args) {
        List<String> ips = getServerIpList();
        System.out.println(ips);
    }

    /**
     * 获取本机的所有网卡IP列表
     * 不包含127.0.0.1
     *
     * @return 本机网卡的IP列表
     */
    public static List<String> getServerIpList() {
        List<String> serverIpList = new ArrayList<>();
        try {
            // 获取所有网络接口（网卡）
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            // 遍历每个网络接口
            for (NetworkInterface networkInterface : Collections.list(interfaces)) {
                // 跳过回环接口和未激活的接口
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }
                // 获取每个网络接口的 IP 地址
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    // 过滤掉IPv6地址（如果你只想要IPv4地址）
                    if (inetAddress instanceof java.net.Inet4Address) {
                        String ip = inetAddress.getHostAddress();
                        if (!LOCALHOST_IP.equals(ip) && isIPv4Valid(ip)) {
                            serverIpList.add(inetAddress.getHostAddress());
                        }
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException("获取服务器网卡IP异常", e);
        }
        return serverIpList;
    }


    public static boolean isIPv4Valid(String ip) {
        return IP_PATTERN.matcher(ip).matches();
    }
}
