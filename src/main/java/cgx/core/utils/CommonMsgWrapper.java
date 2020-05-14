package cgx.core.utils;

import cgx.proto.ProtoCommonMsg;
import com.google.protobuf.MessageLite;

public class CommonMsgWrapper {

    public static ProtoCommonMsg.Msg wrap(int cmd, MessageLite msgValue) {
        return ProtoCommonMsg.Msg.newBuilder()
            .setCmd(cmd)
            .setMsgType(msgValue.getClass().getSimpleName())
            .setMsg(msgValue.toByteString())
            .setTime(System.currentTimeMillis())
            .build();
    }
}
