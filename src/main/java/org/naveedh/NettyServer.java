package org.naveedh;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NettyServer {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        ServerBootstrap serverBootstrap = (ServerBootstrap) ctx.getBean("nettyServerBootstrap");
    }
}
