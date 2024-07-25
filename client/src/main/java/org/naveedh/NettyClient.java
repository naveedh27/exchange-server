package org.naveedh;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        ClientBootstrap clientBootstrap = (ClientBootstrap) ctx.getBean("clientBootstrap");
        clientBootstrap.bootstrap();
    }
}
