package cgx.logic.player;

import cgx.core.database.BaseDb;

public class PlayerDb extends BaseDb {

    private String name;

    private int level;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
