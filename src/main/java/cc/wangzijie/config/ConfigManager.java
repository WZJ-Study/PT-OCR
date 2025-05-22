package cc.wangzijie.config;

import cc.wangzijie.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

@Slf4j
@Component
public class ConfigManager {
    private final Properties properties = new Properties();
    private final String filePath = "config.properties";

    public ConfigManager() {
        loadProperties();
    }

    private void loadProperties() {
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (Exception e) {
            log.error("Failed to load properties file.", e);
        }
    }

    public void saveProperties() {
        try (FileOutputStream output = new FileOutputStream(filePath)) {
            properties.store(output, "Updated at " + DateUtils.nowStr());
        } catch (Exception e) {
            log.error("Failed to save properties file.", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

//    public static void main(String[] args) {
//        ConfigManager manager = new ConfigManager();
//        manager.setProperty("password", "newpassword456"); // 修改配置项
//        manager.saveProperties(); // 保存配置文件
//    }
}
