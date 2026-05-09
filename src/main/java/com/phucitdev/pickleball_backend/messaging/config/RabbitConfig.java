package com.phucitdev.pickleball_backend.messaging.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitConfig {
    public static final String OTP_EXCHANGE = "otp.exchange";
    public static final String OTP_QUEUE = "otp.queue";
    public static final String OTP_ROUTING_KEY = "otp.routing";

    public static final String BOOKING_EXCHANGE = "booking.exchange";
    public static final String BOOKING_QUEUE = "booking.queue";
    public static final String BOOKING_DELAY_QUEUE = "booking.delay.queue";
    public static final String BOOKING_DELAY_ROUTING = "booking.delay";
    public static final String BOOKING_PROCESS_ROUTING = "booking.process";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(OTP_EXCHANGE);
    }
    @Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange(BOOKING_EXCHANGE);
    }

    @Bean
    public Queue otpQueue() {
        return QueueBuilder.durable(OTP_QUEUE).build();
    }
    @Bean
    public Queue bookingQueue() {
        return QueueBuilder.durable(BOOKING_QUEUE).build();
    }
    @Bean
    public Queue bookingDelayQueue() {
        return QueueBuilder.durable(BOOKING_DELAY_QUEUE)
                .withArgument("x-dead-letter-exchange", BOOKING_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", BOOKING_PROCESS_ROUTING)
                .withArgument("x-message-ttl", 15 * 60 * 1000)
                .build();
    }
    @Bean
    public Binding otpBinding() {
        return BindingBuilder.bind(otpQueue())
                .to(exchange())
                .with(OTP_ROUTING_KEY);
    }
    @Bean
    public Binding bookingDelayBinding() {
        return BindingBuilder.bind(bookingDelayQueue())
                .to(bookingExchange())
                .with(BOOKING_DELAY_ROUTING);
    }
    @Bean
    public Binding bookingProcessBinding() {
        return BindingBuilder.bind(bookingQueue())
                .to(bookingExchange())
                .with(BOOKING_PROCESS_ROUTING);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
