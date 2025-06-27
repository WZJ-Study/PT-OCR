package cc.wangzijie.ocr.task;

import cc.wangzijie.ocr.OCRManager;
import cc.wangzijie.ocr.component.TaskExecutor;
import cc.wangzijie.ui.model.ScreenshotAreaModel;
import cc.wangzijie.ui.utils.AwtRobotUtils;
import cc.wangzijie.utils.DateFormat;
import cc.wangzijie.utils.DateUtils;
import javafx.scene.shape.Rectangle;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;

@Slf4j
public class SnapshotTask implements Runnable {

    /**
     * 截屏区域模型
     */
    private final ScreenshotAreaModel screenshotAreaModel;

    /**
     * OCR任务调度中心
     */
    private final OCRManager ocrManager;

    private final Rectangle snapshotRect;

    @Setter
    private boolean asyncFlag;

    public SnapshotTask(ScreenshotAreaModel screenshotAreaModel, OCRManager ocrManager, Rectangle snapshotRect, boolean asyncFlag) {
        log.info("==== SnapshotTask ==== 截屏任务初始化：开始！");
        this.screenshotAreaModel = screenshotAreaModel;
        this.ocrManager = ocrManager;
        this.snapshotRect = snapshotRect;
        this.asyncFlag = asyncFlag;
        log.info("==== SnapshotTask ==== 截屏任务初始化：完毕！");
    }


    @Override
    public void run() {
        String triggerLabel = DateUtils.nowStr(DateFormat.DUMMY_CODE);
        log.info("==== SnapshotTask[{}] ==== 截屏任务开始：开始执行！", triggerLabel);
        // 截屏
        BufferedImage screenshot = AwtRobotUtils.createScreenCapture(this.snapshotRect);
        log.info("==== SnapshotTask[{}] ==== 截屏图片 width={} height={}", triggerLabel,
                screenshot.getWidth(), screenshot.getHeight());
        // 显示预览‌
        this.screenshotAreaModel.setScreenshot(screenshot);
        log.info("==== SnapshotTask[{}] ==== 截屏图片存储到screenshotAreaModel中，以显示预览", triggerLabel);
        // 启动处理任务
//        if (this.asyncFlag) {
//            // 异步执行
//            log.info("==== SnapshotTask[{}] ==== 创建并启动OCR处理任务 OcrProcessTask 处理截屏图片", triggerLabel);
//            TaskExecutor.execute(this.ocrManager.createOcrProcessTask(triggerLabel, screenshot, this.asyncFlag));
//        } else {
//            // 同步执行
//            log.info("==== SnapshotTask[{}] ==== 创建并启动OCR处理任务 OcrProcessTask 处理截屏图片", triggerLabel);
//            OcrProcessTask ocrProcessTask = this.ocrManager.createOcrProcessTask(triggerLabel, screenshot, this.asyncFlag);
//            if (null != ocrProcessTask) {
//                ocrProcessTask.run();
//            }
//        }
        log.info("==== SnapshotTask[{}] ==== 创建并启动OCR处理任务 OcrProcessTask 处理截屏图片", triggerLabel);
        TaskExecutor.execute(this.ocrManager.createOcrProcessTask(triggerLabel, screenshot, this.asyncFlag));
    }

}
