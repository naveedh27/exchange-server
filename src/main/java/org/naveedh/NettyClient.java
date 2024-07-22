package org.naveedh;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.naveedh.Messages.HeartBeat;
import org.naveedh.Messages.WrapperMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.util.function.Supplier;


public class NettyClient {

    private static Logger logger = LoggerFactory.getLogger(NettyClient.class);

    public static Supplier<WrapperMessage.Builder> wrapperMessageSupplier = WrapperMessage::newBuilder;

    public static void main(String[] args) throws InterruptedException {

        Bootstrap bootstrap = new Bootstrap();

        // Configure the server
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast(new ProtobufDecoder(WrapperMessage.getDefaultInstance()))
                                .addLast(new ProtobufEncoder())
                                .addLast(new IdleStateHandler(0, 5, 0))
                                .addLast(new SimpleChannelInboundHandler<WrapperMessage>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, WrapperMessage msg) throws Exception {
                                        switch (msg.getMessageCase()) {
                                            case HEARTBEAT -> System.out.println("Received Heartbeat :: "+ Instant.ofEpochMilli(msg.getHeartbeat().getTimestamp()).atZone(ZoneId.systemDefault()));
                                        }
                                    }

                                    @Override
                                    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                        if (evt instanceof IdleStateEvent) {
                                            HeartBeat heartBeat = HeartBeat.newBuilder().setTimestamp(System.currentTimeMillis()).build();
                                            WrapperMessage message = WrapperMessage.newBuilder().setHeartbeat(heartBeat).build();
                                            ctx.writeAndFlush(message);
                                            System.out.println("Sent Heartbeat :: " + Instant.ofEpochMilli(message.getHeartbeat().getTimestamp()).atZone(ZoneId.systemDefault()));
                                        } else {
                                            super.userEventTriggered(ctx, evt);
                                        }
                                    }
                                });
                    }
                });
        // Bind and start to accept incoming connections.
        ChannelFuture httpChannel = bootstrap.connect("127.0.0.1", Integer.parseInt("8080"));

        logger.info("App Started. Running on Port :: {}");
        // Wait until server socket is closed
        httpChannel.channel().closeFuture().sync();


    }
}
