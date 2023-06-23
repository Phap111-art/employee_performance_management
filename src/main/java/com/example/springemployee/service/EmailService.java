package com.example.springemployee.service;

import com.example.springemployee.entity.Account;
import com.example.springemployee.entity.PasswordResetToken;
import com.example.springemployee.exception.DataNotFound;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailService {
    void sendMail(String recipientEmail,String link) throws MessagingException, UnsupportedEncodingException;
    void updateResetPasswordToken(String token,String email) throws DataNotFound;
    void updatePassword(Account account ,String newPassword);
    void deleteToken(String token);
    PasswordResetToken getPasswordResetToken(String token);

}
