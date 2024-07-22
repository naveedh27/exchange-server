package org.naveedh;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
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
import org.springframework.stereotype.Component;

@Component
public class ClientBootstrap {

    private Logger logger = LoggerFactory.getLogger(ClientBootstrap.class);

    @Value("${client.port}")
    private int port;

    @Value("${client.host}")
    private String host;

    @Value("${client.heartbeat.interval}")
    private int hbInterval;

    @Autowired
    private ClientChannelInBoundHandler clientHandler;

    public void bootstrap() throws InterruptedException {

        Bootstrap bootstrap = new Bootstrap();

        // Configure the server
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast(new LoggingHandler("CLIENT LOGGER", LogLevel.INFO))
                                .addLast(new ProtobufDecoder(Messages.WrapperMessage.getDefaultInstance()))
                                .addLast(new ProtobufEncoder())
                                .addLast(new IdleStateHandler(0, hbInterval, 0))
                                .addLast(clientHandler);
                    }
                });
        // Bind and start to accept incoming connections.
        ChannelFuture httpChannel = bootstrap.connect(host, port);

        if (httpChannel.awaitUninterruptibly().isSuccess()) {
            logger.info("App Started. Running on Port :: {}", port);
        } else {
            logger.info("Failed to start App.");
        }

        // Wait until server socket is closed
        httpChannel.channel().closeFuture().sync();

    }

}
