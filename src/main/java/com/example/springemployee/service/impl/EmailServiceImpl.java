package com.example.springemployee.service.impl;

import com.example.springemployee.entity.Account;
import com.example.springemployee.entity.PasswordResetToken;
import com.example.springemployee.exception.DataNotFound;
import com.example.springemployee.repositories.AccountRepository;
import com.example.springemployee.repositories.PasswordResetTokenRepository;
import com.example.springemployee.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.UnsupportedEncodingException;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final AccountRepository accountRepository;

    public EmailServiceImpl(PasswordResetTokenRepository passwordResetTokenRepository, AccountRepository accountRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.accountRepository = accountRepository;
    }


    @Override
    public void sendMail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("contact@shopme.com", "Shopme Support");
        helper.setTo(recipientEmail);
        String subject = "Here's the link to reset your password";
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);
    }

    @Override
    public void updateResetPasswordToken(String token, String email) throws DataNotFound {
        Account account = accountRepository.findByEmail(email);
        if (account != null) {
            PasswordResetToken passwordResetToken = PasswordResetToken.builder().token(token).account(account).build();
            passwordResetTokenRepository.save(passwordResetToken);
        } else {
            throw new DataNotFound(email);
        }
    }

    @Override
    public void updatePassword(Account account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    @Override
    public void deleteToken(String token) {
        PasswordResetToken prt = passwordResetTokenRepository.findByToken(token);
        passwordResetTokenRepository.delete(prt);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }
}
