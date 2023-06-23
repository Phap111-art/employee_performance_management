package com.example.springemployee.service;


import com.example.springemployee.entity.Account;
import com.example.springemployee.service.impl.AccountServiceImpl;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private com.example.springemployee.repositories.AccountRepository accountRepository;


    private AccountService accountService;

    private static final Integer ID = 4;

    private final Logger logger = LoggerFactory.getLogger(AccountServiceTest.class);

    private static final Account account = Account.builder().id(ID).username("LEO").password("123456").fullName("lila").build();

    @BeforeEach
    void init() {
        accountService = new AccountServiceImpl(accountRepository);
    }

    @Test
    void test_insertAccount_ReturnSuccess() {
        Mockito.when(accountRepository.save(account)).thenReturn(account);
        Account actualAcc = accountService.saveAccount(account);
        Assert.assertNotNull(actualAcc);
        Assert.assertEquals(account, actualAcc);
        logger.info("data : " + actualAcc);
    }

    @Test
    void test_updateOrInsertAccount_ReturnSuccess() {
        Mockito.when(accountRepository.findById(ID)).thenReturn(Optional.of(account));
        Mockito.when(accountRepository.save(account)).thenReturn(account);
        Account updateAcc = accountService.updateOrInsertAccount(account);
        Assert.assertNotNull(updateAcc);
        Assert.assertEquals(account, updateAcc);
        logger.info("data : " + updateAcc);
    }

    @Test
    void test_deleteAccountById_ReturnSuccess() {
        Mockito.when(accountRepository.existsById(ID)).thenReturn(true);
        boolean isDel = accountService.deleteByIdAccount(ID);
        Assert.assertEquals(isDel, true);
        logger.info("id del is : " + isDel);
    }

    @Test
    void test_deleteAccountDoseNotExist_ReturnFalse() {
        Mockito.when(accountRepository.existsById(ID)).thenReturn(false);
        try {
            boolean isDel = accountService.deleteByIdAccount(ID);
            Assert.assertEquals(isDel, false);
        } catch (Exception e) {
            logger.info("" + e);
        }
    }

    @Test
    void test_findAccountById_ReturnSuccess() {
        Mockito.when(accountRepository.findById(ID)).thenReturn(Optional.of(account));
        Account actualAcc = accountService.findByIdAccount(ID);
        Assert.assertEquals(actualAcc, account);
        logger.info("" + actualAcc);
    }

    @Test
    void test_accountPageDetail_ReturnSuccess(){

    }
    @Test
    void test_getAllAccountList_ReturnSuccess(){
        Mockito.when(accountRepository.findAll()).thenReturn(List.of(account));
        List<Account> accounts = accountService.getAllAccount();
        Assert.assertThat(accounts.size(),Matchers.greaterThanOrEqualTo(1));
        logger.info("size : " + accounts.size());
    }
}
