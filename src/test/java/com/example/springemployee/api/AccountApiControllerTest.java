package com.example.springemployee.api;

import com.example.springemployee.entity.Account;
import com.example.springemployee.entity.Role;
import com.example.springemployee.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountApiControllerTest {
    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mvc;

    ObjectMapper om = new ObjectMapper();

    private static final int ID = 4;

    private final Logger logger = LoggerFactory.getLogger(AccountApiControllerTest.class);
    private static final Role role = new Role(1,"ADMIN");
    private static final Account account = Account.builder().id(ID).username("Mandy").password("123").fullName("Mr. Mandy")
            .roles(Set.of(role)).build();

    @Test
    public void test_insertAccountWhenNameDoesNotExistApi_ReturnSuccess() throws Exception {
        Mockito.when(accountService.getAllAccountByUsername("Billy")).thenReturn(List.of(account));
        Mockito.when(accountService.saveAccount(account)).thenReturn(account);
        mvc.perform(post("/api/v1/account/save")
                .content(asJsonString(account))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.username").value("Mandy"));
        logger.info("" + account);
    }

    @Test
    public void test_insertAccountWhenNameExistApi_ReturnFalse() throws Exception {
        Mockito.when(accountService.getAllAccountByUsername("Mandy")).thenReturn(List.of(account));
        Mockito.when(accountService.saveAccount(account)).thenReturn(account);
        mvc.perform(post("/api/v1/account/save")
                .content(asJsonString(account))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(false));
    }

    @Test
    public void test_updateOrInsertEmpApi_ReturnSuccess() throws Exception {
        Mockito.when(accountService.updateOrInsertAccount(account)).thenReturn(account);
        mvc.perform(put("/api/v1/account/uploadOrInsert")
                .content(asJsonString(account))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.username").value("Mandy"));
    }

    @Test
    public void test_WhenIdExistedApi_ReturnSuccess() throws Exception {
        Mockito.when(accountService.findByIdAccount(ID)).thenReturn(account);
        mvc.perform(get("/api/v1/account/{id}", ID)
                .content(asJsonString(account))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.username").value("Mandy"));
    }

    @Test
    public void test_WhenIdDoseNotExistedApi_ReturnFalse() throws Exception {
        Mockito.when(accountService.findByIdAccount(ID)).thenReturn(account);
        mvc.perform(get("/api/v1/account/{id}", 8)
                .content(asJsonString(account))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false));
    }

    @Test
    public void test_getAllAccountListApi_ReturnSuccess() throws Exception {
        Mockito.when(accountService.getAllAccount()).thenReturn(List.of(account));
        mvc.perform(get("/api/v1/account/all")
                .content(asJsonString(account))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data[0].id").value(ID))
                .andExpect(jsonPath("$.data[0].username").value("Mandy"))
                .andExpect(jsonPath("$.data", IsCollectionWithSize.hasSize(1)));
    }

    @Test
    public void test_deleteAccountByIdWhenFoundApi_ReturnSuccess() throws Exception {
        Mockito.when(accountService.deleteByIdAccount(ID)).thenReturn(true);
        mvc.perform(delete("/api/v1/account/delete/{id}", ID)
                .content(asJsonString(account))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    public void test_deleteAccountByIdWhenNotFoundApi_ReturnFalse() throws Exception {
        Mockito.when(accountService.deleteByIdAccount(ID)).thenReturn(true);
        mvc.perform(delete("/api/v1/account/delete/{id}", 7)
                .content(asJsonString(account))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
