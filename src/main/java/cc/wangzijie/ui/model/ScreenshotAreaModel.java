package cc.wangzijie.ui.model;

import cc.wangzijie.config.ServerConfig;
import cc.wangzijie.ocr.OCRManager;
import cc.wangzijie.server.service.IOcrSectionResultService;
import cc.wangzijie.ui.utils.AwtRobotUtils;
import javafx.beans.property.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ScreenshotAreaModel {

    private final ObjectProperty<Rectangle> screenshotArea = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> screenshotImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Cursor> screenshotImageCursor = new SimpleObjectProperty<>();
    private final StringProperty screenshotImageHint = new SimpleStringProperty();
    private final BooleanProperty screenshotImageHintVisible = new SimpleBooleanProperty(true);
    private final BooleanProperty screenshotAreaNoImageFlag = new SimpleBooleanProperty(true);

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private DataListAreaModel dataListAreaModel;

    @Resource
    private MainWindowModel mainWindowModel;

    @Resource
    private SettingsWindowModel settingsWindowModel;

    @Resource
    private IOcrSectionResultService ocrSectionResultService;

    @Getter
    private OCRManager ocrManager;

    @PostConstruct
    public void init() {
        this.ocrManager = new OCRManager(this, dataListAreaModel, mainWindowModel, settingsWindowModel, ocrSectionResultService, serverConfig, restTemplate);
        this.settingsWindowModel.intervalSecondsProperty().addListener((observableValue, oldValue, newValue) -> {
            log.info("==== 修改定时采集间隔 ==== 设置新的采集间隔：{}秒", newValue);
            this.ocrManager.setIntervalSeconds(newValue.intValue());
        });

        this.settingsWindowModel.asyncFlagProperty().addListener((observableValue, oldValue, newValue) -> {
            log.info("==== 修改异步处理开关 ==== 设置AsyncFlag：{}", newValue ? "异步" : "同步");
            this.ocrManager.setAsyncFlag(newValue);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 清理代码，例如关闭数据库连接、停止线程等
            this.ocrManager.stop();
        }));
    }


    @Getter
    private final Map<String, Rectangle> rectMap = new ConcurrentHashMap<>();

    @Getter
    @Setter
    private Integer imageHeight;

    @Getter
    @Setter
    private Integer imageWidth;

    @Getter
    @Setter
    private BufferedImage screenshot;

    public void doScreenshot() {
        Rectangle rect = this.screenshotArea.get();
        if (null == rect) {
            log.info("==== 执行截屏 === 未选定截屏区域！默认截屏整个屏幕！");
            try {
                // 捕获屏幕区域‌
                this.screenshot = AwtRobotUtils.createScreenCapture();
                Objects.requireNonNull(this.screenshot);

                // 转换为JavaFX Image并显示预览‌
                this.setScreenshotImage(SwingFXUtils.toFXImage(this.screenshot, null));
                this.setScreenshotImageCursor(Cursor.CROSSHAIR);
                this.setScreenshotImageHintVisible(false);
                this.setScreenshotAreaNoImageFlag(false);
                this.setImageHeight(this.screenshot.getHeight());
                this.setImageWidth(this.screenshot.getWidth());
            } catch (Exception ex) {
                log.error("==== 执行截屏 === 全屏截图失败！", ex);
            }
        } else {
            log.info("==== 执行截屏 === 截屏区域 rect={}", rect);
            try {
                // 捕获屏幕区域‌
                this.screenshot = AwtRobotUtils.createScreenCapture(rect);

                // 转换为JavaFX Image并显示预览‌
                this.setScreenshotImage(SwingFXUtils.toFXImage(this.screenshot, null));
                this.setScreenshotImageCursor(Cursor.CROSSHAIR);
                this.setScreenshotImageHintVisible(false);
                this.setScreenshotAreaNoImageFlag(false);
                this.setImageHeight(this.screenshot.getHeight());
                this.setImageWidth(this.screenshot.getWidth());
            } catch (Exception ex) {
                log.error("==== 执行截屏 === 区域截图失败！", ex);
            }
        }
    }

    public Collection<Rectangle> getRectList() {
        return this.rectMap.values();
    }


    public synchronized void clearRectMap() {
        this.rectMap.clear();
    }

    public synchronized void addRect(String key, Rectangle rectangle) {
        rectMap.put(key, rectangle);
    }

    public synchronized Rectangle removeRect(String key) {
        return rectMap.remove(key);
    }

    public void setScreenshot(BufferedImage screenshot) {
        if (null == screenshot) {
            return;
        }
        this.screenshot = screenshot;

        // 转换为JavaFX Image并显示预览‌
        this.setScreenshotImage(SwingFXUtils.toFXImage(this.screenshot, null));
        this.setScreenshotImageCursor(Cursor.CROSSHAIR);
        this.setScreenshotImageHintVisible(false);
        this.setScreenshotAreaNoImageFlag(false);
        this.setImageHeight(this.screenshot.getHeight());
        this.setImageWidth(this.screenshot.getWidth());
    }

    public Rectangle getScreenshotArea() {
        return screenshotArea.get();
    }

    public ObjectProperty<Rectangle> screenshotAreaProperty() {
        return screenshotArea;
    }

    public void setScreenshotArea(Rectangle screenshotArea) {
        this.screenshotArea.set(screenshotArea);
    }

    public Image getScreenshotImage() {
        return screenshotImage.get();
    }

    public ObjectProperty<Image> screenshotImageProperty() {
        return screenshotImage;
    }

    public void setScreenshotImage(Image screenshotImage) {
        this.screenshotImage.set(screenshotImage);
    }

    public Cursor getScreenshotImageCursor() {
        return screenshotImageCursor.get();
    }

    public ObjectProperty<Cursor> screenshotImageCursorProperty() {
        return screenshotImageCursor;
    }

    public void setScreenshotImageCursor(Cursor screenshotImageCursor) {
        this.screenshotImageCursor.set(screenshotImageCursor);
    }

    public String getScreenshotImageHint() {
        return screenshotImageHint.get();
    }

    public StringProperty screenshotImageHintProperty() {
        return screenshotImageHint;
    }

    public void setScreenshotImageHint(String screenshotImageHint) {
        this.screenshotImageHint.set(screenshotImageHint);
    }

    public boolean isScreenshotImageHintVisible() {
        return screenshotImageHintVisible.get();
    }

    public BooleanProperty screenshotImageHintVisibleProperty() {
        return screenshotImageHintVisible;
    }

    public void setScreenshotImageHintVisible(boolean screenshotImageHintVisible) {
        this.screenshotImageHintVisible.set(screenshotImageHintVisible);
    }

    public boolean isScreenshotAreaNoImageFlag() {
        return screenshotAreaNoImageFlag.get();
    }

    public BooleanProperty screenshotAreaNoImageFlagProperty() {
        return screenshotAreaNoImageFlag;
    }

    public void setScreenshotAreaNoImageFlag(boolean screenshotAreaNoImageFlag) {
        this.screenshotAreaNoImageFlag.set(screenshotAreaNoImageFlag);
    }



}
