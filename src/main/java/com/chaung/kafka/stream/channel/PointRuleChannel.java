package com.chaung.kafka.stream.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface PointRuleChannel {

    String OUTPUT = "pointRuleOutputChannel";
    String INPUT = "pointRuleInputChannel";

    @Output
    MessageChannel pointRuleOutputChannel();

    @Input
    MessageChannel pointRuleInputChannel();
}
