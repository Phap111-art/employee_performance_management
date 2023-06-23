package com.example.springemployee.service.impl;

import com.example.springemployee.dto.AccountDTO;
import com.example.springemployee.entity.Account;
import com.example.springemployee.entity.Role;
import com.example.springemployee.exception.DataNotFound;

import com.example.springemployee.mapper.AccountMp;
import com.example.springemployee.repositories.AccountRepository;
import com.example.springemployee.repositories.AuthorityRepository;
import com.example.springemployee.repositories.EmployeeRepository;
import com.example.springemployee.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountMp accountMp;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public Account saveAccount(Account account) {
        account.setFullName(account.getFullName().toUpperCase(Locale.ROOT));
        account.setActive(true);

        return accountRepository.save(account);
    }

    @Override
    public AccountDTO updateOrInsertAccountDTO(AccountDTO dto) {
        return accountRepository.findById(dto.getId()).map(data -> {
            data.setUsername(dto.getUsername());
            data.setPassword(passwordEncoder.encode(dto.getPassword()));
            data.setEmail(dto.getEmail());
            data.setActive(dto.isActive());
            if (dto.getPassword().startsWith("$2a$")){
                data.setPassword(dto.getPassword());
            }else{
                data.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
            data.setFullName(dto.getFullName().toUpperCase(Locale.ROOT));
            Set<Role> roles = dto.getRoleId().stream().map(id -> authorityRepository.findById(id).orElseGet(() -> {
                throw new DataNotFound("id " + id + " not found in data");
            })).collect(Collectors.toSet());
            data.setRoles(roles);
            return accountMp.toDto(accountRepository.save(data));
        }).orElseThrow(DataNotFound::new);
    }

    @Override
    public AccountDTO registerAccountDTO(AccountDTO accountDTO) {
        Account account = Account.builder()
                .username(accountDTO.getUsername())
                .email(accountDTO.getEmail())
                .fullName("")
                .password(passwordEncoder.encode(accountDTO.getPassword()))
                .photo("no_avatar.jpg")
                .isActive(true)
                .roles(Set.of(new Role(2,"USER")))
                .build();
        return accountMp.toDto(accountRepository.save(account));
    }

    @Override
    public Account updateOrInsertAccount(Account account) {
        return accountRepository.findById(account.getId()).map(data -> {
            data.setUsername(account.getUsername());
            data.setPassword(account.getPassword());
            data.setEmail(account.getEmail());
            data.setFullName(account.getFullName().toUpperCase(Locale.ROOT));
            Set<Role> roles = account.getRoleId().stream().map(id -> authorityRepository.findById(id).orElseGet(() -> {
                throw new DataNotFound("id " + id + " not found in data");
            })).collect(Collectors.toSet());
            data.setRoles(roles);
            return accountRepository.save(data);
        }).orElseGet(() -> {
            account.setId(account.getId());
            return accountRepository.save(account);
        });
    }

    @Override
    public AccountDTO saveAccountDTO(AccountDTO dto) {
        Account account = accountMp.toEntity(dto);
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setFullName(account.getFullName().toUpperCase(Locale.ROOT));
        Set<Role> roles = dto.getRoleId().stream().map(id -> authorityRepository.findById(id).orElseGet(() -> {
            throw new DataNotFound("id " + id + " id does not exist!");
        })).collect(Collectors.toSet());
        account.setRoles(roles);
        account.setActive(true);
        account.setPhoto("no_avatar.jpg");
        return accountMp.toDto(accountRepository.save(account));
    }


    @Override
    public Account findByIdAccount(int id) {
        return accountRepository.findById(id).orElseGet(() -> {
            throw new DataNotFound("id " + id + " not found in data");
        });
    }

    @Override
    public Account findByEmailAccount(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public Account findByUsernameAccount(String username) {
        return accountRepository.findByUsernameAccount(username);
    }

    @Override
    public boolean deleteByIdAccount(int id) {
        boolean isDel = accountRepository.existsById(id);
        if (isDel) {
            Account account = findByIdAccount(id);
            account.getRoles().clear();
            accountRepository.delete(account);
            return true;
        }
        throw new IllegalArgumentException("id " + id + " do not exist!");
    }

    @Override
    public void createNewAccountAfterLoginWithOauth2(String email, String name, Set<Role> roles) {
        Account account = new Account();
        account.setEmail(email);
        account.setFullName(name);
        account.setRoles(roles);
        account.setPassword("");
        account.setUsername("");
        account.setPhoto("no_avatar.jpg");
        account.setActive(true);
        accountRepository.save(account);
    }

    @Override
    public Account updateAccountAfterLoginWithOauth2(Account account, String name) {
        return accountRepository.findById(account.getId()).map(data -> {
            data.setFullName(name);
            return accountRepository.save(data);
        }).orElseThrow(DataNotFound::new);
    }


    @Override
    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> getAllAccountByUsername(String username) {
        return accountRepository.findAllByUsername(username);
    }

    @Override
    public List<Account> getAllAccountByEmail(String email) {
        return accountRepository.findAllByEmail(email);
    }

    @Override
    public List<Account> getAllAccountDoNotExistInTheListOfEmployee() {
        List<Integer> employeeList = employeeRepository.findAll().stream().map(data->data.getAccount().getId()).collect(Collectors.toList());
        List<Integer> accountList = accountRepository.findAll().stream().map(Account::getId).collect(Collectors.toList());
        return   findDifference(accountList, employeeList).stream()
                .map(id-> accountRepository.findById(id).orElseThrow(DataNotFound::new)).collect(Collectors.toList());
    }
    private static <T> List<T> findDifference(List<T> first, List<T> second)
    {
        List<T> diff = new ArrayList<>(first);
        diff.removeAll(second);
        return diff;
    }

    @Override
    public Page<Account> getDetailPageAccount() {
        Pageable pageable = PageRequest.of(0, 5);
        return accountRepository.findAll(pageable);
    }

    @Override
    public Page<Account> getPageDetailAccountByUsername(int pageCurrent, int size, String username) {
        Pageable pageable = PageRequest.of(pageCurrent - 1, size);
        return accountRepository.findByUsernameLike(username + "%", pageable);
    }


}