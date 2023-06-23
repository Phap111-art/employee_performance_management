package com.example.springemployee.service.impl;

import com.example.springemployee.entity.Account;
import com.example.springemployee.entity.Department;
import com.example.springemployee.entity.Employee;
import com.example.springemployee.exception.DataNotFound;
import com.example.springemployee.repositories.AccountRepository;
import com.example.springemployee.repositories.DepartmentRepository;
import com.example.springemployee.repositories.EmployeeRepository;
import com.example.springemployee.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AccountRepository accountRepository;
    private final DepartmentRepository departmentRepository;


    public EmployeeServiceImpl(EmployeeRepository employeeRepository, AccountRepository accountRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.accountRepository = accountRepository;
        this.departmentRepository = departmentRepository;
    }


    @Override
    public Employee saveEmployee(Employee employee) {
        employee.setId(employee.getId().toUpperCase(Locale.ROOT));
        employee.setName(employee.getName().toUpperCase(Locale.ROOT));
        employee.setBirthday(employee.getBirthday());
        employee.setAccount(accountRepository.findById(employee.getAccount_id()).orElseThrow(DataNotFound::new));
        employee.setDepartment(departmentRepository.findById(employee.getDepartment_id()).orElseThrow(DataNotFound::new));
        employee.setBirthday(employee.getBirthday());
        accountRepository.findById(employee.getAccount().getId()).map(data -> { //update account photo
            data.setPhoto(employee.getPhoto());
            if (data.getFullName().isEmpty()) {
                data.setFullName(employee.getName().toUpperCase(Locale.ROOT));
            }
            return accountRepository.save(data);
        }).orElseThrow(DataNotFound::new);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee insertOrUpdateEmployee(Employee employee) {
        Employee update = employeeRepository.findById(employee.getId()).map(data -> {
            data.setId(data.getId().toUpperCase(Locale.ROOT));
            data.setName(employee.getName());
            data.setDepartment(departmentRepository.findById(employee.getDepartment_id()).orElseThrow(DataNotFound::new));
            data.setGender(employee.isGender());
            data.setSalary(employee.getSalary());
            data.setPhone(employee.getPhone());
            data.setNote(employee.getNote());
            data.setPhoto(employee.getPhoto());
            data.setBirthday(employee.getBirthday());
            return employeeRepository.save(data);
        }).orElseThrow(DataNotFound::new);
        return employeeRepository.save(update);
    }

    @Override
    public Employee findByIdEmployee(String id) {
        return employeeRepository.findById(id).orElseGet(() -> {
            throw new DataNotFound("id " + id + " not found in data!");
        });
    }

    @Override
    public boolean deleteByIdEmployee(String id) {
        boolean isDel = employeeRepository.existsById(id);
        if (isDel) {
            employeeRepository.deleteById(id);
            return true;
        }
        throw new IllegalArgumentException("id " + id + " do not exist!");
    }

    @Override
    public void updateStatusDepartment(String id, int status) {
        Department department = departmentRepository.findById(id).map(data -> {
            data.setStatus(status);
            return departmentRepository.save(data);
        }).orElseThrow(DataNotFound::new);
        departmentRepository.save(department);
    }

    @Override
    public void updateStatusAccount(int id, int status) {
        Account update = accountRepository.findById(id).orElseThrow(DataNotFound::new);
        update.setStatus(status);
        accountRepository.save(update);
    }

    @Override
    public void updateStatusEmployee(String id, int status) {
        Employee employee = employeeRepository.findById(id).orElseThrow(DataNotFound::new);
        employee.setStatus(status);
        employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }


    @Override
    public List<Employee> getAllEmployeeByName(String name) {
        return employeeRepository.findAllByName(name);
    }


    @Override
    public List<Employee> getAllEmployeeById(String id) {
        return employeeRepository.findAllById(id);
    }

    @Override
    public List<Employee> getAllEmployeeByAccountId(int id) {
        return employeeRepository.findByAccount_Id(id);
    }

    @Override
    public List<Employee> getAllEmployeeByDepartmentId(String id) {
        return employeeRepository.findByDepartment_Id(id);
    }

    @Override
    public Page<Employee> getEmployeePageDetail() {
        Pageable pageable = PageRequest.of(0, 5);
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Page<Employee> getAllDetailPageBySearchName(int pageCurrent, int size, String search) {
        Pageable pageable = PageRequest.of(pageCurrent - 1, size);
        return employeeRepository.findByNameLike(search + "%", pageable);
    }

}
