package org.naveedh;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

@Component
public class ClientChannelHandler extends AbstractSimpleDuplexHandler<Messages.WrapperMessage> {

    private final Logger logger = LoggerFactory.getLogger(ClientChannelHandler.class);

    private final Function<Messages.WrapperMessage, String> timeConverter = msg -> Instant
            .ofEpochMilli(msg.getHeartbeat().getTimestamp())
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ISO_LOCAL_TIME);


    @Override
    protected void read(ChannelHandlerContext ctx, Messages.WrapperMessage msg) {
        if (msg.getMessageCase() == Messages.WrapperMessage.MessageCase.HEARTBEAT) {
            logger.info("Recv Heartbeat :: {}", timeConverter.apply(msg));
        } else if (msg.getMessageCase() == Messages.WrapperMessage.MessageCase.EXECUTIONREPORT) {
            logger.info("Recv ExecReport :: {}", msg.getExecutionReport());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            Messages.HeartBeat heartBeat = Messages.HeartBeat.newBuilder().setTimestamp(System.currentTimeMillis()).build();
            Messages.WrapperMessage msg = Messages.WrapperMessage.newBuilder().setHeartbeat(heartBeat).build();
            ctx.writeAndFlush(msg);
            logger.info("Sent Heartbeat :: {}", timeConverter.apply(msg));
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
