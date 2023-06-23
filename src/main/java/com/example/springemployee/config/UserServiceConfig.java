package com.example.springemployee.config;

import com.example.springemployee.entity.Account;
import com.example.springemployee.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserServiceConfig implements UserDetailsService {
    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = accountService.findByUsernameAccount(username);
        if (user == null) {
            throw new UsernameNotFoundException("Could not find username");
        }
        return new MyUserDetail(user);
    }
}
