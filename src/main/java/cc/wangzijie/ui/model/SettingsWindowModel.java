package cc.wangzijie.ui.model;

import cc.wangzijie.config.ConfigManager;
import cc.wangzijie.config.ServerConfig;
import cc.wangzijie.constants.ConfigKeys;
import cc.wangzijie.constants.Constants;
import cc.wangzijie.utils.IpHelper;
import cc.wangzijie.utils.StringSplitUtils;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class SettingsWindowModel {


    private final ObjectProperty<Image> closeWindowButtonImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> applySettingButtonImage = new SimpleObjectProperty<>();

    private final IntegerProperty intervalSeconds = new SimpleIntegerProperty();
    private final BooleanProperty asyncFlag = new SimpleBooleanProperty(true);

    private final StringProperty outputFolderPath = new SimpleStringProperty();
    private final BooleanProperty outputFolderEnabledFlag = new SimpleBooleanProperty(true);

    private final StringProperty callbackHookUrl = new SimpleStringProperty();
    private final BooleanProperty callbackHookEnabledFlag = new SimpleBooleanProperty(true);

    private final BooleanProperty databaseEnabledFlag = new SimpleBooleanProperty(true);

    private final StringProperty updateStatusHookUrl = new SimpleStringProperty();

    private final StringProperty localIp = new SimpleStringProperty();

    private final ObservableList<String> localIpList = FXCollections.observableArrayList(IpHelper.getServerIpList());

    private final ObjectProperty<Image> refreshIpListButtonImage = new SimpleObjectProperty<>();

    private final StringProperty stopWarningHookUrl = new SimpleStringProperty();

    private final StringProperty fieldNameOptions = new SimpleStringProperty();

    @Getter
    private final List<String> fieldNameOptionList = new LinkedList<>();

    public void init(ConfigManager configManager, ServerConfig serverConfig) {
        boolean savePropertiesFlag = false;

        // 设置#1.定时采集间隔（秒）
        String intervalSeconds = configManager.getProperty(ConfigKeys.KEY_INTERVAL_SECONDS);
        if (StringUtils.isBlank(intervalSeconds)) {
            intervalSeconds = String.valueOf(Constants.DEFAULT_INTERVAL_SECONDS);
            configManager.setProperty(ConfigKeys.KEY_INTERVAL_SECONDS, intervalSeconds);
            savePropertiesFlag = true;
        }
        this.setIntervalSeconds(Integer.parseInt(intervalSeconds));
        log.info("==== 初始化设置 ==== 定时采集间隔（秒）：{}", intervalSeconds);

        // 设置#2.输出文件夹路径
        String outputFolderPath = configManager.getProperty(ConfigKeys.KEY_OUTPUT_FOLDER_PATH);
        if (StringUtils.isBlank(outputFolderPath)) {
            outputFolderPath = Constants.DEFAULT_OUTPUT_FOLDER_PATH;
            configManager.setProperty(ConfigKeys.KEY_OUTPUT_FOLDER_PATH, outputFolderPath);
            savePropertiesFlag = true;
        }
        this.setOutputFolderPath(outputFolderPath);
        log.info("==== 初始化设置 ==== 输出文件夹路径：{}", outputFolderPath);

        // 设置#2.输出文件夹路径 - 是否启用
        String outputFolderEnabledFlag = configManager.getProperty(ConfigKeys.KEY_OUTPUT_FOLDER_ENABLED_FLAG);
        if (StringUtils.isBlank(outputFolderEnabledFlag)) {
            outputFolderEnabledFlag = Constants.TRUE;
            configManager.setProperty(ConfigKeys.KEY_OUTPUT_FOLDER_ENABLED_FLAG, outputFolderEnabledFlag);
            savePropertiesFlag = true;
        }
        this.setOutputFolderEnabledFlag(Objects.equals(outputFolderEnabledFlag, Constants.TRUE));
        log.info("==== 初始化设置 ==== 输出文件夹路径 - 是否启用：{}", outputFolderEnabledFlag);

        // 设置#3.Hook回调URL
        String callbackHookUrl = configManager.getProperty(ConfigKeys.KEY_CALLBACK_HOOK_URL);
        if (StringUtils.isBlank(callbackHookUrl)) {
            callbackHookUrl = serverConfig.buildUrl(Constants.DEFAULT_CALLBACK_HOOK_URI);
            configManager.setProperty(ConfigKeys.KEY_CALLBACK_HOOK_URL, callbackHookUrl);
            savePropertiesFlag = true;
        }
        this.setCallbackHookUrl(callbackHookUrl);
        log.info("==== 初始化设置 ==== Hook回调URL：{}", callbackHookUrl);

        // 设置#3.Hook回调URL - 是否启用
        String callbackHookEnabledFlag = configManager.getProperty(ConfigKeys.KEY_CALLBACK_HOOK_ENABLED_FLAG);
        if (StringUtils.isBlank(callbackHookEnabledFlag)) {
            callbackHookEnabledFlag = Constants.TRUE;
            configManager.setProperty(ConfigKeys.KEY_CALLBACK_HOOK_ENABLED_FLAG, callbackHookEnabledFlag);
            savePropertiesFlag = true;
        }
        this.setCallbackHookEnabledFlag(Objects.equals(callbackHookEnabledFlag, Constants.TRUE));
        log.info("==== 初始化设置 ==== Hook回调URL - 是否启用：{}", callbackHookEnabledFlag);

        // 设置#4.输出到本地SQLite数据库 - 是否启用
        String databaseEnabledFlag = configManager.getProperty(ConfigKeys.KEY_DATABASE_ENABLED_FLAG);
        if (StringUtils.isBlank(databaseEnabledFlag)) {
            databaseEnabledFlag = Constants.TRUE;
            configManager.setProperty(ConfigKeys.KEY_DATABASE_ENABLED_FLAG, databaseEnabledFlag);
            savePropertiesFlag = true;
        }
        this.setDatabaseEnabledFlag(Objects.equals(databaseEnabledFlag, Constants.TRUE));
        log.info("==== 初始化设置 ==== 输出到本地SQLite数据库 - 是否启用：{}", databaseEnabledFlag);

        // 设置#5.手动修改状态-回调URL
        String updateStatusHookUrl = configManager.getProperty(ConfigKeys.KEY_UPDATE_STATUS_HOOK_URL);
        if (StringUtils.isBlank(updateStatusHookUrl)) {
            updateStatusHookUrl = serverConfig.buildUrl(Constants.DEFAULT_UPDATE_STATUS_HOOK_URI);
            configManager.setProperty(ConfigKeys.KEY_UPDATE_STATUS_HOOK_URL, updateStatusHookUrl);
            savePropertiesFlag = true;
        }
        this.setUpdateStatusHookUrl(updateStatusHookUrl);
        log.info("==== 初始化设置 ==== 手动修改状态-回调URL：{}", updateStatusHookUrl);

        // 设置#6.关闭灯光报警-回调URL
        String stopWarningHookUrl = configManager.getProperty(ConfigKeys.KEY_STOP_WARNING_HOOK_URL);
        if (StringUtils.isBlank(stopWarningHookUrl)) {
            stopWarningHookUrl = serverConfig.buildUrl(Constants.DEFAULT_STOP_WARNING_HOOK_URI);
            configManager.setProperty(ConfigKeys.KEY_STOP_WARNING_HOOK_URL, stopWarningHookUrl);
            savePropertiesFlag = true;
        }
        this.setStopWarningHookUrl(stopWarningHookUrl);
        log.info("==== 初始化设置 ==== 关闭灯光报警-回调URL：{}", stopWarningHookUrl);

        // 设置#7.本机IP-选择网卡
        String localIp = configManager.getProperty(ConfigKeys.KEY_LOCAL_IP);
        if (StringUtils.isBlank(localIp)) {
            // 初始化配置文件
            localIp = IpHelper.LOCAL_IP;
            configManager.setProperty(ConfigKeys.KEY_LOCAL_IP, localIp);
            savePropertiesFlag = true;
        } else if (!this.getLocalIpList().contains(localIp)) {
            // 配置的本地IP当前不存在的，重新设置为默认本机IP
            log.info("==== 初始化设置 ==== 配置的本地IP[ {} ]当前不存在，重新设置为默认本机IP：{}",
                    localIp, IpHelper.LOCAL_IP);
            localIp = IpHelper.LOCAL_IP;
            configManager.setProperty(ConfigKeys.KEY_LOCAL_IP, localIp);
            savePropertiesFlag = true;
        }
        this.setLocalIp(localIp);
        log.info("==== 初始化设置 ==== 本机IP：{}", localIp);

        // 设置#8.数据信息字段名称-下拉候选项
        String fieldNameOptions = configManager.getProperty(ConfigKeys.KEY_FIELD_NAME_OPTIONS);
        if (StringUtils.isBlank(fieldNameOptions)) {
            fieldNameOptions = Constants.DEFAULT_FIELD_NAME_OPTIONS;
            configManager.setProperty(ConfigKeys.KEY_FIELD_NAME_OPTIONS, fieldNameOptions);
            savePropertiesFlag = true;
        }
        this.setFieldNameOptions(fieldNameOptions);
        log.info("==== 初始化设置 ==== 数据信息字段名称-下拉候选项：{}", fieldNameOptions);

        // 保存配置文件
        if (savePropertiesFlag) {
            configManager.saveProperties();
        }
    }

    public void buildFieldNameOptionList(String fieldNameOptions) {
        if (StringUtils.isBlank(fieldNameOptions)) {
            fieldNameOptions = Constants.DEFAULT_FIELD_NAME_OPTIONS;
        }
        this.fieldNameOptionList.clear();
        this.fieldNameOptionList.addAll(StringSplitUtils.splitToListByComma(fieldNameOptions));
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

    public String getStopWarningHookUrl() {
        return stopWarningHookUrl.get();
    }

    public StringProperty stopWarningHookUrlProperty() {
        return stopWarningHookUrl;
    }

    public void setStopWarningHookUrl(String stopWarningHookUrl) {
        this.stopWarningHookUrl.set(stopWarningHookUrl);
    }

    public String getFieldNameOptions() {
        return fieldNameOptions.get();
    }

    public StringProperty fieldNameOptionsProperty() {
        return fieldNameOptions;
    }

    public void setFieldNameOptions(String fieldNameOptions) {
        this.fieldNameOptions.set(fieldNameOptions);
        this.buildFieldNameOptionList(fieldNameOptions);
    }


    public boolean isAsyncFlag() {
        return asyncFlag.get();
    }

    public BooleanProperty asyncFlagProperty() {
        return asyncFlag;
    }

    public void setAsyncFlag(boolean syncFlag) {
        this.asyncFlag.set(syncFlag);
    }
}
