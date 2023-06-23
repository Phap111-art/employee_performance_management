package com.example.springemployee.controller;

import com.example.springemployee.entity.Account;
import com.example.springemployee.entity.Employee;
import com.example.springemployee.service.AccountService;
import com.example.springemployee.service.StorageService;
import com.example.springemployee.service.UpdateProfileService;
import com.example.springemployee.utility.DateUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/dashboard/")

public class ProfileController {
    private final AccountService accountService;
    private final UpdateProfileService updateProfileService;
    private final StorageService storageService;
    private static final String VIEWS_PROFILE = "html/pages-profile";
    final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    public ProfileController(AccountService accountService, UpdateProfileService updateProfileService, StorageService storageService) {
        this.accountService = accountService;
        this.updateProfileService = updateProfileService;
        this.storageService = storageService;
    }


    @GetMapping("/pages-profile")
    public String profile(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Account accountSession = (Account) session.getAttribute("ACCOUNT");
        Account account = accountService.findByIdAccount(accountSession.getId());
        model.addAttribute("ACC",account);
        Employee employee = updateProfileService.findByEmployeeByAccountEmail(account.getEmail());
        if (employee == null) {
            model.addAttribute("EMPLOYEE", new Employee());
        } else {
            model.addAttribute("EMPLOYEE", employee);
        }
        return VIEWS_PROFILE;
    }

    @PostMapping("/update-profile")
    public String updateOrInsertEmployee(@Valid Employee employee, BindingResult result, Model model, HttpServletRequest request) {

        employee.setBirthday(DateUtils.getInstance().getStringToDate(employee.getDate()));
        if (employee.getFile() != null) {
            employee.setPhoto(storageService.storeAdd(employee.getFile()));
        }
        if (employee.getFile().isEmpty() || employee.getFile() == null) {
            Employee updateFile = updateProfileService.findByIdEmployee(employee.getId());
            employee.setPhoto(updateFile.getPhoto());
        }
        model.addAttribute("edit_success", "*Update Profile Success!");
        updateProfileService.updateProfileEmployee(employee);
        return profile(model, request);
    }

}
