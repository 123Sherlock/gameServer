package cgx.core.command;

import cgx.core.spring.BeanCollector;

import java.util.Map;

public class CommandSet {

    private static Map<Integer, Command> commandMap;

    /**
     * 加载网络消息集合
     */
    public static boolean load(BeanCollector beanCollector) {
        try {
            commandMap = beanCollector.collectBeanMap(Command.class, Cmd.class, cmd -> {
                Cmd anno = cmd.getClass().getAnnotation(Cmd.class);
                return anno.cmdId();
            });
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return commandMap != null;
    }

    public static Command getCommand(Integer cmdId) {
        return commandMap.get(cmdId);
    }
}
