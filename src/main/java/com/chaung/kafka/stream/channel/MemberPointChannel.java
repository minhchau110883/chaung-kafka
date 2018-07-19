package com.chaung.kafka.stream.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface MemberPointChannel {

    String INPUT = "memberPointInputChannel";

    @Input
    MessageChannel memberPointInputChannel();
}
