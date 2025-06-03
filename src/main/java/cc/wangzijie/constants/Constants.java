package cc.wangzijie.constants;

import cc.wangzijie.utils.DateFormat;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    // 默认时间间隔：10s
    public static final int DEFAULT_INTERVAL_SECONDS = 5;
    public static final int MIN_INTERVAL_SECONDS = 5;
    public static final int MAX_INTERVAL_SECONDS = 120;

    public static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern(DateFormat.YYYY_MM_DD_HH_MM_SS);

    public static final String DEFAULT_OUTPUT_FOLDER_PATH = System.getProperty("user.dir") + File.separator + "Screenshots";
    public static final String IMAGE_FORMAT = "png";
    public static final String IMAGE_FORMAT_WITH_DOT = ".png";

    public static final String DEFAULT_CALLBACK_HOOK_URI = "/ocr/callback";
    public static final String DEFAULT_UPDATE_STATUS_HOOK_URI = "/ocr/update-status";
    public static final String DEFAULT_STOP_WARNING_HOOK_URI = "/ocr/stop-warning";

    public static final String MAIN_WINDOW_TITLE = "PT.OCR v0.0.1";
    public static final String SETTINGS_WINDOW_TITLE = "设置";
    public static final String HISTORY_DATA_WINDOW_TITLE = "历史数据";

    public static final String SCREENSHOT_IMAGE_HINT = "点击【截取】按钮，即可截取屏幕";
    public static final String DATA_LIST_TITLE = "数据信息";

    public static final List<String> DATA_TYPE_LIST = Arrays.asList("文本", "数字");

    public static final List<String> MANUAL_UPDATE_STATUS_LIST = Arrays.asList("运行（升温）", "运行（保温）", "运行（试验中）", "待机", "停止");

    /*
     * Logo图片
     */
    public static final String LOGO_IMAGE_PATH = "/icons/logo/Logo.png";

    /*
     * 窗口控制按钮图片
     */
    public static final String CLOSE_IMAGE_PATH = "/icons/window/Close.png";
    public static final String MAXIMIZE_IMAGE_PATH = "/icons/window/Maximize.png";
    public static final String MINIMIZE_IMAGE_PATH = "/icons/window/Minimize.png";
    public static final String RESIZE_IMAGE_PATH = "/icons/window/Resize.png";

    /*
     * 菜单功能按钮图片
     */
    public static final String RELOAD_IMAGE_PATH = "/icons/menu/Reload.png";
    public static final String RELOAD_BLACK_IMAGE_PATH = "/icons/menu/Reload_Black.png";
    public static final String SETTINGS_IMAGE_PATH = "/icons/menu/Settings.png";
    public static final String DRAG_IMAGE_PATH = "/icons/menu/Drag.png";
    public static final String HISTORY_DATA_IMAGE_PATH = "/icons/menu/HistoryData.png";
    public static final String SEND_IMAGE_PATH = "/icons/menu/Send.png";

    /*
     * 菜单功能按钮图片 - 截屏
     */
    public static final String SCREENSHOT_BLUE_IMAGE_PATH = "/icons/menu/screenshot/Screenshot_Blue.png";
    public static final String SCREENSHOT_GREY_IMAGE_PATH = "/icons/menu/screenshot/Screenshot_Grey.png";
    public static final String SCREENSHOT_RED_IMAGE_PATH = "/icons/menu/screenshot/Screenshot_Red.png";
    public static final String SCREENSHOT_WHITE_IMAGE_PATH = "/icons/menu/screenshot/Screenshot_White.png";

    /*
     * 菜单功能按钮图片 - 撤回上一步操作
     */
    public static final String WITHDRAW_BLUE_IMAGE_PATH = "/icons/menu/withdraw/Withdraw_Blue.png";
    public static final String WITHDRAW_GREY_IMAGE_PATH = "/icons/menu/withdraw/Withdraw_Grey.png";
    public static final String WITHDRAW_RED_IMAGE_PATH = "/icons/menu/withdraw/Withdraw_Red.png";
    public static final String WITHDRAW_WHITE_IMAGE_PATH = "/icons/menu/withdraw/Withdraw_White.png";


    /*
     * 菜单功能按钮图片 - 开始
     */
    public static final String RUN_BLUE_IMAGE_PATH = "/icons/menu/run/Run_Blue.png";
    public static final String RUN_GREY_IMAGE_PATH = "/icons/menu/run/Run_Grey.png";
    public static final String RUN_RED_IMAGE_PATH = "/icons/menu/run/Run_Red.png";
    public static final String RUN_WHITE_IMAGE_PATH = "/icons/menu/run/Run_White.png";

    /*
     * 菜单功能按钮图片 - 暂停
     */
    public static final String PAUSE_BLUE_IMAGE_PATH = "/icons/menu/pause/Pause_Blue.png";
    public static final String PAUSE_GREY_IMAGE_PATH = "/icons/menu/pause/Pause_Grey.png";
    public static final String PAUSE_RED_IMAGE_PATH = "/icons/menu/pause/Pause_Red.png";
    public static final String PAUSE_WHITE_IMAGE_PATH = "/icons/menu/pause/Pause_White.png";

    /*
     * 菜单功能按钮图片 - 结束
     */
    public static final String STOP_BLUE_IMAGE_PATH = "/icons/menu/stop/Stop_Blue.png";
    public static final String STOP_GREY_IMAGE_PATH = "/icons/menu/stop/Stop_Grey.png";
    public static final String STOP_RED_IMAGE_PATH = "/icons/menu/stop/Stop_Red.png";
    public static final String STOP_WHITE_IMAGE_PATH = "/icons/menu/stop/Stop_White.png";


    /*
     * 主界面图片 - 左侧截屏图片预览区域
     */
    public static final String SCREEN_CAPTURE_IMAGE_PATH = "/icons/main/ScreenCapture.png";

    /*
     * 主界面图片 - 右侧数据列表区域
     */
    public static final String SEARCH_IMAGE_PATH = "/icons/main/Search.png";
    public static final String DELETE_IMAGE_PATH = "/icons/main/Delete.png";

    /*
     * 设置窗口图片 - 应用设置按钮
     */
    public static final String APPLY_IMAGE_PATH = "/icons/settings/Apply.png";
}
