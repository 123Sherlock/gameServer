package cgx.logic.activity;

import cgx.core.database.DbGetOrCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActivityDbGetter {

    public ActivityDb get(int activityId) {
        return dbGetOrCreator.getDb(ActivityDb.class, (long) activityId);
    }

    public ActivityDb getOrCreate(int activityId) {
        ActivityDb activityDb = get(activityId);
        return activityDb == null ? new ActivityDb() : activityDb;
    }

    public List<ActivityDb> getAll() {
        return dbGetOrCreator.getAllDbList(ActivityDb.class);
    }

    @Autowired
    private DbGetOrCreator dbGetOrCreator;
}
