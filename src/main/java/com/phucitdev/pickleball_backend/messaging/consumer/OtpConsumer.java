package com.phucitdev.pickleball_backend.messaging.consumer;
import com.phucitdev.pickleball_backend.messaging.config.RabbitConfig;
import com.phucitdev.pickleball_backend.modules.auth.dto.EmailDetails;
import com.phucitdev.pickleball_backend.modules.auth.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class OtpConsumer {
    private final EmailService emailService;
    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void receive(EmailDetails emailDetails) {
        try {
            emailService.sendOtpMail(emailDetails);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}