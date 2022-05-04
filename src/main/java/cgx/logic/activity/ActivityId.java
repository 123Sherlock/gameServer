package cgx.logic.activity;

import java.util.Arrays;

/**
 * @see ActivityCfg#getId
 */
public enum ActivityId {

    NONE(0),
    ;

    private final int _id;

    public static ActivityId getById(int id) {
        return Arrays.stream(values())
            .filter(t -> t.getId() == id)
            .findAny()
            .orElse(NONE);
    }

    public int getId() {
        return _id;
    }

    ActivityId(int id) {
        _id = id;
    }
}
