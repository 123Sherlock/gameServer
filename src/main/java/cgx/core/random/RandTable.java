package cgx.core.random;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 权重随机工具
 *
 * @param <K> 用来标记值的唯一key
 * @param <V> 值
 */
public class RandTable<K, V> {

    private final Map<K, RandObj> valMap = new HashMap<>();

    /**
     * 添加一个值
     *
     * @param key 唯一key
     * @param val 值
     * @param rate 权重
     */
    public void addVal(K key, V val, double rate) {
        valMap.put(key, new RandObj(val, rate));
    }

    /**
     * 移除一个值
     *
     * @param key 唯一key
     */
    public void removeVal(K key) {
        valMap.remove(key);
    }

    /**
     * 随机一个值
     *
     * @return 不存在时返回Optional.empty
     */
    public Optional<V> randVal() {
        if (valMap.isEmpty()) {
            return Optional.empty();
        }

        double rateSum = valMap.values().stream()
                .mapToDouble((o) -> o._rate)
                .sum();

        double randVal = ThreadLocalRandom.current().nextDouble(rateSum);
        double curVal = 0.0;

        for (RandObj obj : valMap.values()) {
            curVal += obj._rate;
            if (curVal > randVal) {
                return Optional.of(obj._val);
            }
        }
        return Optional.empty();
    }

    private class RandObj {

        RandObj(V val, double rate) {
            _val = val;
            _rate = rate;
        }

        V _val;
        double _rate;
    }
}
