package com.example.springemployee.oauth2;


import com.example.springemployee.entity.Account;
import com.example.springemployee.entity.Role;
import com.example.springemployee.service.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private AccountService accountService;
    final Logger logger = LoggerFactory.getLogger(Oauth2LoginSuccessHandler.class);
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        SecurityContext securityContext = SecurityContextHolder.getContext();

        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) securityContext.getAuthentication().getPrincipal();
        String email = defaultOAuth2User.getAttribute("email") ;
        String name = defaultOAuth2User.getAttribute("name");
        String given_name = defaultOAuth2User.getAttribute("given_name");
        String family_name = defaultOAuth2User.getAttribute("family_name");
        Account account = accountService.findByEmailAccount(email);
        if (account == null){
            accountService.createNewAccountAfterLoginWithOauth2(email,name, Set.of(new Role(2,"USER")));
        }else{
            accountService.updateAccountAfterLoginWithOauth2(account,name);
        }


        super.setDefaultTargetUrl("/dashboard/home");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
