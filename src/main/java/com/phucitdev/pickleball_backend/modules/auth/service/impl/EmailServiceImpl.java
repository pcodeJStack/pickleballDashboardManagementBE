package com.phucitdev.pickleball_backend.modules.auth.service.impl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import com.phucitdev.pickleball_backend.modules.auth.dto.EmailDetails;
import com.phucitdev.pickleball_backend.modules.auth.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.phucitdev.pickleball_backend.modules.auth.dto.EmailDetails;
import com.phucitdev.pickleball_backend.modules.auth.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
@Service
public class EmailServiceImpl implements EmailService {
//    @Autowired
//    private TemplateEngine templateEngine;
//
//    @Autowired
//    private JavaMailSender javaMailSender;
    @Value("${resend.api-key}")
    private String apiKey;

    @Value("${resend.from-email}")
    private String fromEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendOtpMail(EmailDetails emailDetails) {

            String url = "https://api.resend.com/emails";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // HTML OTP template (thay Thymeleaf bằng string hoặc bạn có thể giữ Thymeleaf render riêng)
            String htmlContent = buildOtpHtml(
                    emailDetails.getOtpCode(),
                    emailDetails.getExpiryTime().toString()
            );

            Map<String, Object> body = new HashMap<>();
            body.put("from", fromEmail);
            body.put("to", List.of(emailDetails.getEmail()));
            body.put("subject", emailDetails.getSubject());
            body.put("html", htmlContent);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            try {
                ResponseEntity<String> response =
                        restTemplate.postForEntity(url, request, String.class);

                System.out.println("Resend response: " + response.getBody());

            } catch (Exception e) {
                System.out.println("Send OTP email failed: " + e.getMessage());
            }
        }

        private String buildOtpHtml(String otp, String expiryTime) {
            return """
                <div style="font-family: Arial; padding: 20px;">
                    <h2>Your OTP Code</h2>
                    <p>Your OTP code is:</p>
                    <h1 style="color:#2d6cdf">%s</h1>
                    <p>Expires at: %s</p>
                </div>
                """.formatted(otp, expiryTime);

//        try {
//            // Truyền dữ liệu vào Thymeleaf template
//            Context context = new Context();
//            context.setVariable("email", emailDetails.getEmail());
//            context.setVariable("otpCode", emailDetails.getOtpCode());
//            context.setVariable("expiryTime", emailDetails.getExpiryTime());
//
//            // Render template "otp-template.html"
//            String template = templateEngine.process("otp-template", context);
//
//            // Tạo email
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//
//            helper.setFrom("phucp9698@gmail.com");
//            helper.setTo(emailDetails.getEmail());
//            helper.setSubject(emailDetails.getSubject());
//            helper.setText(template, true); // true = gửi dạng HTML
//
//            // Gửi email
//            javaMailSender.send(mimeMessage);
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            System.out.println("Send OTP email failed!!");
//        }
    }
}
