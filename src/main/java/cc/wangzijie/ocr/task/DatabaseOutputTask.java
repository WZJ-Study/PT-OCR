package cc.wangzijie.ocr.task;

import cc.wangzijie.server.entity.OcrSectionResult;
import cc.wangzijie.server.service.IOcrSectionResultService;
import cc.wangzijie.ui.model.SettingsWindowModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@Slf4j
public class DatabaseOutputTask implements Runnable {

    /**
     * OCR识别结果数据库保存服务
     */
    private final IOcrSectionResultService ocrSectionResultService;

    private final List<OcrSectionResult> resultList;

    private final boolean enabledFlag;

    private final String triggerLabel;

    public DatabaseOutputTask(String triggerLabel, IOcrSectionResultService ocrSectionResultService, List<OcrSectionResult> resultList, SettingsWindowModel settingsWindowModel) {
        log.info("==== DatabaseOutputTask[{}] ==== 数据库输出任务初始化：开始！", triggerLabel);
        this.triggerLabel = triggerLabel;
        this.ocrSectionResultService = ocrSectionResultService;
        this.resultList = resultList;
        if (settingsWindowModel == null) {
            this.enabledFlag = true;
        } else {
            this.enabledFlag = settingsWindowModel.isCallbackHookEnabledFlag();
        }
        log.info("==== DatabaseOutputTask[{}] ==== 数据库输出任务初始化：完毕！", triggerLabel);
    }

    @Override
    public void run() {
        if (!this.enabledFlag) {
            log.info("==== DatabaseOutputTask[{}] ==== 已禁用本地SQLite数据库，跳过！", triggerLabel);
            return;
        }
        if (CollectionUtils.isEmpty(resultList)) {
            log.info("==== DatabaseOutputTask[{}] ==== 没有需要保存的结果数据，跳过！", triggerLabel);
            return;
        }
        if (this.ocrSectionResultService == null) {
            log.error("==== DatabaseOutputTask[{}] ==== 数据库服务注入异常，请检查！", triggerLabel);
            return;
        }
        this.ocrSectionResultService.saveBatch(resultList);
        log.info("==== DatabaseOutputTask[{}] ==== 数据库输出任务：执行完毕！", triggerLabel);
    }
}
