package cgx.core.netty;

import cgx.proto.ProtoCommonMsg;
import com.google.protobuf.MessageLite;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

public class ProtobufToMessageCodec extends MessageToMessageCodec<ProtoCommonMsg.Msg, Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> list) throws Exception {
        MessageLite msgValue = msg.getValue();
        ProtoCommonMsg.Msg commonMsg = ProtoCommonMsg.Msg.newBuilder()
            .setCmd(msg.getCmd())
            .setMsgType(msgValue.getClass().getSimpleName())
            .setMsg(msgValue.toByteString())
            .setTime(msg.getTime())
            .build();
        list.add(commonMsg);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ProtoCommonMsg.Msg commonMsg, List<Object> list) throws Exception {
        MessageLite msgValue = CommonMsgBodyParser.parse(commonMsg);
        Message message = new Message();
        message.setCmd(commonMsg.getCmd());
        message.setTime(commonMsg.getTime());
        message.setValue(msgValue);
        list.add(message);
    }
}
