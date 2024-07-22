package org.naveedh;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.naveedh.Messages.*;

@ChannelHandler.Sharable
@Component
public class ProtobufChannelHandler<T> extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(ProtobufChannelHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg != null){
            T t = (T) msg;
            logger.info(t.toString());
            HeartBeat heartBeat = HeartBeat.newBuilder().setTimestamp(System.currentTimeMillis()).build();
            WrapperMessage wrapperMessage = WrapperMessage.newBuilder().setHeartbeat(heartBeat).build();
            ctx.writeAndFlush(wrapperMessage);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        HeartBeat heartBeat = HeartBeat.newBuilder().setTimestamp(System.currentTimeMillis()).build();
        WrapperMessage wrapperMessage = WrapperMessage.newBuilder().setHeartbeat(heartBeat).build();
        ctx.writeAndFlush(wrapperMessage);
    }
}
