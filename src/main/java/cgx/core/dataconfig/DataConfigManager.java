package cgx.core.dataconfig;

import cgx.core.utils.FileUtils;
import cgx.core.utils.PropertyUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DataConfigManager {

    @Autowired
    private ApplicationContext _applicationContext;

    public <T extends DataConfig> T get(Class<T> configType, Integer id) {
        return getConfigMap(configType).get(id);
    }

    public <T extends DataConfig> List<T> getAll(Class<? extends T> configType) {
        return ImmutableList.copyOf(getConfigMap(configType).values());
    }

    @SuppressWarnings("unchecked")
    private <T extends DataConfig> Map<Integer, T> getConfigMap(Class<T> configType) {
        return (Map<Integer, T>) _allTypeConfigMap.get(configType);
    }

    public void init() {
        for (DataConfig dataConfig : _applicationContext.getBeansOfType(DataConfig.class).values()) {
            Class<? extends DataConfig> configType = dataConfig.getClass();
            String jsonConfigStr = getStringFromJson(configType.getSimpleName());
            _allTypeConfigMap.put(configType, getDataConfigByJson(jsonConfigStr, configType));
        }
    }

    private <T extends DataConfig> Map<Integer, T> getDataConfigByJson(String json, Class<T> configType) {
        try {
            JSONObject jsonObj = JSON.parseObject(json);
            return jsonObj.keySet().stream()
                .map(jsonObj::getJSONObject)
                .map(o -> parseOneRow(o.toString(), configType))
                .collect(Collectors.toMap(DataConfig::getId, Function.identity(), throwingMerger()));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return ImmutableMap.of();
    }

    private <T extends DataConfig> T parseOneRow(String jsonStr, Class<T> configType) {
        T config = JSON.parseObject(jsonStr, configType);
        config.afterOneRowSet();
        return config;
    }

    private <T extends DataConfig> BinaryOperator<T> throwingMerger() {
        return (u, v) -> {
            String msg = String.format("DataConfig duplicate id:[%s:%s]", u.getClass().getSimpleName(), u.getId());
            throw new IllegalStateException(msg);
        };
    }

    private String getStringFromJson(String name) {
        String fileName = getDataConfigDir() + name + JSON_EXTENSION;
        String jsonStr = "";
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader((reader));

            String line = bufferedReader.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line);
                line = bufferedReader.readLine();
            }
            jsonStr = sb.toString();

            bufferedReader.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    private String getDataConfigDir() {
        String configDir = PropertyUtils.getProperties().getProperty("data_config_dir", "dataconfig");
        return FileUtils.USER_DIR + File.separator + configDir + File.separator;
    }

    private final Map<Class<?>, Map<Integer, ? extends DataConfig>> _allTypeConfigMap = new ConcurrentHashMap<>();

    private static final String JSON_EXTENSION = ".json";
}
