package cc.wangzijie.ocr.task;

import cc.wangzijie.config.ServerConfig;
import cc.wangzijie.constants.Constants;
import cc.wangzijie.server.entity.BasicHookVO;
import cc.wangzijie.ui.model.SettingsWindowModel;
import cc.wangzijie.utils.IpHelper;
import cc.wangzijie.utils.JacksonUtils;
import cc.wangzijie.utils.RetryHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class StopWarningHookTask implements Runnable {

    private final String localIp;

    private final String closeWarningHookUrl;

    private final RestTemplate restTemplate;

    public StopWarningHookTask(SettingsWindowModel settingsWindowModel, ServerConfig serverConfig, RestTemplate restTemplate) {
        if (settingsWindowModel == null || settingsWindowModel.getLocalIp() == null) {
            this.localIp = IpHelper.LOCAL_IP;
        } else {
            this.localIp = settingsWindowModel.getLocalIp();
        }
        if (settingsWindowModel == null || settingsWindowModel.getStopWarningHookUrl() == null) {
            this.closeWarningHookUrl = serverConfig.buildUrl(Constants.DEFAULT_STOP_WARNING_HOOK_URI);
        } else {
            this.closeWarningHookUrl = settingsWindowModel.getStopWarningHookUrl();
        }
        this.restTemplate = restTemplate == null ? new RestTemplate() : restTemplate;
    }



    @Override
    public void run() {
        BasicHookVO vo = BasicHookVO.of(localIp);
        String jsonData = JacksonUtils.toJSONString(vo);
        log.info("==== StopWarningHookTask ==== 回调钩子URL：{} \n准备发送的json数据: {}", this.closeWarningHookUrl, jsonData);
        RetryHelper.execute(context -> callback(jsonData));
    }



    public boolean callback(String jsonData) {
        try {
            RequestEntity<String> requestEntity = RequestEntity.method(HttpMethod.POST, new URI(closeWarningHookUrl))
                    .contentType(MediaType.APPLICATION_JSON).body(jsonData);
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("==== callback ==== 回调钩子URL成功！对方返回结果：{}", responseEntity.getBody());
                return true;
            } else {
                log.error("==== callback ==== 回调钩子URL失败！对方返回结果：{}", responseEntity.getBody());
                throw new RuntimeException(responseEntity.getBody());
            }
        } catch (URISyntaxException e) {
            log.error("==== callback ==== 回调钩子URL失败！", e);
            throw new RuntimeException(e);
        }
    }
}
