package com.chaung.kafka.stream.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface PaymentTransactionChannel {
    String CHANNEL = "paymentTransactionChannel";

    @Output
    MessageChannel paymentTransactionChannel();
}
