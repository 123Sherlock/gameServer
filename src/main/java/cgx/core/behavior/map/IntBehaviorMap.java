package cgx.core.behavior.map;

import cgx.core.behavior.AbstractBehaviorMap;
import cgx.core.behavior.BehaviorNameException;
import cgx.core.behavior.IBehavior;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntBehaviorMap<B extends IBehavior> extends AbstractBehaviorMap<Integer, B> {

    @Override
    protected Integer getKey(B behavior) {
        String className = behavior.getClass().getSimpleName();
        Matcher nameMatcher = PATTERN.matcher(className);
        if (nameMatcher.find()) { // 必须先调用find才能获取到内容
            // 表达式以()分组，第0组表示全部
            return Integer.valueOf(nameMatcher.group(2));
        }
        throw new BehaviorNameException(className);
    }

    private static final Pattern PATTERN = Pattern.compile("(\\w+)(\\d+)");
}
