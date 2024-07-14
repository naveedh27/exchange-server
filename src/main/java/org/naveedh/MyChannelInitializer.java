package org.naveedh;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new ChannelInboundHandler() {
            @Override
            public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
                channelHandlerContext.writeAndFlush("Echo Back");
            }

            @Override
            public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {

            }

            @Override
            public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {

            }

            @Override
            public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {

            }

            @Override
            public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {

            }

            @Override
            public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {

            }

            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
                channelHandlerContext.writeAndFlush()
                System.out.println(o.toString());
            }

            @Override
            public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {

            }

            @Override
            public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

            }

            @Override
            public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {

            }

            @Override
            public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

            }
        });
    }
}
