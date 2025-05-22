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
public class CallbackHookVO {

    public static CallbackHookVO of(String localIp, List<OcrSectionResult> resultList) {
        CallbackHookVO vo = new CallbackHookVO();
        vo.setIpAddr(localIp);
        vo.setHostName(IpHelper.HOST_NAME);
        vo.setData(resultList.stream().map(OcrSectionResult::toDisplay).collect(Collectors.toList()));
        vo.setSendTime(DateUtils.nowStr());
        return vo;
    }

    private String ipAddr;
    private String hostName;
    private List<OcrSectionResultDisplayVO> data;
    private String sendTime;


}
