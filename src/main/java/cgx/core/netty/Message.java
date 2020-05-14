package cgx.core.netty;

import com.google.protobuf.MessageLite;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class Message implements Serializable {

    /**
     * 命令ID
     */
    private int cmd;

    /**
     * 消息请求/响应时的服务器时间
     */
    private long time;

    /**
     * 请求/响应的消息
     */
    private MessageLite value;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @SuppressWarnings({"unchecked", "unused"})
    public <T extends MessageLite> T getValue(Class<T> msgType) {
        return (T) value;
    }

    public MessageLite getValue() {
        return value;
    }

    public void setValue(MessageLite value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
