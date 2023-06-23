package com.example.springemployee.controller;

import com.example.springemployee.dto.AccountDTO;
import com.example.springemployee.entity.Account;
import com.example.springemployee.exception.FieldErrorResultMsg;
import com.example.springemployee.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dashboard/manage")
public class AccountController {

    private final AccountService accountService;
    private static final String VIEWS_ACCOUNT_MANAGE = "html/pages-account";

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/pages-account")
    public String pageManageAccount(@RequestParam(defaultValue = "1") int pageCurrent,
                                    @RequestParam(defaultValue = "5") int size,
                                    @RequestParam(defaultValue = "") String search, Model model) {
        model.addAttribute("pageCurrent", pageCurrent);
        model.addAttribute("size", size);
        model.addAttribute("search", search);
        Page<Account> accountPage = accountService.getPageDetailAccountByUsername(pageCurrent, size, search);
        return addPaginationModel(model, accountPage);
    }

    private String addPaginationModel(Model model, Page<Account> paginated) {
        List<Account> listOwners = paginated.getContent();
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("list", listOwners);
        return VIEWS_ACCOUNT_MANAGE;
    }

    @PostMapping("/add-account")
    public String addUser(@RequestParam int pageCurrent,
                          @RequestParam int size,
                          @RequestParam String search,
                          @Valid AccountDTO dto, BindingResult result, Model model) {
        List<Account> isUsername = accountService.getAllAccountByUsername(dto.getUsername().trim());
        List<Account> isEmail = accountService.getAllAccountByEmail(dto.getEmail().trim());
        String errorFiled = FieldErrorResultMsg.getMsgError(result);
        if (!errorFiled.isEmpty()) {
            model.addAttribute("error",errorFiled);
            return pageManageAccount(pageCurrent, size, search, model);
        }
        if (!isUsername.isEmpty()) {
            model.addAttribute("mess", "*Username account already exist !");
            return pageManageAccount(pageCurrent, size, search, model);
        }
        if (!isEmail.isEmpty()) {
            model.addAttribute("mess", "*Email account already exist !");
            return pageManageAccount(pageCurrent, size, search, model);
        }
        if (!result.hasErrors() || isUsername.isEmpty() || isEmail.isEmpty()) {
            model.addAttribute("add_success", "Add Success!");
        }
        accountService.saveAccountDTO(dto);
        return pageManageAccount(pageCurrent, size, search, model);
    }

    @PostMapping("/edit-account")
    public String editAccount(@RequestParam int pageCurrent,
                               @RequestParam int size,
                               @RequestParam String search,
                               @Valid AccountDTO dto, BindingResult result, Model model,
                               @RequestParam(name = "active") String active) {
        if (active.equals("1")){
            dto.setActive(true);
        }
        if (active.equals("0")){
            dto.setActive(false);
        }
        String errorFiled = FieldErrorResultMsg.getMsgError(result);
        if (!errorFiled.isEmpty()) {
            model.addAttribute("error",errorFiled);
            return pageManageAccount(pageCurrent, size, search, model);
        }
        if (!result.hasErrors()) {
            model.addAttribute("edit_success", "Edit Success!");
        }
        accountService.updateOrInsertAccountDTO(dto);
        return pageManageAccount(pageCurrent, size, search, model);
    }

    @GetMapping("/del-account")
    public String delAccount(@RequestParam(defaultValue = "1") int pageCurrent,
                              @RequestParam(defaultValue = "5") int size,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam int id, Model model) {
        accountService.deleteByIdAccount(id);
        return pageManageAccount(pageCurrent, size, search, model);
    }

    @ModelAttribute("USER")
    public AccountDTO init() {
        return new AccountDTO();
    }


}
