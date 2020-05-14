package cgx.logic.player;

import cgx.core.database.BaseDb;

public class PlayerDb extends BaseDb {

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public int getLevel() {
        return _level;
    }

    public void setLevel(int level) {
        _level = level;
    }

    private String _name;

    private int _level;
}
