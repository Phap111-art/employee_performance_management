package com.example.springemployee.controller;

import com.example.springemployee.dto.AccountDTO;
import com.example.springemployee.entity.Account;
import com.example.springemployee.entity.Role;
import com.example.springemployee.exception.FieldErrorResultMsg;
import com.example.springemployee.service.AccountService;
import com.example.springemployee.service.RewardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class AuthenticationController {

    private final RewardService rewardService;
    private final AccountService accountService;
    final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationController(RewardService rewardService, AccountService accountService) {
        this.rewardService = rewardService;

        this.accountService = accountService;
    }


    @GetMapping("/dashboard/home")
    public String home(HttpSession session, Authentication authentication, Model model) {
        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            String email = defaultOAuth2User.getAttribute("name") != null ? defaultOAuth2User.getAttribute("email") : defaultOAuth2User.getAttribute("login");
            Account account = accountService.findByEmailAccount(email);
            session.setAttribute("ACCOUNT", account);
            getAuthorities(account, session);
        } else {
            Account account = accountService.findByUsernameAccount(authentication.getName());
            session.setAttribute("ACCOUNT", account);
        }
        model.addAttribute("top10Employee", rewardService.getTop10Employee());
        return "html/index";
    }

    private void getAuthorities(Account account, HttpSession session) {
        Set<Role> roles = account.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role auth : roles) {
            authorities.add(new SimpleGrantedAuthority(auth.getName()));
        }
        logger.info("authorities : " + authorities.get(0).getAuthority());
        System.out.println("authorities : " + authorities.get(0).getAuthority());
        session.setAttribute("authorities", authorities.get(0).getAuthority());
    }

    @GetMapping("/normal")
    public String normal(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("ACCOUNT");
        return "test/normal.html";
    }

    @GetMapping("/admin")
    public String admin() {
        return "test/admin.html";
    }


    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("USER",new AccountDTO());
        return "html/sign-up.html";
    }

    @PostMapping("/save")
    public String saveRegister(@Valid AccountDTO accountDTO, BindingResult result, Model model) {

        List<Account> isUsername = accountService.getAllAccountByUsername(accountDTO.getUsername().trim());
        List<Account> isEmail = accountService.getAllAccountByEmail(accountDTO.getEmail().trim());
        String fieldError = FieldErrorResultMsg.getMsgError(result);
        if (!fieldError.isEmpty()){
            model.addAttribute("errorField" , "fieldError");
            return register(model);
        }
        if (!isUsername.isEmpty()) {
            model.addAttribute("errorField", "*Username account already exist !");
            return register(model);
        }
        if (!isEmail.isEmpty()) {
            model.addAttribute("errorField", "*Email account already exist !");
            return register(model);
        }
        if (!result.hasErrors() || isUsername.isEmpty() || isEmail.isEmpty()) {
            model.addAttribute("add_success", "Add Success!");
        }
        accountService.registerAccountDTO(accountDTO);
        return register(model);
    }

    @GetMapping("/login")
    public String login() {
        return "html/login";
    }

    @GetMapping("/403")
    public String notFound() {
        return "403.html";
    }

    @ModelAttribute("USER")
    public AccountDTO init() {
        return new AccountDTO();
    }
}
