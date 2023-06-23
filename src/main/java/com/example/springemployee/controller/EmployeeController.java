package com.example.springemployee.controller;

import com.example.springemployee.entity.Account;
import com.example.springemployee.entity.Department;
import com.example.springemployee.entity.Employee;
import com.example.springemployee.utility.DateUtils;
import com.example.springemployee.service.AccountService;
import com.example.springemployee.service.DepartmentService;
import com.example.springemployee.service.EmployeeService;
import com.example.springemployee.service.StorageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class EmployeeController {
    private final EmployeeService employeeService;
    private final AccountService accountService;
    private final DepartmentService departmentService;
    private final StorageService storageService;
    private static final String VIEWS_EMPLOYEE_MANAGE = "html/pages-employee";
    final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    public EmployeeController(EmployeeService employeeService, AccountService accountService, DepartmentService departmentService, StorageService storageService) {
        this.employeeService = employeeService;
        this.accountService = accountService;
        this.departmentService = departmentService;
        this.storageService = storageService;
    }


    @ModelAttribute("ACCOUNT")
    public List<Account> getAllAccount() {
        return accountService.getAllAccount();
    }

    @ModelAttribute("DEPARTMENT")
    public List<Department> getAllDepartment() {
        return departmentService.getAllDepartment();
    }

    @ModelAttribute("USER")
    public Employee init() {
        return new Employee();
    }

    @GetMapping("/pages-employee")
    public String pageManageEmployee(@RequestParam(defaultValue = "1") int pageCurrent,
                                     @RequestParam(defaultValue = "5") int size,
                                     @RequestParam(defaultValue = "") String search,
                                     Model model) {
        model.addAttribute("getAccountListDoesNotExitsInTheEmployee",accountService.getAllAccountDoNotExistInTheListOfEmployee());
        model.addAttribute("pageCurrent", pageCurrent);
        model.addAttribute("size", size);
        model.addAttribute("search", search);
        Page<Employee> paginated = employeeService.getAllDetailPageBySearchName(pageCurrent, size, search);
        return addPaginationModel(model, paginated);
    }

    private String addPaginationModel(Model model, Page<Employee> paginated) {
        List<Employee> listOwners = paginated.getContent();
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("list", listOwners);
        return VIEWS_EMPLOYEE_MANAGE;
    }

    @PostMapping("/add-employee")
    public String addEmployee(@RequestParam int pageCurrent,
                              @RequestParam int size,
                              @RequestParam String search,
                              @Valid Employee employee, BindingResult result, Model model) {
        employee.setBirthday(DateUtils.getInstance().getStringToDate(employee.getDate()));
        String messError = messageAllErrorAdd(result, employee, pageCurrent, size, search, model);
        if (!messError.isEmpty()) {
            return messError;
        }
        if (employee.getFile() != null) {
            employee.setPhoto(storageService.storeAdd(employee.getFile()));
        }
        if (employee.getFile().isEmpty() || employee.getFile() == null) {
            employee.setPhoto("no_avatar.jpg");
        }
        if (messError.isEmpty()) {
            model.addAttribute("add_success", "Add Success!");
        }
        employeeService.updateStatusAccount(employee.getAccount_id(), 1);
        employeeService.updateStatusDepartment(employee.getDepartment_id(), 1);
        employeeService.saveEmployee(employee);
        return pageManageEmployee(pageCurrent, size, search, model);
    }

    @PostMapping("/edit-employee")
    public String editEmployee(@RequestParam int pageCurrent,
                               @RequestParam int size,
                               @RequestParam String search,
                               @Valid Employee employee, BindingResult result, Model model) {

        employee.setBirthday(DateUtils.getInstance().getStringToDate(employee.getDate()));
        String messError = messageAllErrorEdit(result, employee, pageCurrent, size, search, model);
        if (!messError.isEmpty()) {
            return messError;
        }
        if (messError.isEmpty()) {
            model.addAttribute("edit_success", "Edit Success!");
        }
        if (employee.getFile() != null) {
            employee.setPhoto(storageService.storeAdd(employee.getFile()));
        }
        if (employee.getFile().isEmpty() || employee.getFile() == null) {
            Employee updateFile = employeeService.findByIdEmployee(employee.getId());
            employee.setPhoto(updateFile.getPhoto());
        }
        setDepartmentUpdateStatusOldAndStatusNew(employee.getDepartment_id(), employee.getId());
        employeeService.insertOrUpdateEmployee(employee);
        return pageManageEmployee(pageCurrent, size, search, model);
    }

    @GetMapping("/del-employee")
    public String delEmployee(@RequestParam int pageCurrent,
                              @RequestParam int size,
                              @RequestParam String search,
                              @RequestParam String id, Model model) {
        Employee employee = employeeService.findByIdEmployee(id);
        List<Employee> empForAccount = employeeService.getAllEmployeeByAccountId(employee.getAccount().getId());
        List<Employee> empForDepartment = employeeService.getAllEmployeeByDepartmentId(employee.getDepartment().getId());
        if (empForAccount.size() == 1) {
            employeeService.updateStatusAccount(employee.getAccount().getId(), 0);
        }
        if (empForDepartment.size() == 1) {
            employeeService.updateStatusDepartment(employee.getDepartment().getId(), 0);
        }
        employeeService.deleteByIdEmployee(id);
        return pageManageEmployee(pageCurrent, size, search, model);
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

    public void setDepartmentUpdateStatusOldAndStatusNew(String idDepartNew, String idEmp) {
        Employee employee = employeeService.findByIdEmployee(idEmp);
        String idDepartOld = employee.getDepartment().getId();
        employee.setDepartment_id(idDepartNew);
        employee.setAccount_id(employee.getAccount().getId());
        List<Employee> empForDepartmentOld = employeeService.getAllEmployeeByDepartmentId(idDepartOld);
        if (empForDepartmentOld.size() == 1) {
            employeeService.updateStatusDepartment(idDepartOld, 0);
        }
        employeeService.insertOrUpdateEmployee(employee);
        List<Employee> empForDepartmentNew = employeeService.getAllEmployeeByDepartmentId(idDepartNew);
        if (empForDepartmentNew.size() == 1) {
            employeeService.updateStatusDepartment(idDepartNew, 1);
        }
    }

    private String messageAllErrorAdd(BindingResult result, Employee employee, int pageCurrent, int size, String search, Model model) {
        String validId = employee.validId(employee.getId());
        List<Employee> isNameAlreadyExist = employeeService.getAllEmployeeByName(employee.getName().trim());
        List<Employee> isIdAlreadyExist = employeeService.getAllEmployeeById(employee.getId());
        List<Account> accountList = accountService.getAllAccountDoNotExistInTheListOfEmployee();
        if (accountList.size() == 0 || accountList.isEmpty()) {
            model.addAttribute("error", "*You need to add an account for employees!");
            return pageManageEmployee(pageCurrent, size, search, model);
        }
        if (!isIdAlreadyExist.isEmpty()) {
            model.addAttribute("error", "*Id employee already exist !");
            return pageManageEmployee(pageCurrent, size, search, model);
        }
        if (!isNameAlreadyExist.isEmpty()) {
            model.addAttribute("error", "*Name employee already exist !");
            return pageManageEmployee(pageCurrent, size, search, model);
        }
        if (result.hasErrors()) {
            fieldErrorValidation(result, model);
            return pageManageEmployee(pageCurrent, size, search, model);
        }
        if (!validId.isEmpty()) {
            model.addAttribute("error", validId);
            return pageManageEmployee(pageCurrent, size, search, model);
        }
        return "";
    }

    private String messageAllErrorEdit(BindingResult result, Employee employee, int pageCurrent, int size, String search, Model model) {
        String validId = employee.validId(employee.getId());
        if (result.hasErrors()) {
            fieldErrorValidation(result, model);
            return pageManageEmployee(pageCurrent, size, search, model);
        }
        if (!validId.isEmpty()) {
            model.addAttribute("error", validId);
            return pageManageEmployee(pageCurrent, size, search, model);
        }
        return "";
    }
}
