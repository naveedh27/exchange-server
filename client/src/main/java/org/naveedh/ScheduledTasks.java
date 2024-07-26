package org.naveedh;


import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;


@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private Channel channel;

    private Random random = new Random();

    @Scheduled(fixedRate = 7000)
    public void sendNewOrder() {
        Messages.NewOrder newOrder = Messages.NewOrder.newBuilder()
                .setClOrdID(UUID.randomUUID().toString().substring(0, 3))
                .setExchange("XNSE")
                .setInstrument("INFY")
                .setPrice(random.nextDouble())
                .setQuantity(random.nextInt(5000))
                .build();
        Messages.WrapperMessage msg = Messages.WrapperMessage.newBuilder().setNewOrder(newOrder).build();
        channel.writeAndFlush(msg);
        log.info("Sending New Order :: {}", newOrder);
    }
}
