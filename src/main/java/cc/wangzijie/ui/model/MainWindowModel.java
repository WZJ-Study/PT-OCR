package cc.wangzijie.ui.model;

import javafx.beans.property.*;
import javafx.scene.image.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MainWindowModel {

    private final ObjectProperty<Image> mainWindowLogoImage = new SimpleObjectProperty<>();

    private final ObjectProperty<Image> closeWindowButtonImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> maximizeWindowButtonImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> minimizeWindowButtonImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> openSettingsWindowButtonImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> reloadMainWindowButtonImage = new SimpleObjectProperty<>();

    private final ObjectProperty<Image> openFullMenuButtonImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> historyDataMenuButtonImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> screenshotMenuButtonImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> withdrawMenuButtonImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> toggleCollectStatusMenuButtonImage = new SimpleObjectProperty<>();
    private final SimpleStringProperty collectCountDownText = new SimpleStringProperty();
    private final BooleanProperty collectRunningFlag = new SimpleBooleanProperty(false);

    private final StringProperty manualUpdateStatus = new SimpleStringProperty();
    private final ObjectProperty<Image> manualUpdateStatusButtonImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> stopWarningButtonImage = new SimpleObjectProperty<>();

    public Image getMainWindowLogoImage() {
        return mainWindowLogoImage.get();
    }

    public ObjectProperty<Image> mainWindowLogoImageProperty() {
        return mainWindowLogoImage;
    }

    public void setMainWindowLogoImage(Image mainWindowLogoImage) {
        this.mainWindowLogoImage.set(mainWindowLogoImage);
    }


    public Image getCloseWindowButtonImage() {
        return closeWindowButtonImage.get();
    }

    public ObjectProperty<Image> closeWindowButtonImageProperty() {
        return closeWindowButtonImage;
    }

    public void setCloseWindowButtonImage(Image closeWindowButtonImage) {
        this.closeWindowButtonImage.set(closeWindowButtonImage);
    }

    public Image getMaximizeWindowButtonImage() {
        return maximizeWindowButtonImage.get();
    }

    public ObjectProperty<Image> maximizeWindowButtonImageProperty() {
        return maximizeWindowButtonImage;
    }

    public void setMaximizeWindowButtonImage(Image maximizeWindowButtonImage) {
        this.maximizeWindowButtonImage.set(maximizeWindowButtonImage);
    }

    public Image getMinimizeWindowButtonImage() {
        return minimizeWindowButtonImage.get();
    }

    public ObjectProperty<Image> minimizeWindowButtonImageProperty() {
        return minimizeWindowButtonImage;
    }

    public void setMinimizeWindowButtonImage(Image minimizeWindowButtonImage) {
        this.minimizeWindowButtonImage.set(minimizeWindowButtonImage);
    }

    public Image getOpenSettingsWindowButtonImage() {
        return openSettingsWindowButtonImage.get();
    }

    public ObjectProperty<Image> openSettingsWindowButtonImageProperty() {
        return openSettingsWindowButtonImage;
    }

    public void setOpenSettingsWindowButtonImage(Image openSettingsWindowButtonImage) {
        this.openSettingsWindowButtonImage.set(openSettingsWindowButtonImage);
    }

    public Image getReloadMainWindowButtonImage() {
        return reloadMainWindowButtonImage.get();
    }

    public ObjectProperty<Image> reloadMainWindowButtonImageProperty() {
        return reloadMainWindowButtonImage;
    }

    public void setReloadMainWindowButtonImage(Image reloadMainWindowButtonImage) {
        this.reloadMainWindowButtonImage.set(reloadMainWindowButtonImage);
    }

    public Image getOpenFullMenuButtonImage() {
        return openFullMenuButtonImage.get();
    }

    public ObjectProperty<Image> openFullMenuButtonImageProperty() {
        return openFullMenuButtonImage;
    }

    public void setOpenFullMenuButtonImage(Image openFullMenuButtonImage) {
        this.openFullMenuButtonImage.set(openFullMenuButtonImage);
    }

    public Image getHistoryDataMenuButtonImage() {
        return historyDataMenuButtonImage.get();
    }

    public ObjectProperty<Image> historyDataMenuButtonImageProperty() {
        return historyDataMenuButtonImage;
    }

    public void setHistoryDataMenuButtonImage(Image historyDataMenuButtonImage) {
        this.historyDataMenuButtonImage.set(historyDataMenuButtonImage);
    }

    public Image getScreenshotMenuButtonImage() {
        return screenshotMenuButtonImage.get();
    }

    public ObjectProperty<Image> screenshotMenuButtonImageProperty() {
        return screenshotMenuButtonImage;
    }

    public void setScreenshotMenuButtonImage(Image screenshotMenuButtonImage) {
        this.screenshotMenuButtonImage.set(screenshotMenuButtonImage);
    }

    public Image getWithdrawMenuButtonImage() {
        return withdrawMenuButtonImage.get();
    }

    public ObjectProperty<Image> withdrawMenuButtonImageProperty() {
        return withdrawMenuButtonImage;
    }

    public void setWithdrawMenuButtonImage(Image withdrawMenuButtonImage) {
        this.withdrawMenuButtonImage.set(withdrawMenuButtonImage);
    }

    public Image getToggleCollectStatusMenuButtonImage() {
        return toggleCollectStatusMenuButtonImage.get();
    }

    public ObjectProperty<Image> toggleCollectStatusMenuButtonImageProperty() {
        return toggleCollectStatusMenuButtonImage;
    }

    public void setToggleCollectStatusMenuButtonImage(Image toggleCollectStatusMenuButtonImage) {
        this.toggleCollectStatusMenuButtonImage.set(toggleCollectStatusMenuButtonImage);
    }

    public String getCollectCountDownText() {
        return collectCountDownText.get();
    }

    public SimpleStringProperty collectCountDownTextProperty() {
        return collectCountDownText;
    }

    public void setCollectCountDownText(String collectCountDownText) {
        this.collectCountDownText.set(collectCountDownText);
    }

    public boolean isCollectRunningFlag() {
        return collectRunningFlag.get();
    }

    public BooleanProperty collectRunningFlagProperty() {
        return collectRunningFlag;
    }

    public void setCollectRunningFlag(boolean collectRunningFlag) {
        this.collectRunningFlag.set(collectRunningFlag);
    }


    public String getManualUpdateStatus() {
        return manualUpdateStatus.get();
    }

    public StringProperty manualUpdateStatusProperty() {
        return manualUpdateStatus;
    }

    public void setManualUpdateStatus(String manualUpdateStatus) {
        this.manualUpdateStatus.set(manualUpdateStatus);
    }

    public Image getManualUpdateStatusButtonImage() {
        return manualUpdateStatusButtonImage.get();
    }

    public ObjectProperty<Image> manualUpdateStatusButtonImageProperty() {
        return manualUpdateStatusButtonImage;
    }

    public void setManualUpdateStatusButtonImage(Image manualUpdateStatusButtonImage) {
        this.manualUpdateStatusButtonImage.set(manualUpdateStatusButtonImage);
    }

    public Image getStopWarningButtonImage() {
        return stopWarningButtonImage.get();
    }

    public ObjectProperty<Image> stopWarningButtonImageProperty() {
        return stopWarningButtonImage;
    }

    public void setStopWarningButtonImage(Image stopWarningButtonImage) {
        this.stopWarningButtonImage.set(stopWarningButtonImage);
    }
}
