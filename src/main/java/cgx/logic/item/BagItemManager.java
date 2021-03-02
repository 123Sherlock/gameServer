package cgx.logic.item;

import org.springframework.stereotype.Component;

@Component
public class BagItemManager {

    public boolean isPermenent(BagItemDb bagItemDb) {
        return bagItemDb.getExpiredTime() < 0;
    }
}
