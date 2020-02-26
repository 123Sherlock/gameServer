package cgx.core.netty.msg;

import java.io.Serializable;

public class Message implements Serializable {

    /**
     * 模块ID
     */
    private int module;

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
    private Object value;

    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

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

    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Message{module=" + module + ", cmd=" + cmd + ", time=" + time + '}';
    }
}
