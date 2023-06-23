package com.example.springemployee.controller;


import com.example.springemployee.entity.Account;
import com.example.springemployee.entity.Reward;
import com.example.springemployee.repositories.RewardRepository;
import com.example.springemployee.service.AccountService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/dashboard")
public class ViewController {


    @GetMapping("/icon-fontawesome")
    public String icon(){
        return "html/icon-fontawesome";
    }
    @GetMapping("/map-google")
    public String map(){
        return "html/map-google.html";
    }
    @GetMapping("/pages-blank")
    public String blank(){
        return "html/pages-blank.html";
    }

    @GetMapping("/pages-error-404")
    public String error(){
        return "html/pages-error-404.html";
    }




}
