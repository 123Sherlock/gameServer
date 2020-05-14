package cgx.core.netty;

import cgx.core.utils.ClassUtil;
import cgx.proto.ProtoCommonMsg;
import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class CommonMsgBodyParser {

    /**
     * 把ByteString还原为对应的Protobuf类
     */
    public static MessageLite parse(ProtoCommonMsg.Msg commonMsg) throws Exception {
        String type = commonMsg.getMsgType();
        ByteString body = commonMsg.getMsg();

        Method method = _parseMethodMap.get(type);
        if (method == null) {
            throw new RuntimeException(String.format("Unknown message type[%s]", type));
        }
        return (MessageLite) method.invoke(null, body);
    }

    /**
     * 反射获取parseFrom方法并缓存到map
     */
    @SuppressWarnings("unchecked")
    private static void collectMethod(Class<?> protoClass) {
        try {
            _parseMethodMap.put(protoClass.getSimpleName(), protoClass.getMethod("parseFrom", ByteString.class));
        } catch (NoSuchMethodException ignored) {

        }
    }

    private static final Map<String, Method> _parseMethodMap = new ConcurrentHashMap<>();

    static {
        // 找到指定包下所有protobuf实体类
        List<Class<?>> classes = ClassUtil.getClasses("cgx.proto");
        classes.stream()
            .filter(c -> !Objects.equals(c, ProtoCommonMsg.Msg.class))
            .forEach(CommonMsgBodyParser::collectMethod);
    }
}
