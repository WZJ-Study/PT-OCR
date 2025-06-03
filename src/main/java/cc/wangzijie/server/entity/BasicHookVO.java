package cc.wangzijie.server.entity;

import cc.wangzijie.utils.DateUtils;
import cc.wangzijie.utils.IpHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BasicHookVO {

    public static BasicHookVO of(String localIp) {
        BasicHookVO vo = new BasicHookVO();
        vo.setIpAddr(localIp);
        vo.setHostName(IpHelper.HOST_NAME);
        vo.setSendTime(DateUtils.nowStr());
        return vo;
    }

    private String ipAddr;
    private String hostName;
    private String sendTime;


}
