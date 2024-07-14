package org.naveedh;

import io.netty.channel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(scopeName = "prototype")
@ChannelHandler.Sharable
public class MyHandler extends ChannelInboundHandlerAdapter {


    @Autowired
    private MyService myService;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // Use your Spring bean
        String resp = myService.doSomething(msg);
        ChannelFuture responseFromNetty = ctx.writeAndFlush("Response from Netty. " + resp);
        responseFromNetty.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
