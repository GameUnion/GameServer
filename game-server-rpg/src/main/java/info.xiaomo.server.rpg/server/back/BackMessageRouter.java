package info.xiaomo.server.rpg.server.back;


import com.google.protobuf.AbstractMessage;
import info.xiaomo.gengine.network.INetworkConsumer;
import io.netty.channel.Channel;

/**
 * @author qq
 */
public class BackMessageRouter implements INetworkConsumer {

	@Override
	public void consume(AbstractMessage message, Channel channel) {
		System.out.println();
//        message.doAction();
	}

}