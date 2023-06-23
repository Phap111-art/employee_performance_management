package com.example.springemployee.controller;


import com.example.springemployee.dto.DepartmentDTO;
import com.example.springemployee.entity.Department;
import com.example.springemployee.exception.FieldErrorResultMsg;
import com.example.springemployee.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dashboard/manage")
public class DepartmentController {
    private final DepartmentService departmentService;
    private static final String VIEWS_DEPARTMENT_MANAGE = "html/pages-department";

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }


    @ModelAttribute("USER")
    public DepartmentDTO init() {
        return new DepartmentDTO();
    }

    @GetMapping("/pages-department")
    public String pageManageDepartment(@RequestParam(defaultValue = "1") int pageCurrent,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam(defaultValue = "") String search,
                                       Model model) {
        model.addAttribute("pageCurrent", pageCurrent);
        model.addAttribute("size", size);
        model.addAttribute("search", search);
        Page<Department> paginated = departmentService.getDepartmentPageDetailByName(pageCurrent, size, search);
        return addPaginationModel(model, paginated);
    }
    private String addPaginationModel(Model model, Page<Department> paginated) {
        List<Department> listOwners = paginated.getContent();
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("list", listOwners);
        return VIEWS_DEPARTMENT_MANAGE;
    }

    @PostMapping("/add-department")
    public String addDepartment(@RequestParam int pageCurrent,
                                @RequestParam int size,
                                @RequestParam String search,
                                @Valid DepartmentDTO dto, BindingResult result, Model model) {
        List<Department> isNameAlreadyExist = departmentService.getAllDepartmentByName(dto.getName().trim());
        List<Department> isIdAlreadyExist = departmentService.getAllDepartmentById(dto.getId());
        String validId = dto.validId(dto.getId());
        if (result.hasErrors()) {
            fieldErrorValidation(result,model);
            return pageManageDepartment(pageCurrent, size, search, model);
        }
        if (!validId.isEmpty()) {
            model.addAttribute("error",validId);
            return pageManageDepartment(pageCurrent, size, search, model);
        }
        if (!isIdAlreadyExist.isEmpty()) {
            model.addAttribute("error","*Id department already exist !");
            return pageManageDepartment(pageCurrent, size, search, model);
        }
        if (!isNameAlreadyExist.isEmpty()) {
            model.addAttribute("error","*Name department already exist !");
            return pageManageDepartment(pageCurrent, size, search, model);
        }
        if (!result.hasErrors() || isNameAlreadyExist.isEmpty() || isIdAlreadyExist.isEmpty() || validId.isEmpty()) {
            model.addAttribute("add_success", "Add Success!");
        }
        departmentService.saveDepartmentDTO(dto);
        return pageManageDepartment(pageCurrent, size, search, model);
    }

    @PostMapping("/edit-department")
    public String editDepartment(@RequestParam int pageCurrent,
                                 @RequestParam int size,
                                 @RequestParam String search,
                                 @Valid DepartmentDTO dto, BindingResult result,Model model) {
        String errorFiled = FieldErrorResultMsg.getMsgError(result);
        if (!errorFiled.isEmpty()) {
            model.addAttribute("error",errorFiled);
            return pageManageDepartment(pageCurrent, size, search, model);
        }
        String validId = dto.validId(dto.getId());
        if (!validId.isEmpty()) {
            model.addAttribute("error",validId);
            return pageManageDepartment(pageCurrent, size, search, model);
        }
        if (errorFiled.isEmpty() || validId.isEmpty()) {
            model.addAttribute("edit_success", "Edit Success!");
        }
        departmentService.insertOrUpdateDepartmentDTO(dto);
        return pageManageDepartment(pageCurrent, size, search, model);
    }

    @GetMapping("/del-department")
    public String delDepartment(@RequestParam int pageCurrent,
                                @RequestParam int size,
                                @RequestParam String search,
                                @RequestParam String id, Model model) {
        departmentService.deleteByIdDepartment(id);
        return pageManageDepartment(pageCurrent, size, search, model);
    }
    private void fieldErrorValidation(BindingResult result, Model model) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        String errorMess = "";
        for (Map.Entry<String, String> entry : errors.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            errorMess = "Error in Filed : " + key + " - Because : " + value;
        }
        model.addAttribute("error", errorMess);
    }

}
