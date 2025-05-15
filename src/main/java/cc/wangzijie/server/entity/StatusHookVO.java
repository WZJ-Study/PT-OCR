package cc.wangzijie.server.entity;

import cc.wangzijie.utils.DateUtils;
import cc.wangzijie.utils.IpHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class StatusHookVO {

    public static StatusHookVO of(String status) {
        StatusHookVO vo = new StatusHookVO();
        vo.setIpAddr(IpHelper.LOCAL_IP);
        vo.setHostName(IpHelper.HOST_NAME);
        vo.setData(status);
        vo.setSendTime(DateUtils.nowStr());
        return vo;
    }

    private String ipAddr;
    private String hostName;
    private String data;
    private String sendTime;


}
