package com.example.springemployee.api;

import com.example.springemployee.entity.Account;
import com.example.springemployee.exception.DataNotFound;
import com.example.springemployee.utility.Local;
import com.example.springemployee.model.ResponseObject;
import com.example.springemployee.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/forgot")
public class SendMailControllerApi {
    final Logger logger = LoggerFactory.getLogger(AccountControllerApi.class);

    private final EmailService emailService;

    public SendMailControllerApi(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send-mail")
    public ResponseEntity<ResponseObject> sendMail(HttpServletRequest request, @RequestParam("email") String email){
        logger.info("data : " + email);
        String token = StringUtils.randomAlphanumeric(45);
        try {
            emailService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Local.getSiteURL(request) + "/login/view-reset-password?token=" + token;
            emailService.sendMail(email, resetPasswordLink);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Send Mail success!" + email, true, "We have sent a reset password link to your email. Please check.")
            );
        } catch (DataNotFound ex) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Error!" + email, true, ex.getMessage())
            );
        } catch (UnsupportedEncodingException | MessagingException e) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Error!" + email, true, "Error while sending email")
            );
        }

    }
    @GetMapping("/{token}")
    public ResponseEntity<ResponseObject> resetPassword(@PathVariable("token") String token){
        Account account =  emailService.getPasswordResetToken(token).getAccount();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("find token", true, account)
        );
    }
}
