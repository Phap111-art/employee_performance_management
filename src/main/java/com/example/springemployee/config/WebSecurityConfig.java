package com.example.springemployee.config;

import com.example.springemployee.oauth2.Oauth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@EnableWebSecurity
@Configuration

public class WebSecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService detailsService() {
        return new UserServiceConfig();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(detailsService());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .requestMatchers("/login","/login/*","/dashboard/home","/oauth2/**","/register","/save").permitAll()
                .anyRequest().authenticated() // chỉ được admin hoặc normal mới truy cập được page này
                .and()
                .formLogin().loginPage("/login")
                .loginProcessingUrl("/do-login")
                .defaultSuccessUrl("/dashboard/home")
                .and().csrf().disable()
                .logout() //lật chức năng đăng xuất và cấu hình các tùy chọn liên quan đến đăng xuất.
                .invalidateHttpSession(true) //Đánh dấu phiên người dùng (HttpSession) là không hợp lệ sau khi đăng xuất.
                .clearAuthentication(true) //Xóa thông tin xác thực của người dùng sau khi đăng xuất.
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //Xác định URL để xử lý yêu cầu đăng xuất.
                .addLogoutHandler(new CookieClearingLogoutHandler("remember-me"))
                .logoutSuccessUrl("/login?logout") //Xác định URL mà người dùng sẽ được chuyển đến sau khi đăng xuất thành công.
                .permitAll() //Cho phép tất cả các yêu cầu tới các URL liên quan đến đăng xuất được truy cập công khai mà không yêu cầu xác thực.
                .and().exceptionHandling().accessDeniedPage("/403") //nếu truy cập đường dẫn ko tồn tại hoặc chỉ admin mới vào được thì sẽ trả về trang 403
                .and().oauth2Login().loginPage("/login").successHandler(customAuthenticationSuccessHandler())
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
        return http.build();
    }
    @Bean
    public Oauth2LoginSuccessHandler customAuthenticationSuccessHandler(){
        return new Oauth2LoginSuccessHandler();
    }
}
