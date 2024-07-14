package org.naveedh;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.StandardCharsets;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new ChannelInboundHandler() {
            @Override
            public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
                System.out.println("Handler Added");
            }

            @Override
            public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
                System.out.println("Handler Removed");
                channelHandlerContext.writeAndFlush("Close");
            }

            @Override
            public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
                System.out.println("Handler Registered");
            }

            @Override
            public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {
                System.out.println("Handler UnRegistered");
            }

            @Override
            public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
                System.out.println("Channel Active");
            }

            @Override
            public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
                System.out.println("Channel InActive");
            }

            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
                System.out.println("Channel Read");
                ByteBuf in = (ByteBuf) o;
                try {
                    while (in.isReadable()) { // (1)
                        System.out.print((char) in.readByte());
                        System.out.flush();
                    }
                } finally {
                    ReferenceCountUtil.release(o); // (2)
                }
                channelHandlerContext.flush();
            }

            @Override
            public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
                System.out.println("Channel Read Complete");
                channelHandlerContext.flush();
            }

            @Override
            public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
                System.out.println("userEventTriggered");
            }

            @Override
            public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {
                System.out.println("channelWritabilityChanged");
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
                System.out.println("Exception Caught");
            }
        });
    }
}
