package org.naveedh;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class TCPHandler {

    public TCPHandler(int port) {
        this.port = port;
    }

    private final int port;

    @Autowired
    private EventLoopGroup workerGroup;
    @Autowired
    private EventLoopGroup bossGroup;
    @Autowired
    private CustomChannelInitializer customChannelInitializer;

    private Logger logger = LoggerFactory.getLogger(TCPHandler.class);

    @PostConstruct
    public void start() throws InterruptedException {

        ServerBootstrap httpBootstrap = new ServerBootstrap();
        // Configure the server
        httpBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler("SERVER LOGGER", LogLevel.INFO))
                .childHandler(customChannelInitializer)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        // Bind and start to accept incoming connections.
        ChannelFuture httpChannel = httpBootstrap.bind(port).sync();
        logger.info("App Started. Running on Port :: {}", port);
        // Wait until server socket is closed
        httpChannel.channel().closeFuture().sync();
    }


    @PreDestroy
    public void destroy() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

}
