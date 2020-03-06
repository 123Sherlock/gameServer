package cgx.core.behavior.map;

import cgx.core.behavior.AbstractBehaviorMap;
import cgx.core.behavior.BehaviorNameException;
import cgx.core.behavior.IBehavior;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringBehaviorMap<B extends IBehavior> extends AbstractBehaviorMap<String, B> {

    @Override
    protected String getKey(B behavior) {
        String className = behavior.getClass().getSimpleName();
        Matcher nameMatcher = PATTERN.matcher(className);
        if (nameMatcher.find()) {
            return nameMatcher.group(2);
        }
        throw new BehaviorNameException(className);
    }

    private static final Pattern PATTERN = Pattern.compile("(\\w+)Behavior(\\w+)");
}
