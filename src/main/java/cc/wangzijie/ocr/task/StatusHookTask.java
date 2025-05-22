package cc.wangzijie.ocr.task;

import cc.wangzijie.config.ServerConfig;
import cc.wangzijie.constants.Constants;
import cc.wangzijie.server.entity.CallbackHookVO;
import cc.wangzijie.server.entity.OcrSectionResult;
import cc.wangzijie.server.entity.StatusHookVO;
import cc.wangzijie.ui.model.SettingsWindowModel;
import cc.wangzijie.utils.JacksonUtils;
import cc.wangzijie.utils.RetryHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
public class StatusHookTask implements Runnable {

    private final String newStatus;

    private final String updateStatusHookUrl;

    private final RestTemplate restTemplate;

    public StatusHookTask(String newStatus, SettingsWindowModel settingsWindowModel, ServerConfig serverConfig, RestTemplate restTemplate) {
        this.newStatus = newStatus;
        if (settingsWindowModel == null || settingsWindowModel.getUpdateStatusHookUrl() == null) {
            this.updateStatusHookUrl = serverConfig.buildUrl(Constants.DEFAULT_UPDATE_STATUS_HOOK_URI);
        } else {
            this.updateStatusHookUrl = settingsWindowModel.getUpdateStatusHookUrl();
        }
        this.restTemplate = restTemplate == null ? new RestTemplate() : restTemplate;
    }



    @Override
    public void run() {
        if (StringUtils.isBlank(this.newStatus)) {
            log.info("==== StatusHookTask ==== 没有需要发送的结果数据，跳过！");
            return;
        }
        StatusHookVO vo = StatusHookVO.of(newStatus);
        String jsonData = JacksonUtils.toJSONString(vo);
        log.info("==== StatusHookTask ==== 回调钩子URL：{} \n准备发送的json数据: {}", this.updateStatusHookUrl, jsonData);
        RetryHelper.execute(context -> callback(jsonData));
    }



    public boolean callback(String jsonData) {
        try {
            RequestEntity<String> requestEntity = RequestEntity.method(HttpMethod.POST, new URI(updateStatusHookUrl))
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
