package com.chaung.kafka.stream.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface PointProcessorChannel {

    String OUTPUT = "pointProcessorOutputChannel";
    String INPUT = "pointProcessorInputChannel";

    @Output
    MessageChannel pointProcessorOutputChannel();

    @Input
    MessageChannel pointProcessorInputChannel();
}
