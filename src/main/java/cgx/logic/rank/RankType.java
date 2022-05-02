package cgx.logic.rank;

/**
 * 排行榜类型，与配置表的id对应
 */
public enum RankType {

    NONE(0),
    ;

    private final Integer _id;

    RankType(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }
}
