package cgx.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileUtils {

    public static final String USER_DIR = System.getProperty("user.dir");

    /**
     * 加载配置文件
     *
     * @param propertiesName 配置文件名称
     */
    public static Properties loadProperties(String propertiesName) {
        String propertyPath = USER_DIR + File.separator + "property" + File.separator + propertiesName;
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(propertyPath);
            properties.load(inputStream);
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
