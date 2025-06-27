package cc.wangzijie.ocr.task;

import cc.wangzijie.constants.Constants;
import cc.wangzijie.server.entity.OcrSection;
import cc.wangzijie.server.entity.OcrSectionResult;
import cc.wangzijie.ui.model.SettingsWindowModel;
import cc.wangzijie.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@Slf4j
public class FileOutputTask implements Runnable {

    /**
     * 待保存的截图图片
     */
    private final BufferedImage snapshotImage;

    /**
     * OCR识别框选区域
     */
    private final Map<String, OcrSection> ocrRectMap;

    /**
     * OCR识别数据结果列表
     */
    private final List<OcrSectionResult> resultList;

    /**
     * 截屏图片保存配置
     */
    private final boolean enabledFlag;
    private final String nowStr;
    private final String outputFolderPath;

    private final String triggerLabel;

    public FileOutputTask(String triggerLabel, BufferedImage snapshotImage, Map<String, OcrSection> ocrRectMap, List<OcrSectionResult> resultList, SettingsWindowModel settingsWindowModel) {
        log.info("==== FileOutputTask[{}] ==== 文件输出任务初始化：开始！", triggerLabel);
        this.triggerLabel = triggerLabel;
        this.snapshotImage = snapshotImage;
        this.ocrRectMap = ocrRectMap;
        this.resultList = resultList;
        this.nowStr = DateUtils.nowStr(DateFormat.DUMMY_CODE);
        if (settingsWindowModel == null) {
            this.enabledFlag = true;
        } else {
            this.enabledFlag = settingsWindowModel.isOutputFolderEnabledFlag();
        }
        if (settingsWindowModel == null || settingsWindowModel.getOutputFolderPath() == null) {
            this.outputFolderPath = Constants.DEFAULT_OUTPUT_FOLDER_PATH + File.separator + nowStr;
        } else {
            this.outputFolderPath = settingsWindowModel.getOutputFolderPath() + File.separator + nowStr;
        }
        log.info("==== FileOutputTask[{}] ==== 文件输出任务初始化：完毕！", triggerLabel);
    }

    @Override
    public void run() {
        if (!this.enabledFlag) {
            log.info("==== FileOutputTask[{}] ==== 已禁用截屏文件输出目录，跳过！", triggerLabel);
            return;
        }
        log.info("==== FileOutputTask[{}] ==== 当前时间：{}\n截屏文件输出目录: {}\n", triggerLabel, this.nowStr, this.outputFolderPath);

        // 创建上级目录
        log.info("==== FileOutputTask[{}] ==== 创建上级目录！", triggerLabel);
        FileUtils.initFolder(this.outputFolderPath);

        // JSON文件保存OCR识别结果
        log.info("==== FileOutputTask[{}] ==== JSON文件保存OCR识别结果！", triggerLabel);
        this.outputAsJsonFile(resultList);

        // 保存截屏图片文件
        log.info("==== FileOutputTask[{}] ==== 保存截屏图片文件！", triggerLabel);
        this.saveToFile();

        // 绘制各框选区域并保存图片
        log.info("==== FileOutputTask[{}] ==== 绘制各框选区域并保存图片！", triggerLabel);
        this.saveDrawImageToFile();
    }


    /**
     * 截屏图片保存为文件
     */
    private void saveToFile() {
        try {
            int height = this.snapshotImage.getHeight();
            int width = this.snapshotImage.getWidth();
            log.info("==== FileOutputTask[{}] saveToFile ==== 保存截屏图片，图片尺寸 height = {} width = {}", triggerLabel, height, width);
            String fileName = String.format("截屏图片-%s.%s", this.nowStr, Constants.IMAGE_FORMAT);
            String filePath = String.format("%s%s%s", this.outputFolderPath, File.separator, fileName);
            File file = new File(filePath);
            ImageIO.write(this.snapshotImage, Constants.IMAGE_FORMAT, file);
            log.info("==== FileOutputTask[{}] saveToFile ==== 截屏图片文件大小：{} \n保存为：{}", triggerLabel, FileSizeUtils.displayFileSize(file), file.getAbsolutePath());
        } catch (IOException e) {
            log.error("==== FileOutputTask[" + triggerLabel + "] saveToFile ==== 截屏图片文件保存失败，捕获到异常！", e);
        }
    }


    private void saveDrawImageToFile() {
        // 创建Graphics2D对象
        log.info("==== FileOutputTask[{}] saveDrawImageToFile ==== 创建Graphics2D对象！", triggerLabel);
        Graphics2D g2d = this.snapshotImage.createGraphics();

        // 设置颜色和线宽
        log.info("==== FileOutputTask[{}] saveDrawImageToFile ==== 设置颜色和线宽！", triggerLabel);
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));

        // 绘制各框选区域
        log.info("==== FileOutputTask[{}] saveDrawImageToFile ==== 绘制各框选区域！", triggerLabel);
        for (String key : this.ocrRectMap.keySet()) {
            // 画矩形框
            OcrSection ocrSection = this.ocrRectMap.get(key);
            Rectangle rect = new Rectangle(ocrSection.getX(), ocrSection.getY(), ocrSection.getWidth(), ocrSection.getHeight());
            g2d.draw(rect);

            // 画文本
            g2d.drawString(ocrSection.displaySection(), ocrSection.getX(), ocrSection.getY());
        }

        // 释放图形上下文使用的资源
        log.info("==== FileOutputTask[{}] saveDrawImageToFile ==== 释放图形上下文使用的资源！", triggerLabel);
        g2d.dispose();

        // 保存修改后的图片
        log.info("==== FileOutputTask[{}] saveDrawImageToFile ==== 保存修改后的图片！", triggerLabel);
        try {
            String fileName = String.format("截屏图片-%s.绘制框选区域.%s", this.nowStr, Constants.IMAGE_FORMAT);
            String filePath = String.format("%s%s%s", this.outputFolderPath, File.separator, fileName);
            ImageIO.write(this.snapshotImage, Constants.IMAGE_FORMAT, new File(filePath));
        } catch (IOException e) {
            log.error("==== FileOutputTask[" + triggerLabel + "] saveDrawImageToFile ==== 绘制框选区域图片文件保存失败，捕获到异常！", e);
        }
    }

    private void outputAsJsonFile(List<OcrSectionResult> resultList) {
        if (CollectionUtils.isEmpty(resultList)) {
            return;
        }
        String json = JacksonUtils.toJSONStringPretty(resultList);

        String fileName = String.format("截屏图片-%s.OCR识别结果.json", this.nowStr);
        String filePath = String.format("%s%s%s", this.outputFolderPath, File.separator, fileName);
        try (OutputStream os = new FileOutputStream(filePath)) {
            IOUtils.write(json, os, Charset.defaultCharset());
        } catch (IOException e) {
            log.error("==== FileOutputTask[" + triggerLabel + "] outputAsJsonFile ==== JSON文件保存OCR识别结果失败，捕获到异常！", e);
        }
    }

}
