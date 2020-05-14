package cgx.logic.player;

import cgx.core.utils.CommonMsgWrapper;
import com.google.protobuf.MessageLite;
import io.netty.channel.Channel;

public class GamePlayer {

    public void sendToClient(MessageLite msg) {
        _channel.writeAndFlush(CommonMsgWrapper.wrap(0, msg));
    }

    public Long getPlayerId() {
        return _playerId;
    }

    public void setPlayerId(Long playerId) {
        _playerId = playerId;
    }

    public Channel getChannel() {
        return _channel;
    }

    public void setChannel(Channel channel) {
        _channel = channel;
    }

    private Long _playerId;

    private Channel _channel;
}
