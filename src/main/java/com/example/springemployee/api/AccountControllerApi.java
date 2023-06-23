package com.example.springemployee.api;

import com.example.springemployee.dto.AccountDTO;
import com.example.springemployee.entity.Account;
import com.example.springemployee.exception.FieldErrorResultMsg;
import com.example.springemployee.model.ResponseObject;
import com.example.springemployee.service.AccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/account")
public class AccountControllerApi {
    private final AccountService accountService;
    final Logger logger = LoggerFactory.getLogger(AccountControllerApi.class);

    public AccountControllerApi(AccountService accountService) {
        this.accountService = accountService;
    }


    @PostMapping("/save")
    public ResponseEntity<ResponseObject> saveAccount(@RequestBody @Valid AccountDTO dto, BindingResult result) {
        List<Account> isUsername = accountService.getAllAccountByUsername(dto.getUsername().trim());
        String error = FieldErrorResultMsg.getMsgError(result);
        if (!error.isEmpty()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject(error, false, "")
            );
        }
        if (!isUsername.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("Username account already exist !", false, "")
            );
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject("insert Account successfully!", true, accountService.saveAccountDTO(dto))
        );

    }

    @PutMapping("/uploadOrInsert")
    public ResponseEntity<ResponseObject> updateOrInsertAccount(@Valid @RequestBody AccountDTO dto,BindingResult result) {
        dto.setId(dto.getId());
        String error = FieldErrorResultMsg.getMsgError(result);
        if (!error.isEmpty()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject(error, false, "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("insert Or Update Account successfully!", true, accountService.updateOrInsertAccountDTO(dto))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findByIdAccount(@PathVariable("id") int id) {
        try {
            Account account = accountService.findByIdAccount(id);
            if (Objects.nonNull(account)) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("found account id is " + id, true, account)
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("error: " + id, false, e)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("account id not found " + id, false, "")
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteByIdAccount(@PathVariable("id") int id) {
        boolean isDel = accountService.deleteByIdAccount(id);
        if (isDel) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    new ResponseObject("Deleting account id " + id + " successfully", true, "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("account id not found " + id, false, "")
        );
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllAccount() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("return all account successfully!", true, accountService.getAllAccount())
        );
    }

    @GetMapping("/detail_page")
    public ResponseEntity<ResponseObject> getAccountPageDetail() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Account detail page !", true, accountService.getDetailPageAccount())
        );
    }
}
