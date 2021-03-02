package cgx.logic.item;

import cgx.core.dataconfig.DataConfig;
import org.springframework.stereotype.Component;

/**
 * 背包道具配置
 */
@Component
public class BagItemCfg extends DataConfig {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPileLimit() {
        return pileLimit;
    }

    public void setPileLimit(int pileLimit) {
        this.pileLimit = pileLimit;
    }

    /**
     * 名称
     */
    private String name;

    /**
     * 堆叠上限
     */
    private int pileLimit;
}
