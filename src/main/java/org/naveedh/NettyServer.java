package org.naveedh;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NettyServer {

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        TCPHandler tcpHandler = (TCPHandler) ctx.getBean("nettyServer");
    }
}
