package cc.wangzijie.ui.view;


import cc.wangzijie.config.ServerConfig;
import cc.wangzijie.constants.Constants;
import cc.wangzijie.fxml.FxmlViews;
import cc.wangzijie.fxml.loader.SpringFxmlLoader;
import cc.wangzijie.ocr.component.TaskExecutor;
import cc.wangzijie.ocr.task.CallbackHookTask;
import cc.wangzijie.ocr.task.StatusHookTask;
import cc.wangzijie.server.entity.CallbackHookVO;
import cc.wangzijie.server.entity.OcrSection;
import cc.wangzijie.server.entity.OcrSectionResult;
import cc.wangzijie.server.entity.StatusHookVO;
import cc.wangzijie.server.service.IOcrSectionResultService;
import cc.wangzijie.server.service.IOcrSectionService;
import cc.wangzijie.utils.JacksonUtils;
import cc.wangzijie.utils.RetryHelper;
import cc.wangzijie.utils.SpringHelper;
import cc.wangzijie.ui.enums.ActionTypeEnum;
import cc.wangzijie.ui.helper.StageManager;
import cc.wangzijie.ui.model.*;
import cc.wangzijie.ui.screenshot.ScreenCaptureStage;
import cc.wangzijie.ui.utils.*;
import cc.wangzijie.ui.vo.ActionVO;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MainWindowView implements Initializable {

    @Resource
    private ServerConfig serverConfig;


    @Resource
    private IOcrSectionService ocrSectionService;

    @Resource
    private IOcrSectionResultService ocrSectionResultService;



    @Resource
    private StageManager stageManager;

    @Resource
    private SpringFxmlLoader springFxmlLoader;




    @Resource
    private MainWindowModel mainWindowModel;

    @Resource
    private SettingsWindowModel settingsWindowModel;

    @Resource
    private ScreenshotAreaModel screenshotAreaModel;

    @Resource
    private DataListAreaModel dataListAreaModel;

    @Resource
    private HistoryDataWindowModel historyDataWindowModel;

    @Resource
    private WithdrawModel withdrawModel;


    @FXML
    private ImageView mainWindowLogoImage;


    @FXML
    private ImageView closeWindowButtonImage;
    @FXML
    private ImageView maximizeWindowButtonImage;
    @FXML
    private ImageView minimizeWindowButtonImage;
    @FXML
    private ImageView openSettingsWindowButtonImage;
    @FXML
    private ImageView reloadMainWindowButtonImage;


    @FXML
    private ImageView openFullMenuButtonImage;
    @FXML
    private ImageView historyDataMenuButtonImage;
    @FXML
    private ImageView screenshotMenuButtonImage;
    @FXML
    private ImageView withdrawMenuButtonImage;
    @FXML
    private ImageView toggleCollectStatusMenuButtonImage;
    @FXML
    private Label collectCountDownText;

    @FXML
    private Button toggleCollectStatusMenuButton;

    @FXML
    public ChoiceBox<String> updateStatusInput;

    @FXML
    public ImageView updateStatusButtonImage;


    @FXML
    private StackPane screenshotImageStackPane;
    @FXML
    private ImageView screenshotImage;
    @FXML
    private Label screenshotImageHint;

    @FXML
    private ImageView dataListTitleBarMenuButtonImage;
    @FXML
    private Label dataListTitleBarMenuTitle;


    @FXML
    public TextField dataListTitleBarSearchInput;
    @FXML
    private ImageView dataListTitleBarSearchButtonImage;
    @FXML
    private ImageView dataListTitleBarDeleteButtonImage;
    @FXML
    private TableView<OcrSectionResult> ocrSectionTable;

    @FXML
    private TableColumn<OcrSectionResult, Boolean> varChecked;
    @FXML
    private TableColumn<OcrSectionResult, String> varName;
    @FXML
    private TableColumn<OcrSectionResult, String> varPosition;
    @FXML
    private TableColumn<OcrSectionResult, String> varType;
    @FXML
    private TableColumn<OcrSectionResult, String> varValue;

    private double offsetX;
    private double offsetY;

    private RectBuilder rectBuilder;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 绑定FXML组件与model属性 - 标题栏logo
        mainWindowLogoImage.imageProperty().bindBidirectional(mainWindowModel.mainWindowLogoImageProperty());

        // 绑定FXML组件与model属性 - 标题栏右侧窗口按钮
        closeWindowButtonImage.imageProperty().bindBidirectional(mainWindowModel.closeWindowButtonImageProperty());
        maximizeWindowButtonImage.imageProperty().bindBidirectional(mainWindowModel.maximizeWindowButtonImageProperty());
        minimizeWindowButtonImage.imageProperty().bindBidirectional(mainWindowModel.minimizeWindowButtonImageProperty());
        openSettingsWindowButtonImage.imageProperty().bindBidirectional(mainWindowModel.openSettingsWindowButtonImageProperty());
        reloadMainWindowButtonImage.imageProperty().bindBidirectional(mainWindowModel.reloadMainWindowButtonImageProperty());

        // 绑定FXML组件与model属性 - 菜单栏按钮
        openFullMenuButtonImage.imageProperty().bindBidirectional(mainWindowModel.openFullMenuButtonImageProperty());
        historyDataMenuButtonImage.imageProperty().bindBidirectional(mainWindowModel.historyDataMenuButtonImageProperty());
        screenshotMenuButtonImage.imageProperty().bindBidirectional(mainWindowModel.screenshotMenuButtonImageProperty());
        withdrawMenuButtonImage.imageProperty().bindBidirectional(mainWindowModel.withdrawMenuButtonImageProperty());
        toggleCollectStatusMenuButtonImage.imageProperty().bindBidirectional(mainWindowModel.toggleCollectStatusMenuButtonImageProperty());
        collectCountDownText.textProperty().bindBidirectional(mainWindowModel.collectCountDownTextProperty());

        withdrawMenuButtonImage.disableProperty().bind(withdrawModel.canWithdrawFlagProperty());
        toggleCollectStatusMenuButton.disableProperty().bindBidirectional(screenshotAreaModel.screenshotAreaNoImageFlagProperty());

        updateStatusInput.setItems(FXCollections.observableList(Constants.MANUAL_UPDATE_STATUS_LIST));
        updateStatusInput.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            log.info("==== 手动选择当前状态 ==== {}", newValue);
            if (StringUtils.isNotBlank(newValue)) {
                mainWindowModel.setManualUpdateStatus(newValue);
            }
        });

        updateStatusButtonImage.imageProperty().bindBidirectional(mainWindowModel.manualUpdateStatusButtonImageProperty());

        // 绑定FXML组件与model属性 - 主界面 - 左侧截屏图片预览
        screenshotImage.imageProperty().bindBidirectional(screenshotAreaModel.screenshotImageProperty());
        screenshotImage.cursorProperty().bindBidirectional(screenshotAreaModel.screenshotImageCursorProperty());
        screenshotImageHint.textProperty().bindBidirectional(screenshotAreaModel.screenshotImageHintProperty());
        screenshotImageHint.visibleProperty().bindBidirectional(screenshotAreaModel.screenshotImageHintVisibleProperty());

        // 绑定FXML组件与model属性 - 主界面 - 右侧数据列表 - 标题栏
        dataListTitleBarMenuButtonImage.imageProperty().bindBidirectional(dataListAreaModel.dataListTitleBarMenuButtonImageProperty());
        dataListTitleBarMenuTitle.textProperty().bind(dataListAreaModel.dataListTitleBarMenuTitleProperty());
        dataListTitleBarSearchButtonImage.imageProperty().bindBidirectional(dataListAreaModel.dataListTitleBarSearchButtonImageProperty());
        dataListTitleBarDeleteButtonImage.imageProperty().bindBidirectional(dataListAreaModel.dataListTitleBarDeleteButtonImageProperty());
        dataListTitleBarSearchInput.textProperty().bindBidirectional(dataListAreaModel.searchTextProperty());

        // 绑定FXML组件与model属性 - 主界面 - 右侧数据列表
        ocrSectionTable.itemsProperty().bindBidirectional(dataListAreaModel.searchedOcrSectionResultListProperty());
        varChecked.setCellValueFactory(new PropertyValueFactory<>("checkedFlag"));
        varName.setCellValueFactory(new PropertyValueFactory<>("name"));
        varPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        varType.setCellValueFactory(new PropertyValueFactory<>("type"));
        varValue.setCellValueFactory(new PropertyValueFactory<>("value"));

        varChecked.setCellFactory(tableColumn -> new CheckBoxTableCell<>(index -> {
            final OcrSectionResult r = ocrSectionTable.getItems().get(index);
            boolean checked = r.getCheckedFlag() != null && r.getCheckedFlag();
            ObservableValue<Boolean> property = new SimpleBooleanProperty(r, "checkedFlag", checked);
            property.addListener((observable, oldValue, newValue) -> r.setCheckedFlag(newValue));
            return property;
        }));
        varName.setCellFactory(tableColumn -> new CustomTextFieldTableCell());
        varType.setCellFactory(tableColumn -> new CustomComboBoxTableCell(Constants.DATA_TYPE_LIST));

        varName.setOnEditCommit(
                event -> {
                    int row = event.getTablePosition().getRow();
                    OcrSectionResult r = event.getTableView().getItems().get(row);
                    r.setName(event.getNewValue());
                    onSectionEdit(r);
                });
        varType.setOnEditCommit(
                event -> {
                    int row = event.getTablePosition().getRow();
                    OcrSectionResult r = event.getTableView().getItems().get(row);
                    r.setType(event.getNewValue());
                    onSectionEdit(r);
                });

        // 初始化数据列表
        dataListAreaModel.setOcrSectionResultList(FXCollections.observableArrayList());
        dataListAreaModel.setSearchedOcrSectionResultList(FXCollections.observableArrayList());

        // 处理model属性 - 标题栏logo
        mainWindowModel.setMainWindowLogoImage(ImageLoader.load(Constants.LOGO_IMAGE_PATH));

        // 处理model属性 - 标题栏右侧窗口按钮
        mainWindowModel.setCloseWindowButtonImage(ImageLoader.load(Constants.CLOSE_IMAGE_PATH));
        mainWindowModel.setMaximizeWindowButtonImage(ImageLoader.load(Constants.MAXIMIZE_IMAGE_PATH));
        mainWindowModel.setMinimizeWindowButtonImage(ImageLoader.load(Constants.MINIMIZE_IMAGE_PATH));
        mainWindowModel.setOpenSettingsWindowButtonImage(ImageLoader.load(Constants.SETTINGS_IMAGE_PATH));
        mainWindowModel.setReloadMainWindowButtonImage(ImageLoader.load(Constants.RELOAD_IMAGE_PATH));

        // 处理model属性 - 菜单栏按钮
        mainWindowModel.setOpenFullMenuButtonImage(ImageLoader.load(Constants.DRAG_IMAGE_PATH));
        mainWindowModel.setHistoryDataMenuButtonImage(ImageLoader.load(Constants.HISTORY_DATA_IMAGE_PATH));
        mainWindowModel.setScreenshotMenuButtonImage(ImageLoader.load(Constants.SCREENSHOT_BLUE_IMAGE_PATH));
        mainWindowModel.setWithdrawMenuButtonImage(ImageLoader.load(Constants.WITHDRAW_WHITE_IMAGE_PATH));
        mainWindowModel.setToggleCollectStatusMenuButtonImage(ImageLoader.load(Constants.RUN_BLUE_IMAGE_PATH));
        mainWindowModel.setCollectCountDownText("已停止");
        mainWindowModel.setCollectRunningFlag(false);
        mainWindowModel.setManualUpdateStatusButtonImage(ImageLoader.load(Constants.SEND_IMAGE_PATH));

        // 处理model属性 - 主界面 - 左侧截屏图片预览区域
        screenshotAreaModel.setScreenshotImage(ImageLoader.load(Constants.SCREEN_CAPTURE_IMAGE_PATH));
        screenshotAreaModel.setScreenshotImageCursor(Cursor.DEFAULT);
        screenshotAreaModel.setScreenshotImageHint(Constants.SCREENSHOT_IMAGE_HINT);
        screenshotAreaModel.setScreenshotImageHintVisible(true);
        screenshotAreaModel.setScreenshotAreaNoImageFlag(true);

        // 处理model属性 - 主界面 - 右侧数据列表区域 - 标题栏
        dataListAreaModel.setDataListTitleBarMenuButtonImage(ImageLoader.load(Constants.DRAG_IMAGE_PATH));
        dataListAreaModel.setDataListTitleBarMenuTitle(Constants.DATA_LIST_TITLE);
        dataListAreaModel.setDataListTitleBarSearchButtonImage(ImageLoader.load(Constants.SEARCH_IMAGE_PATH));
        dataListAreaModel.setDataListTitleBarDeleteButtonImage(ImageLoader.load(Constants.DELETE_IMAGE_PATH));
    }


    private void onSectionEdit(OcrSectionResult result) {
        this.screenshotAreaModel.getOcrManager().onSectionEdit(result.getPosition(), result.getName(), result.getType());
        this.ocrSectionService.updateNameTypeById(result.getSectionId(), result.getName(), result.getType());
    }


    @FXML
    protected void onMainWindowMousePressed(MouseEvent event) {
        this.offsetX = event.getSceneX();
        this.offsetY = event.getSceneY();
    }

    @FXML
    protected void onMainWindowMouseDragged(MouseEvent event) {
        Stage stage = stageManager.getMainWindowStage();
        if (null != stage) {
            Platform.runLater(() -> {
                stage.setX(event.getScreenX() - offsetX);
                stage.setY(event.getScreenY() - offsetY);
            });
        }
    }

    @FXML
    protected void onCloseWindowButtonClick() {
        log.info("==== onCloseWindowButtonClick ==== 点击【关闭窗口】按钮，退出！");
        SpringHelper.close();
        Platform.exit();
    }

    @FXML
    protected void onMaximizeWindowButtonClick() {
        log.info("==== onMaximizeWindowButtonClick ==== 点击【最大化窗口/恢复窗口大小】按钮！");
        Stage stage = stageManager.getMainWindowStage();
        if (null != stage) {
            if (WindowSizeHolder.isMaximized()) {
                // 关闭全屏
                log.info("==== onMaximizeWindowButtonClick ==== 恢复窗口大小！");
                Platform.runLater(() -> {
                    WindowSizeHolder.restore(stage);
                });
                mainWindowModel.setMaximizeWindowButtonImage(ImageLoader.load(Constants.MAXIMIZE_IMAGE_PATH));
            } else {
                // 全屏
                log.info("==== onMaximizeWindowButtonClick ==== 最大化窗口！");
                WindowSizeHolder.backup(stage);
                Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
                log.info("==== WindowSizeHolder ==== 最大化的窗口位置大小：visualBounds={}", visualBounds);
                Platform.runLater(() -> {
                    stage.setX(visualBounds.getMinX());
                    stage.setY(visualBounds.getMinY());
                    stage.setWidth(visualBounds.getWidth());
                    stage.setHeight(visualBounds.getHeight());
                });
                mainWindowModel.setMaximizeWindowButtonImage(ImageLoader.load(Constants.RESIZE_IMAGE_PATH));
            }
        }
    }

    @FXML
    protected void onMinimizeWindowButtonClick() {
        log.info("==== onMinimizeWindowButtonClick ==== 点击【最小化窗口】按钮！");
        Stage stage = stageManager.getMainWindowStage();
        if (null != stage && !stage.isIconified()) {
            Platform.runLater(() -> stage.setIconified(true));
        }
    }

    @FXML
    protected void onOpenSettingsWindowButtonClick() {
        log.info("==== onOpenSettingsWindowButtonClick ==== 点击【打开[设置]窗口】按钮！");
        Stage settingsWindowStage = stageManager.getSettingsWindowStage();
        if (null == settingsWindowStage) {
            // 首次打开设置窗口，初始化
            settingsWindowStage = new Stage();
            springFxmlLoader.loadTo(FxmlViews.SETTINGS_WINDOW, settingsWindowStage);
            stageManager.setSettingsWindowStage(settingsWindowStage);
        } else {
            // 打开设置窗口
            Platform.runLater(settingsWindowStage::show);
        }
    }

    @FXML
    protected void onReloadButtonClick() {
        log.info("==== onReloadButtonClick ==== 点击【重新加载】按钮！");
        if (mainWindowModel.isCollectRunningFlag()) {
            log.error("采集正在运行中，不可重新加载！");
            return;
        }
        screenshotAreaModel.setScreenshotImage(ImageLoader.load(Constants.SCREEN_CAPTURE_IMAGE_PATH));
        screenshotAreaModel.setScreenshotImageCursor(Cursor.DEFAULT);
        screenshotAreaModel.setScreenshotImageHintVisible(true);
        screenshotAreaModel.setScreenshotAreaNoImageFlag(true);
        this.onReload();
    }

    public void onReload() {
        // 删除所有选中框
        screenshotImageStackPane.getChildren().removeAll(screenshotAreaModel.getRectList());
        screenshotAreaModel.clearRectMap();
        // 删除所有OCR识别区域
        screenshotAreaModel.getOcrManager().clearOcrSection();
        // 删除所有数据列表
        dataListAreaModel.clearDataMap();
        // 重置撤回动作队列
        withdrawModel.resetActionDeque();
    }

    @FXML
    protected void onOpenFullMenuButtonClick() {
        log.info("==== onOpenFullMenuButtonClick ==== 点击【菜单栏】按钮！");

    }


    @FXML
    protected void onHistoryDataMenuButtonClick() {
        log.info("==== onHistoryDataMenuButtonClick ==== 点击【历史数据】按钮！");
        Stage historyDataWindowStage = stageManager.getHistoryDataWindowStage();
        if (null == historyDataWindowStage) {
            // 首次打开历史数据窗口，初始化
            historyDataWindowStage = new Stage();
            springFxmlLoader.loadTo(FxmlViews.HISTORY_DATA_WINDOW, historyDataWindowStage);
            stageManager.setHistoryDataWindowStage(historyDataWindowStage);
        } else {
            // 打开历史数据窗口
            Platform.runLater(historyDataWindowStage::show);
        }
        // 重置搜索区域，执行搜索
        historyDataWindowModel.initSearchArea();
        historyDataWindowModel.doSearch();
    }

    @FXML
    protected void onScreenshotMenuButtonClick() {
        log.info("==== onScreenshotMenuButtonClick ==== 点击【截屏】按钮！");
        Stage stage = stageManager.getMainWindowStage();
        ScreenCaptureStage screenCaptureStage = new ScreenCaptureStage(stage, this, screenshotAreaModel);
        screenCaptureStage.show();
    }

    @FXML
    protected void onWithdrawMenuButtonClick() {
        log.info("==== onWithdrawMenuButtonClick ==== 点击【撤回上一步】按钮！");
        if (!withdrawModel.isCanWithdrawFlag()) {
            log.info("没有可撤回的动作，跳过！");
        }
        withdrawModel.doWithdraw();
    }

    @FXML
    protected void onToggleCollectStatusMenuButtonClick() {
        if (mainWindowModel.isCollectRunningFlag()) {
            // 采集中，结束采集
            log.info("==== onToggleCollectStatusMenuButtonClick ==== 点击【结束采集】按钮！");
            screenshotAreaModel.getOcrManager().stop();
            // 按钮变为【开始采集】按钮
            mainWindowModel.setToggleCollectStatusMenuButtonImage(ImageLoader.load(Constants.RUN_BLUE_IMAGE_PATH));
            mainWindowModel.setCollectRunningFlag(false);
        } else {
            // 开始采集
            log.info("==== onToggleCollectStatusMenuButtonClick ==== 点击【开始采集】按钮！");
            screenshotAreaModel.getOcrManager().start();
            // 按钮变为【结束采集】按钮
            mainWindowModel.setToggleCollectStatusMenuButtonImage(ImageLoader.load(Constants.STOP_RED_IMAGE_PATH));
            mainWindowModel.setCollectRunningFlag(true);
        }
    }


    @FXML
    protected void onUpdateStatusButtonClick() {
        log.info("==== onUpdateStatusButtonClick ==== 点击【手动修改状态】按钮！");
        String newStatus = mainWindowModel.getManualUpdateStatus();
        if (StringUtils.isBlank(newStatus)) {
            log.error("未选择【手动更新状态】，跳过！");
            return;
        }
        // 触发回调钩子
        TaskExecutor.execute(new StatusHookTask(newStatus, this.settingsWindowModel, this.serverConfig));
    }

    @FXML
    protected void onScreenshotImageMouseClicked(MouseEvent event) {
        event.consume();
        if (event.getButton() == MouseButton.PRIMARY) {
            if (screenshotAreaModel.isScreenshotAreaNoImageFlag()) {
                // 触发截图功能
                log.debug("==== onScreenshotImageMouseClicked ==== [鼠标左键]点击[截图区域]，尚无截图，触发截图功能！\nevent button={}\nevent x={}, y={}\nscene x={} y={}\nscreen x={} y={}",
                        event.getButton(), event.getX(), event.getY(), event.getSceneX(), event.getSceneY(), event.getScreenX(), event.getScreenY());
                Stage stage = stageManager.getMainWindowStage();
                ScreenCaptureStage screenCaptureStage = new ScreenCaptureStage(stage, this, screenshotAreaModel);
                screenCaptureStage.show();
            }
        }
    }

    @FXML
    protected void onScreenshotImageMousePressed(MouseEvent event) {
        event.consume();
        if (event.getButton() == MouseButton.PRIMARY) {
            if (!screenshotAreaModel.isScreenshotAreaNoImageFlag()) {
                log.debug("==== onScreenshotImageMousePressed ==== [鼠标左键]按下，[截图区域]已有截图，开始创建[框选区域]！\nevent button={}\nevent x={}, y={}\nscene x={} y={}\nscreen x={} y={}",
                        event.getButton(), event.getX(), event.getY(), event.getSceneX(), event.getSceneY(), event.getScreenX(), event.getScreenY());
                this.rectBuilder = new RectBuilder(screenshotImageStackPane, screenshotAreaModel);
                this.rectBuilder.onMousePressed(event);
            }
        }
    }


    @FXML
    protected void onScreenshotImageMouseDragged(MouseEvent event) {
        event.consume();
        if (event.getButton() == MouseButton.PRIMARY) {
            if (!screenshotAreaModel.isScreenshotAreaNoImageFlag()) {
                log.debug("==== onScreenshotImageMouseDragged ==== [鼠标左键]按下后拖拽，[截图区域]已有截图，调整正在创建的[框选区域]！\nevent button={}\nevent x={}, y={}\nscene x={} y={}\nscreen x={} y={}",
                        event.getButton(), event.getX(), event.getY(), event.getSceneX(), event.getSceneY(), event.getScreenX(), event.getScreenY());
                this.rectBuilder.onMouseDragged(event);
            }
        }
    }


    @FXML
    protected void onScreenshotImageMouseReleased(MouseEvent event) {
        event.consume();
        if (event.getButton() == MouseButton.PRIMARY) {
            if (!screenshotAreaModel.isScreenshotAreaNoImageFlag()) {
                log.debug("==== onScreenshotImageMouseReleased ==== [鼠标左键]按下后释放，[截图区域]已有截图，完成正在创建的[框选区域]！\nevent button={}\nevent x={}, y={}\nscene x={} y={}\nscreen x={} y={}",
                        event.getButton(), event.getX(), event.getY(), event.getSceneX(), event.getSceneY(), event.getScreenX(), event.getScreenY());
                Rectangle rect = this.rectBuilder.onMouseReleased(event);
                OcrSection ocrSection = ocrSectionService.createNewSection(rect);
                String key = ocrSection.displayPosition();
                // 注册选中框
                screenshotAreaModel.addRect(key, rect);
                // 注册OCR识别区域
                screenshotAreaModel.getOcrManager().addOcrSection(ocrSection);
                // 注册数据列表
                OcrSectionResult ocrSectionResult = ocrSection.newResult(null);
                ocrSectionResultService.save(ocrSectionResult);
                dataListAreaModel.addData(key, ocrSectionResult);

                // 记录动作 - 新增OCR识别区域
                ActionVO action = new ActionVO();
                action.setType(ActionTypeEnum.ADD_RECT);
                action.setKey(key);
                action.setRect(rect);
                action.setOcrSection(ocrSection);
                withdrawModel.addAction(action);
            }
        }
    }




    @FXML
    protected void onDataListTitleBarMenuButtonClick() {
        log.info("==== onDataListTitleBarMenuButtonClick ==== 点击【数据列表区域-菜单栏】按钮！");
        dataListAreaModel.toggleSelectAllFlag();
        varChecked.setCellFactory(tableColumn -> new CheckBoxTableCell<>(index -> {
            final OcrSectionResult r = ocrSectionTable.getItems().get(index);
            boolean checked = r.getCheckedFlag() != null && r.getCheckedFlag();
            ObservableValue<Boolean> property = new SimpleBooleanProperty(r, "checkedFlag", checked);
            property.addListener((observable, oldValue, newValue) -> r.setCheckedFlag(newValue));
            return property;
        }));
    }

    @FXML
    protected void onDataListTitleBarSearchButtonClick() {
        log.info("==== onDataListTitleBarSearchButtonClick ==== 点击【数据列表区域-菜单栏-搜索】按钮！");
        dataListAreaModel.filterData();
    }

    @FXML
    protected void onDataListTitleBarDeleteButtonClick() {
        log.info("==== onDataListTitleBarDeleteButtonClick ==== 点击【数据列表区域-菜单栏-删除】按钮！");
        FilteredList<OcrSectionResult> checkedList = dataListAreaModel.getOcrSectionResultList().filtered(OcrSectionResult::getCheckedFlag);
        Set<String> removeKeySet = checkedList.stream().map(OcrSectionResult::getPosition).collect(Collectors.toSet());
        for (String removeKey : removeKeySet) {
            log.info("==== onDataListTitleBarDeleteButtonClick ==== 执行删除：{}", removeKey);
            // 删除选中框
            Rectangle rect = screenshotAreaModel.removeRect(removeKey);
            screenshotImageStackPane.getChildren().remove(rect);
            // 删除OCR识别区域
            OcrSection ocrSection = screenshotAreaModel.getOcrManager().removeOcrSection(removeKey);
            // 删除数据列表行
            dataListAreaModel.removeData(removeKey);

            // 记录动作 - 删除OCR识别区域
            ActionVO action = new ActionVO();
            action.setType(ActionTypeEnum.REMOVE_RECT);
            action.setKey(removeKey);
            action.setRect(rect);
            action.setOcrSection(ocrSection);
            withdrawModel.addAction(action);
        }
    }



    public void doWithdrawAddRect(ActionVO action) {
        // 删除选中框
        Rectangle rect = screenshotAreaModel.removeRect(action.getKey());
        screenshotImageStackPane.getChildren().remove(rect);
        // 删除OCR识别区域
        screenshotAreaModel.getOcrManager().removeOcrSection(action.getKey());
        // 删除数据列表行
        dataListAreaModel.removeData(action.getKey());
    }

    public void doWithdrawRemoveRect(ActionVO action) {
        // 注册选中框
        screenshotAreaModel.addRect(action.getKey(), action.getRect());
        // 注册OCR识别区域
        screenshotAreaModel.getOcrManager().addOcrSection(action.getOcrSection());
        // 注册数据列表
        OcrSectionResult ocrSectionResult = action.getOcrSection().newResult(null);
        ocrSectionResultService.save(ocrSectionResult);
        dataListAreaModel.addData(action.getKey(), ocrSectionResult);
    }
}
