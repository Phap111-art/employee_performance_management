package com.example.springemployee.controller;

import com.example.springemployee.api.AccountControllerApi;
import com.example.springemployee.entity.Account;
import com.example.springemployee.entity.PasswordResetToken;
import com.example.springemployee.exception.DataNotFound;
import com.example.springemployee.model.ResponseObject;
import com.example.springemployee.service.EmailService;
import com.example.springemployee.utility.Local;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/login")

public class SendMailController {

    private final EmailService emailService;
    private static final String VIEW_RESET_PASSWORD = "html/reset-password.html";
    private static final String VIEW_FORGOT_PASSWORD = "html/forgot-password.html";
    final Logger logger = LoggerFactory.getLogger(SendMailController.class);

    public SendMailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/forgot-password")
    public String viewForgotPassword(){
        return VIEW_FORGOT_PASSWORD;
    }
    @PostMapping("/send-mail")
    public String sendMail(HttpServletRequest request, Model model){
        String email = request.getParameter("email");
        logger.info("data : " + email);
        String token = StringUtils.randomAlphanumeric(45);
        try {
            emailService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Local.getSiteURL(request) + "/login/view-reset-password?token=" + token;
            emailService.sendMail(email, resetPasswordLink);
            model.addAttribute("sendMail_success", "SendMail Success!");
        } catch (DataNotFound ex) {
            model.addAttribute("messError",ex);
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("messError",e);
        }
        return viewForgotPassword();
    }
    @GetMapping("/view-reset-password")
    public String showResetPasswordForm(@RequestParam(defaultValue = "1") String token, Model model){
        model.addAttribute("token",token);
        return VIEW_RESET_PASSWORD;
    }
    @PostMapping("/change-password")
    public String processResetPassword(@RequestParam String newPassword,
                                @RequestParam String token, Model model) {
        PasswordResetToken prt = emailService.getPasswordResetToken(token);
        model.addAttribute("title", "Reset your password");
        if (prt == null) {
            model.addAttribute("message", "Invalid Token");
            return "html/pages-error-404";
        } else {
            emailService.updatePassword(prt.getAccount(), newPassword);
            emailService.deleteToken(token);
            model.addAttribute("change_success", "Change Password Success!");
        }
        return VIEW_RESET_PASSWORD;
    }
}
