package org.naveedh;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class NettyClient {

    private static Logger logger = LoggerFactory.getLogger(NettyClientConfig.class);

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("org.naveedh");
        Channel tcpChannel = ctx.getBean(Channel.class);

        try {
            tcpChannel.closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("Error closing future", e);
        } finally {
            tcpChannel.eventLoop().shutdownGracefully();
        }
    }
}
