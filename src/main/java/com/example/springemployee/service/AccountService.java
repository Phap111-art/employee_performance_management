package com.example.springemployee.service;

import com.example.springemployee.dto.AccountDTO;
import com.example.springemployee.entity.Account;
import com.example.springemployee.entity.Role;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface AccountService {
    Account saveAccount(Account account);
    Account updateOrInsertAccount(Account account);
    AccountDTO saveAccountDTO(AccountDTO accountDTO);
    AccountDTO updateOrInsertAccountDTO(AccountDTO accountDTO);
    AccountDTO registerAccountDTO(AccountDTO accountDTO);
    Account findByIdAccount(int id);
    Account findByEmailAccount(String email);
    Account findByUsernameAccount(String username);
    boolean deleteByIdAccount(int id);
    void createNewAccountAfterLoginWithOauth2(String email, String name, Set<Role> roles);
    Account updateAccountAfterLoginWithOauth2(Account account,String name);
    List<Account> getAllAccount();
    List<Account> getAllAccountByUsername(String username);
    List<Account> getAllAccountByEmail(String email);
    List<Account> getAllAccountDoNotExistInTheListOfEmployee();
    Page<Account> getDetailPageAccount();
    Page<Account> getPageDetailAccountByUsername(int pageCurrent,int size,String username);

}
