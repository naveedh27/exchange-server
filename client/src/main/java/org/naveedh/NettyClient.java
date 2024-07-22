package org.naveedh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class NettyClient {

    private static Logger logger = LoggerFactory.getLogger(NettyClient.class);

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        ClientBootstrap clientBootstrap = (ClientBootstrap) ctx.getBean("clientBootstrap");
        clientBootstrap.bootstrap();
    }
}
