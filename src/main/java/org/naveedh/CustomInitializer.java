package org.naveedh;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.ReferenceCountUtil;

public class CustomInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel
                .pipeline()
                .addLast(new LoggingHandler(LogLevel.TRACE))
                .addLast(new SimpleChannelInboundHandler<SocketChannel>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, SocketChannel msg) throws Exception {
                        System.out.println("Channel Read");
                        super.channelRead(ctx, msg);
                    }
                })
                .addLast(new ChannelOutboundHandlerAdapter());
    }
}
