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
        log.info("==== OCRManager ==== 开始运行！");
        boolean syncFlag = true;

        // 开始定时截屏采集
        log.info("==== OCRManager ==== 创建截屏采集定时任务 SnapshotTask 定时采集截屏图片（时间间隔：{}秒）", intervalSeconds);
        SnapshotTask snapshotTask = new SnapshotTask(this.screenshotAreaModel, this, this.screenshotAreaModel.getScreenshotArea(), syncFlag);
        this.scheduledFuture = TaskExecutor.scheduleWithFixedDelay(snapshotTask, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);

        // 开始倒计时
        log.info("==== OCRManager ==== 创建屏幕倒计时定时任务 scheduleAtFixedRate 每秒一次刷新屏幕倒计时");
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
        log.info("==== OCRManager ==== 设置运行标志=运行中");
        this.running = true;
    }

    /**
     * 结束运行
     */
    public synchronized void stop() {
        log.info("==== OCRManager ==== 结束运行！");
        // 停止截屏采集定时任务
        if (null != this.scheduledFuture) {
            if (!this.scheduledFuture.isCancelled() || !this.scheduledFuture.isDone()) {
                this.scheduledFuture.cancel(true);
                log.info("==== OCRManager ==== 停止截屏采集定时任务！");
            }
        }
        // 结束倒计时
        if (null != this.countDownFuture) {
            if (!this.countDownFuture.isCancelled() || !this.countDownFuture.isDone()) {
                this.countDownFuture.cancel(true);
                log.info("==== OCRManager ==== 停止屏幕倒计时定时任务！");
            }
        }
        // 设置运行标志=已停止
        log.info("==== OCRManager ==== 设置运行标志=已停止");
        this.running = false;
        // 确保UI更新在JavaFX线程中执行
        log.info("==== OCRManager ==== UI显示文本：已停止");
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

    public OcrProcessTask createOcrProcessTask(String triggerLabel, BufferedImage screenshot, boolean syncFlag) {
        log.info("==== OCRManager ==== 创建OCR处理任务 OcrProcessTask[{}] 处理截屏图片", triggerLabel);
        return new OcrProcessTask(triggerLabel, this, this.ocrEngine, screenshot, this.ocrSectionMap, syncFlag);
    }

    public FileOutputTask createFileOutputTask(String triggerLabel, BufferedImage screenshot, List<OcrSectionResult> resultList) {
        log.info("==== OCRManager ==== 创建文件输出任务 FileOutputTask[{}] 保存截屏图片和OCR识别结果", triggerLabel);
        return new FileOutputTask(triggerLabel, screenshot, this.ocrSectionMap, resultList, this.settingsWindowModel);
    }

    public DatabaseOutputTask createDatabaseOutputTask(String triggerLabel, List<OcrSectionResult> resultList) {
        log.info("==== OCRManager ==== 创建数据库输出任务 DatabaseOutputTask[{}] 保存OCR识别结果", triggerLabel);
        return new DatabaseOutputTask(triggerLabel, this.ocrSectionResultService, resultList, this.settingsWindowModel);
    }

    public CallbackHookTask createCallbackHookTask(String triggerLabel, List<OcrSectionResult> resultList) {
        log.info("==== OCRManager ==== 创建URL回调任务 CallbackHookTask[{}] 发送OCR识别结果", triggerLabel);
        return new CallbackHookTask(triggerLabel, resultList, this.settingsWindowModel, this.serverConfig, this.restTemplate);
    }

}
