package cgx.logic.player;

import io.netty.channel.Channel;

public class GamePlayer {

    private Long playerId;

    private Channel channel;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
