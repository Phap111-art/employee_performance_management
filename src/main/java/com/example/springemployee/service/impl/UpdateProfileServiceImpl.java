package com.example.springemployee.service.impl;

import com.example.springemployee.entity.Department;
import com.example.springemployee.entity.Employee;
import com.example.springemployee.exception.DataNotFound;
import com.example.springemployee.repositories.AccountRepository;
import com.example.springemployee.repositories.EmployeeRepository;
import com.example.springemployee.service.UpdateProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Random;

@Service
public class UpdateProfileServiceImpl implements UpdateProfileService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final AccountRepository accountRepository;

    public UpdateProfileServiceImpl(EmployeeRepository employeeRepository, AccountRepository accountRepository) {
        this.employeeRepository = employeeRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Employee updateProfileEmployee(Employee employee) {

        Employee update = employeeRepository.findById(employee.getId()).map(data -> { // update emp
            data.setId(data.getId().toUpperCase(Locale.ROOT));
            data.setName(employee.getAccount().getFullName().toUpperCase(Locale.ROOT));
            data.setGender(employee.isGender());
            data.setPhone(employee.getPhone());
            data.setNote(employee.getNote());
            data.setPhoto(employee.getPhoto());
            data.setBirthday(employee.getBirthday());
            return employeeRepository.save(data);
        }).orElseGet(() -> { //new Emp
            employee.setId(getRandomIdEmployee()); //id random
            employee.setName(employee.getAccount().getFullName().toUpperCase(Locale.ROOT));
            employee.setAccount(employee.getAccount());
            employee.setDepartment(Department.builder().id("PB01").build());

            return employeeRepository.save(employee);
        });
        accountRepository.findById(employee.getAccount().getId()).map(data -> { //update account
            data.setUsername(employee.getAccount().getUsername());
            data.setFullName(employee.getAccount().getFullName().toUpperCase(Locale.ROOT));
            if (employee.getAccount().getPassword().startsWith("$2a$")){
                data.setPassword(employee.getAccount().getPassword());
            }else{
                data.setPassword(passwordEncoder.encode(employee.getAccount().getPassword()));
            }
            data.setStatus(1);
            data.setPassword(passwordEncoder.encode(employee.getAccount().getPassword()));
            data.setEmail(employee.getAccount().getEmail());
            data.setPhoto(employee.getPhoto());
            return accountRepository.save(data);
        }).orElseThrow(DataNotFound::new);


        return employeeRepository.save(update);
    }

    @Override
    public Employee findByIdEmployee(String id) {
        return employeeRepository.findById(id).orElseThrow(DataNotFound::new);
    }

    @Override
    public Employee findByEmployeeByAccountEmail(String email) {
        return employeeRepository.findByEmployeeByAccountEmail(email);
    }

    String getRandomIdEmployee() {
        Random random = new Random();
        int num = random.nextInt(90) + 10;
        Employee idEmp = employeeRepository.getByIdEmployee("NV" + num);
        while (idEmp != null) {
            num = random.nextInt(90) + 10;
            if (idEmp == null) {
                break;
            }
        }
        return "NV" + num;
    }
}
