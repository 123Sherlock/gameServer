package cgx.core.dataconfig;

/**
 * 策划配置父类，所有配置类都要继承此类
 */
public class DataConfig {

    /**
     * 在一行配置初始化完后如果需要额外操作则复写此方法
     */
    public void afterOneRowSet() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    protected Integer id;
}
