package cc.wangzijie.ocr;


import cc.wangzijie.config.ServerConfig;
import cc.wangzijie.constants.Constants;
import cc.wangzijie.ocr.component.TaskExecutor;
import cc.wangzijie.ocr.task.*;
import cc.wangzijie.server.entity.OcrSection;
import cc.wangzijie.server.entity.OcrSectionResult;
import cc.wangzijie.server.service.IOcrSectionResultService;
import cc.wangzijie.ui.model.DataListAreaModel;
import cc.wangzijie.ui.model.MainWindowModel;
import cc.wangzijie.ui.model.ScreenshotAreaModel;
import cc.wangzijie.ui.model.SettingsWindowModel;
import io.github.mymonstercat.Model;
import io.github.mymonstercat.ocr.InferenceEngine;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * OCR任务调度中心
 */
@Slf4j
public class OCRManager {


    /**
     * 截屏区域模型
     */
    private final ScreenshotAreaModel screenshotAreaModel;

    /**
     * 数据列表区域模型
     */
    private final DataListAreaModel dataListAreaModel;

    /**
     * 设置弹窗区域模型
     */
    private final SettingsWindowModel settingsWindowModel;

    /**
     * 主窗口模型
     */
    private final MainWindowModel mainWindowModel;

    /**
     * OCR识别结果数据库保存服务
     */
    private final IOcrSectionResultService ocrSectionResultService;

    /**
     * 服务器配置
     */
    private final ServerConfig serverConfig;

    /**
     * RestTemplate
     */
    private final RestTemplate restTemplate;

    /**
     * RapidOCR识别引擎
     */
    private final InferenceEngine ocrEngine;

    /**
     * OCR识别框选区域
     */
    private final Map<String, OcrSection> ocrSectionMap;

    /**
     * 定时截屏采集任务线程Future
     */
    private ScheduledFuture<?> scheduledFuture;

    /**
     * 定时截屏采集任务线程Future
     */
    private ScheduledFuture<?> countDownFuture;

    /**
     * 定时截屏采集任务时间间隔
     */
    private volatile int intervalSeconds;

    /**
     * 倒计时显示的秒数
     */
    private final AtomicInteger countDownSeconds;

    private volatile boolean running;

    public OCRManager(ScreenshotAreaModel screenshotAreaModel, DataListAreaModel dataListAreaModel, MainWindowModel mainWindowModel, SettingsWindowModel settingsWindowModel, IOcrSectionResultService ocrSectionResultService, ServerConfig serverConfig, RestTemplate restTemplate) {
        this.screenshotAreaModel = screenshotAreaModel;
        this.dataListAreaModel = dataListAreaModel;
        this.mainWindowModel = mainWindowModel;
        this.settingsWindowModel = settingsWindowModel;
        this.ocrSectionResultService = ocrSectionResultService;
        this.serverConfig = serverConfig;
        this.restTemplate = restTemplate == null ? new RestTemplate() : restTemplate;
        this.ocrEngine = InferenceEngine.getInstance(Model.ONNX_PPOCR_V4_SERVER);
        // 默认时间间隔：5s
        this.intervalSeconds = Constants.DEFAULT_INTERVAL_SECONDS;
        this.countDownSeconds = new AtomicInteger(0);
        this.ocrSectionMap = new ConcurrentHashMap<>();
        // 设置运行标志=已停止
        this.running = false;
    }

    /**
     * 开始运行
     */
    public synchronized void start() {
        if (this.running) {
            return;
        }
        // 开始定时截屏采集
        SnapshotTask snapshotTask = new SnapshotTask(this.screenshotAreaModel, this, this.screenshotAreaModel.getScreenshotArea());
        this.scheduledFuture = TaskExecutor.scheduleWithFixedDelay(snapshotTask, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);

        // 开始倒计时
        this.countDownSeconds.set(this.intervalSeconds);
        this.countDownFuture = TaskExecutor.scheduleAtFixedRate(() -> {
            int cdSec = this.countDownSeconds.getAndDecrement();
            this.countDownSeconds.compareAndSet(0, this.intervalSeconds);
            String cdSecText = String.format("%02d:%02d", cdSec / 60, cdSec % 60);
            // 确保UI更新在JavaFX线程中执行
            Platform.runLater(() -> {
                this.mainWindowModel.setCollectCountDownText(cdSecText);
            });
        }, 0, 1, TimeUnit.SECONDS);

        // 设置运行标志=运行中
        this.running = true;
    }

    /**
     * 结束运行
     */
    public synchronized void stop() {
        // 停止截屏定时任务
        if (null != this.scheduledFuture) {
            if (!this.scheduledFuture.isCancelled() || !this.scheduledFuture.isDone()) {
                this.scheduledFuture.cancel(true);
            }
        }
        // 结束倒计时
        if (null != this.countDownFuture) {
            if (!this.countDownFuture.isCancelled() || !this.countDownFuture.isDone()) {
                this.countDownFuture.cancel(true);
            }
        }
        // 设置运行标志=已停止
        this.running = false;
        // 确保UI更新在JavaFX线程中执行
        Platform.runLater(() -> {
            mainWindowModel.setCollectCountDownText("已停止");
        });
    }

    public synchronized void setIntervalSeconds(int intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    public synchronized void addOcrSection(OcrSection ocrSection) {
        String key = ocrSection.displayPosition();
        OcrSection oldSection = this.ocrSectionMap.put(key, ocrSection);
        if (oldSection == null) {
            log.info("新增识别区域：key={} \nocrSection={}", key, ocrSection);
        } else {
            log.info("更新识别区域：key={} \noldSection={} \nnewSection={}", key, oldSection, ocrSection);
        }
    }

    public synchronized void onSectionEdit(String key, String name, String type) {
        OcrSection ocrSection = this.ocrSectionMap.get(key);
        if (ocrSection != null) {
            ocrSection.setName(name);
            ocrSection.setType(type);
            log.info("更新识别区域：key={} \nname={}\ntype={}", key, name, type);
        }
    }

    public synchronized OcrSection removeOcrSection(String key) {
        OcrSection ocrSection = this.ocrSectionMap.remove(key);
        if (ocrSection != null) {
            log.info("删除识别区域：key={} \nocrSection={}", key, ocrSection);
        }
        return ocrSection;
    }

    public synchronized void clearOcrSection() {
        this.ocrSectionMap.clear();
    }

    public void newResult(String key, OcrSectionResult result) {
        // OCR识别结果更新到UI视图模型中
        this.dataListAreaModel.addData(key, result);
    }

    public OcrProcessTask createTask(BufferedImage screenshot) {
        return new OcrProcessTask(this, this.ocrEngine, screenshot, this.ocrSectionMap);
    }

    public FileOutputTask createFileOutputTask(BufferedImage screenshot, List<OcrSectionResult> resultList) {
        return new FileOutputTask(screenshot, this.ocrSectionMap, resultList, this.settingsWindowModel);
    }

    public DatabaseOutputTask createDatabaseOutputTask(List<OcrSectionResult> resultList) {
        return new DatabaseOutputTask(this.ocrSectionResultService, resultList, this.settingsWindowModel);
    }

    public CallbackHookTask createCallbackHookTask(List<OcrSectionResult> resultList) {
        return new CallbackHookTask(resultList, this.settingsWindowModel, this.serverConfig, this.restTemplate);
    }
}
