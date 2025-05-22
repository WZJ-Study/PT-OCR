package cc.wangzijie.ocr.task;

import cc.wangzijie.config.ServerConfig;
import cc.wangzijie.constants.Constants;
import cc.wangzijie.server.entity.CallbackHookVO;
import cc.wangzijie.server.entity.OcrSectionResult;
import cc.wangzijie.server.service.IOcrSectionResultService;
import cc.wangzijie.ui.model.SettingsWindowModel;
import cc.wangzijie.utils.JacksonUtils;
import cc.wangzijie.utils.RetryHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
public class CallbackHookTask implements Runnable {

    private final List<OcrSectionResult> resultList;

    private final boolean enabledFlag;

    private final String callbackHookUrl;

    private final RestTemplate restTemplate;

    public CallbackHookTask(List<OcrSectionResult> resultList, SettingsWindowModel settingsWindowModel, ServerConfig serverConfig, RestTemplate restTemplate) {
        this.resultList = resultList;
        if (settingsWindowModel == null) {
            this.enabledFlag = true;
        } else {
            this.enabledFlag = settingsWindowModel.isCallbackHookEnabledFlag();
        }
        if (settingsWindowModel == null || settingsWindowModel.getCallbackHookUrl() == null) {
            this.callbackHookUrl = serverConfig.buildUrl(Constants.DEFAULT_CALLBACK_HOOK_URI);
        } else {
            this.callbackHookUrl = settingsWindowModel.getCallbackHookUrl();
        }
        this.restTemplate = restTemplate == null ? new RestTemplate() : restTemplate;
    }

    @Override
    public void run() {
        if (!this.enabledFlag) {
            log.info("==== CallbackHookTask ==== 已禁用回调钩子URL，跳过！");
            return;
        }
        if (CollectionUtils.isEmpty(resultList)) {
            log.info("==== CallbackHookTask ==== 没有需要发送的结果数据，跳过！");
            return;
        }
        CallbackHookVO vo = CallbackHookVO.of(resultList);
        String jsonData = JacksonUtils.toJSONString(vo);
        log.info("==== CallbackHookTask ==== 回调钩子URL：{} \n准备发送的json数据: {}", callbackHookUrl, jsonData);
        RetryHelper.execute(context -> callback(jsonData));
    }



    public boolean callback(String jsonData) {
        try {
            RequestEntity<String> requestEntity = RequestEntity.method(HttpMethod.POST, new URI(callbackHookUrl))
                    .contentType(MediaType.APPLICATION_JSON).body(jsonData);
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("==== callback ==== 回调钩子URL成功！对方返回结果：{}", responseEntity.getBody());
                return true;
            } else {
                log.error("==== callback ==== 回调钩子URL失败！对方返回结果：{}", responseEntity.getBody());
                throw new RuntimeException(responseEntity.getBody());
            }
        } catch (Exception e) {
            log.error("==== callback ==== 回调钩子URL失败！", e);
            throw new RuntimeException(e);
        }
    }
}
