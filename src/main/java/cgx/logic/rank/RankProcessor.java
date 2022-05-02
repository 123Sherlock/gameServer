package cgx.logic.rank;

import cgx.core.dataconfig.DataConfigManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 排行榜处理器父类
 * @param <T> 每个子类自定义RankDb的子类
 */
public abstract class RankProcessor<T extends RankDb> {

    protected SortedSet<T> rankSet;

    protected ReadWriteLock lock = new ReentrantReadWriteLock();
    protected Lock readLock = lock.readLock();
    protected Lock wirteLock = lock.writeLock();

    protected abstract RankType getRankType();

    public void init() {
        rankSet = new TreeSet<>(getComparator());
        ParameterizedType superParamType = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> dbType = (Class<T>) superParamType.getActualTypeArguments()[0];
        rankSet.addAll(rankDbGetter.getRankDbList(dbType));
    }

    public Set<T> getAllRanks() {
        readLock.lock();
        try {
            return Set.copyOf(rankSet);
        } finally {
            readLock.unlock();
        }
    }

    public void addRank(T newRank) {
        RankCfg rankCfg = getConfig();
        if (!canAddRank(newRank, rankCfg)) {
            return;
        }

        wirteLock.lock();
        try {
            Optional<T> playerRank = findPlayerRank(newRank.getPlayerId());
            if (playerRank.isPresent()) {
                T oldRank = playerRank.get();
                if (!needUpdate(oldRank, newRank)) {
                    return;
                }
                rankSet.remove(oldRank);
            }
            rankSet.add(newRank);

            if (rankSet.size() > rankCfg.getSize()) {
                rankSet.remove(rankSet.last());
            }
        } finally {
            wirteLock.unlock();
        }
    }

    protected Comparator<T> getComparator() {
        return Comparator.comparing(T::getScore).reversed();
    }

    protected RankCfg getConfig() {
        return dataConfigManager.get(RankCfg.class, getRankType().getId());
    }

    protected boolean canAddRank(T rankDb, RankCfg rankCfg) {
        return rankDb.getScore() >= rankCfg.getLeastScore();
    }

    protected Optional<T> findPlayerRank(Long playerId) {
        return rankSet.stream()
            .filter(db -> db.getPlayerId().equals(playerId))
            .findAny();
    }

    protected boolean needUpdate(T oldRank, T newRank) {
        return newRank.getScore() > oldRank.getScore();
    }

    @Autowired
    protected DataConfigManager dataConfigManager;

    @Autowired
    private RankDbGetter rankDbGetter;
}
