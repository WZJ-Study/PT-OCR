package cc.wangzijie.ui.model;

import cc.wangzijie.constants.Constants;
import cc.wangzijie.utils.IpHelper;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.springframework.stereotype.Component;

@Component
public class SettingsWindowModel {


    private final ObjectProperty<Image> closeWindowButtonImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> applySettingButtonImage = new SimpleObjectProperty<>();

    private final IntegerProperty intervalSeconds = new SimpleIntegerProperty();

    private final StringProperty outputFolderPath = new SimpleStringProperty();
    private final BooleanProperty outputFolderEnabledFlag = new SimpleBooleanProperty(true);

    private final StringProperty callbackHookUrl = new SimpleStringProperty();
    private final BooleanProperty callbackHookEnabledFlag = new SimpleBooleanProperty(true);

    private final BooleanProperty databaseEnabledFlag = new SimpleBooleanProperty(true);

    private final StringProperty updateStatusHookUrl = new SimpleStringProperty();

    private final StringProperty localIp = new SimpleStringProperty();

    private final ObservableList<String> localIpList = FXCollections.observableArrayList(IpHelper.getServerIpList());

    private final ObjectProperty<Image> refreshIpListButtonImage = new SimpleObjectProperty<>();


    public Image getCloseWindowButtonImage() {
        return closeWindowButtonImage.get();
    }

    public ObjectProperty<Image> closeWindowButtonImageProperty() {
        return closeWindowButtonImage;
    }

    public void setCloseWindowButtonImage(Image closeWindowButtonImage) {
        this.closeWindowButtonImage.set(closeWindowButtonImage);
    }

    public Image getApplySettingButtonImage() {
        return applySettingButtonImage.get();
    }

    public ObjectProperty<Image> applySettingButtonImageProperty() {
        return applySettingButtonImage;
    }

    public void setApplySettingButtonImage(Image applySettingButtonImage) {
        this.applySettingButtonImage.set(applySettingButtonImage);
    }

    public int getIntervalSeconds() {
        return intervalSeconds.get();
    }

    public IntegerProperty intervalSecondsProperty() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(int intervalSeconds) {
        this.intervalSeconds.set(intervalSeconds);
    }

    public String getOutputFolderPath() {
        return outputFolderPath.get();
    }

    public StringProperty outputFolderPathProperty() {
        return outputFolderPath;
    }

    public void setOutputFolderPath(String outputFolderPath) {
        this.outputFolderPath.set(outputFolderPath);
    }

    public boolean isOutputFolderEnabledFlag() {
        return outputFolderEnabledFlag.get();
    }

    public BooleanProperty outputFolderEnabledFlagProperty() {
        return outputFolderEnabledFlag;
    }

    public void setOutputFolderEnabledFlag(boolean outputFolderEnabledFlag) {
        this.outputFolderEnabledFlag.set(outputFolderEnabledFlag);
    }

    public String getCallbackHookUrl() {
        return callbackHookUrl.get();
    }

    public StringProperty callbackHookUrlProperty() {
        return callbackHookUrl;
    }

    public void setCallbackHookUrl(String callbackHookUrl) {
        this.callbackHookUrl.set(callbackHookUrl);
    }

    public boolean isCallbackHookEnabledFlag() {
        return callbackHookEnabledFlag.get();
    }

    public BooleanProperty callbackHookEnabledFlagProperty() {
        return callbackHookEnabledFlag;
    }

    public void setCallbackHookEnabledFlag(boolean callbackHookEnabledFlag) {
        this.callbackHookEnabledFlag.set(callbackHookEnabledFlag);
    }

    public boolean isDatabaseEnabledFlag() {
        return databaseEnabledFlag.get();
    }

    public BooleanProperty databaseEnabledFlagProperty() {
        return databaseEnabledFlag;
    }

    public void setDatabaseEnabledFlag(boolean databaseEnabledFlag) {
        this.databaseEnabledFlag.set(databaseEnabledFlag);
    }

    public String getUpdateStatusHookUrl() {
        return updateStatusHookUrl.get();
    }

    public StringProperty updateStatusHookUrlProperty() {
        return updateStatusHookUrl;
    }

    public void setUpdateStatusHookUrl(String updateStatusHookUrl) {
        this.updateStatusHookUrl.set(updateStatusHookUrl);
    }

    public String getLocalIp() {
        return localIp.get();
    }

    public StringProperty localIpProperty() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp.set(localIp);
    }

    public ObservableList<String> getLocalIpList() {
        return localIpList;
    }

    public int getLocalIpIndex(String localIp) {
        return localIpList.indexOf(localIp);
    }

    public Image getRefreshIpListButtonImage() {
        return refreshIpListButtonImage.get();
    }

    public ObjectProperty<Image> refreshIpListButtonImageProperty() {
        return refreshIpListButtonImage;
    }

    public void setRefreshIpListButtonImage(Image refreshIpListButtonImage) {
        this.refreshIpListButtonImage.set(refreshIpListButtonImage);
    }
}
