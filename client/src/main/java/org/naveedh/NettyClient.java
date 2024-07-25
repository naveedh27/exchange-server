package org.naveedh;

import io.netty.channel.ChannelFuture;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        ClientBootstrap clientBootstrap = (ClientBootstrap) ctx.getBean("clientBootstrap");
        ChannelFuture tcpChannel = clientBootstrap.bootstrap();

        Messages.NewOrder newOrder = Messages.NewOrder.newBuilder()
                .setClOrdID("11A")
                .setExchange("XNSE")
                .setInstrument("INFY")
                .build();
        Messages.WrapperMessage msg = Messages.WrapperMessage.newBuilder().setNewOrder(newOrder).build();

        tcpChannel.addListener(future -> {
           if(future.isSuccess()) {
               tcpChannel.channel().writeAndFlush(msg);
           }
        });

        // Wait until server socket is closed
        tcpChannel.channel().closeFuture().sync();
    }
}
