package com.chaung.kafka.web.rest;

import com.chaung.kafka.domain.PaymentTransaction;
import com.chaung.kafka.stream.channel.PaymentTransactionChannel;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PaymentTransactionResource {

    private final Logger log = LoggerFactory.getLogger(PaymentTransactionResource.class);

    private MessageChannel paymentTransactionChannel;

    public PaymentTransactionResource(PaymentTransactionChannel paymentTransactionChannel) {
        this.paymentTransactionChannel = paymentTransactionChannel.paymentTransactionChannel();
    }

    @PostMapping("/transactions")
    @Timed
    public void produce(PaymentTransaction paymentTransaction) {
        Message<PaymentTransaction> message = MessageBuilder.withPayload(paymentTransaction)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .build();
        boolean sent = paymentTransactionChannel.send(message);

        if(sent){
            log.debug("Sent");
        }else{
            log.error("Unable to sent message to paymentTransactionChannel", message);
        }
    }
}
