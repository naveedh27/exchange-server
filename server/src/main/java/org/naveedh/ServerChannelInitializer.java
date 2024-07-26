package org.naveedh;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.naveedh.Messages.WrapperMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private ProtobufChannelHandler protobufChannelHandler;

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline()
                .addLast(new LoggingHandler("CLIENT EVENT LOGGER", LogLevel.INFO))
                .addLast(new ProtobufDecoder(WrapperMessage.getDefaultInstance()))
                .addLast(new ProtobufEncoder())
                .addLast(protobufChannelHandler);
    }
}
