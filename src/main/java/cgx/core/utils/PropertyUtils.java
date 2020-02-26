package cgx.core.utils;

import java.util.Properties;

public class PropertyUtils {

    private static final Properties PROPERTIES;

    static {
        PROPERTIES = FileUtils.loadProperties("game.properties");
    }

    public static Properties getProperties() {
        return PROPERTIES;
    }
}
