package org.naveedh;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public abstract class AbstractSimpleDuplexHandler<T> extends ChannelDuplexHandler {

    private final Logger logger = LoggerFactory.getLogger(AbstractSimpleDuplexHandler.class);

    private final Function<Messages.WrapperMessage, String> timeConverter = msg -> Instant
            .ofEpochMilli(msg.getHeartbeat().getTimestamp())
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ISO_LOCAL_TIME);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            T t = (T) msg;
            this.read(ctx, t);
        } catch (Exception e) {
            logger.info("Exception caught", e);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }


    protected abstract void read(ChannelHandlerContext ctx, T t);

}
