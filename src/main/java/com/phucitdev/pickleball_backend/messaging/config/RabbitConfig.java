package com.phucitdev.pickleball_backend.messaging.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "otp.exchange";
    public static final String QUEUE = "otp.queue";
    public static final String ROUTING_KEY = "otp.routing";
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }
    @Bean
    public Queue queue() {
        return QueueBuilder.durable(QUEUE).build();
    }
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(ROUTING_KEY);
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
