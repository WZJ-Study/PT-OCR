package cc.wangzijie.ocr.task;

import cc.wangzijie.constants.Constants;
import cc.wangzijie.ocr.OCRManager;
import cc.wangzijie.ocr.component.TaskExecutor;
import cc.wangzijie.server.entity.OcrSection;
import cc.wangzijie.server.entity.OcrSectionResult;
import cc.wangzijie.utils.DateUtils;
import com.benjaminwan.ocrlibrary.OcrResult;
import io.github.mymonstercat.ocr.InferenceEngine;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class OcrProcessTask implements Runnable {

    /**
     * OCR任务调度中心
     */
    private final OCRManager ocrManager;

    /**
     * RapidOCR识别引擎
     */
    private final InferenceEngine ocrEngine;

    /**
     * 待OCR识别的截图图片
     */
    private final BufferedImage snapshotImage;

    /**
     * OCR识别框选区域
     */
    private final Map<String, OcrSection> ocrRectMap;

    private final String triggerLabel;

    private final boolean syncFlag;

    public OcrProcessTask(String triggerLabel, OCRManager ocrManager, InferenceEngine ocrEngine, BufferedImage snapshotImage, Map<String, OcrSection> ocrRectMap, boolean syncFlag) {
        log.info("==== OcrProcessTask[{}] ==== OCR处理任务初始化：开始！", triggerLabel);
        this.triggerLabel = triggerLabel;
        this.ocrManager = ocrManager;
        this.snapshotImage = snapshotImage;
        this.ocrRectMap = ocrRectMap;
        this.ocrEngine = ocrEngine;
        this.syncFlag = syncFlag;
        log.info("==== OcrProcessTask[{}] ==== OCR处理任务初始化：完毕！", triggerLabel);
    }

    @Override
    public void run() {
        log.info("==== OcrProcessTask[{}] ==== OCR处理任务：开始执行！", triggerLabel);
        String collectTime = DateUtils.nowStr();
        log.info("==== OcrProcessTask[{}] ==== 开始OCR识别！当前时间：{}\n", triggerLabel, collectTime);

        // OCR识别各框选区域
        List<OcrSectionResult> resultList = new LinkedList<>();
        for (String key : this.ocrRectMap.keySet()) {
            try {
                OcrSection ocrSection = this.ocrRectMap.get(key);

                // 创建截取区域的新图片
                BufferedImage rectImage = this.snapshotImage.getSubimage(ocrSection.getX(), ocrSection.getY(), ocrSection.getWidth(), ocrSection.getHeight());

                // 保存截取区域的图片
                String subFileName = "SubFile_" + ocrSection.getId();
                File subFile = File.createTempFile(subFileName, Constants.IMAGE_FORMAT_WITH_DOT);
                ImageIO.write(rectImage, Constants.IMAGE_FORMAT, subFile);

                // 执行OCR识别
                OcrResult ocrResult = this.ocrEngine.runOcr(subFile.getPath());
                log.info("==== OcrProcessTask[{}] ==== 截屏图片文件OCR识别成功，区域：{} ==> 识别结果：\n\n{}",
                        triggerLabel, key, ocrResult.getStrRes());
                OcrSectionResult result = ocrSection.newResult(ocrResult, collectTime);
                resultList.add(result);

                // OCR识别结果更新到UI视图模型中
                this.ocrManager.newResult(ocrSection.displayPosition(), result);
            } catch (Exception e) {
                log.error("==== OcrProcessTask[" + triggerLabel + "] ==== 截屏图片文件OCR识别失败，捕获到异常！", e);
            }
        }

        // OCR识别结果保存到数据库
        log.info("==== OcrProcessTask[{}] ==== 创建并启动数据库输出任务 DatabaseOutputTask 保存OCR识别结果", triggerLabel);
        if (this.syncFlag) {
            DatabaseOutputTask databaseOutputTask = this.ocrManager.createDatabaseOutputTask(triggerLabel, resultList);
            if (null != databaseOutputTask) {
                databaseOutputTask.run();
            }
        } else {
            TaskExecutor.execute(this.ocrManager.createDatabaseOutputTask(triggerLabel, resultList));
        }

        // OCR识别结果保存到文件
        log.info("==== OcrProcessTask[{}] ==== 创建并启动文件输出任务 FileOutputTask 保存截屏图片和OCR识别结果", triggerLabel);
        if (this.syncFlag) {
            FileOutputTask fileOutputTask = this.ocrManager.createFileOutputTask(triggerLabel, this.snapshotImage, resultList);
            if (null != fileOutputTask) {
                fileOutputTask.run();
            }
        } else {
            TaskExecutor.execute(this.ocrManager.createFileOutputTask(triggerLabel, this.snapshotImage, resultList));
        }

        // OCR识别结果触发回调钩子
        log.info("==== OcrProcessTask[{}] ==== 创建并启动URL回调任务 CallbackHookTask 发送OCR识别结果", triggerLabel);
        if (this.syncFlag) {
            CallbackHookTask callbackHookTask = this.ocrManager.createCallbackHookTask(triggerLabel, resultList);
            if (null != callbackHookTask) {
                callbackHookTask.run();
            }
        } else {
            TaskExecutor.execute(this.ocrManager.createCallbackHookTask(triggerLabel, resultList));
        }
    }

}
