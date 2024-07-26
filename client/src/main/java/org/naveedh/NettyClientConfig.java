package org.naveedh;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@PropertySource("application.properties")
@ComponentScan(basePackages = "org.naveedh.*")
@EnableScheduling
public class NettyClientConfig {

    private Logger logger = LoggerFactory.getLogger(NettyClientConfig.class);

    @Value("${client.port}")
    private int port;

    @Value("${client.host}")
    private String host;

    @Value("${client.heartbeat.interval}")
    private int hbInterval;

    @Autowired
    private ClientChannelHandler channelHandler;

    @Bean(name = "clientChannel")
    public Channel bootstrap() throws InterruptedException {

        Bootstrap bootstrap = new Bootstrap();

        // Configure the server
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast(new ProtobufDecoder(Messages.WrapperMessage.getDefaultInstance()))
                                .addLast(new ProtobufEncoder())
                                .addLast(new IdleStateHandler(0, 0, 0))
                                .addLast(channelHandler);
                    }
                });
        // Bind and start to accept incoming connections.
        ChannelFuture tcpChannel = bootstrap.connect(host, port);

        tcpChannel.addListener((ChannelFutureListener) future -> {
            if(future.isSuccess()) {
                logger.info("Client Connected to Server on Port :: {}", port);
            } else {
                logger.error("Error connecting to Server. Host :: {} Port :: {}", host, port);
                System.exit(0);
            }
        });

        tcpChannel.awaitUninterruptibly();
        return tcpChannel.channel();
    }
}
