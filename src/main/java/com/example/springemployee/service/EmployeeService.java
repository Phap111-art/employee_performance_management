package com.example.springemployee.service;

import com.example.springemployee.entity.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    Employee insertOrUpdateEmployee(Employee employee);
    Employee findByIdEmployee(String id);
    boolean deleteByIdEmployee(String id);
    void updateStatusDepartment(String id,int status);
    void updateStatusAccount(int id, int status);
    void updateStatusEmployee(String id, int status);
    List<Employee> getAllEmployee();
    List<Employee> getAllEmployeeByName(String name);
    List<Employee> getAllEmployeeById(String id);
    List<Employee> getAllEmployeeByAccountId(int id);
    List<Employee> getAllEmployeeByDepartmentId(String id);
    Page<Employee> getEmployeePageDetail();
    Page<Employee> getAllDetailPageBySearchName(int pageCurrent,int size,String search);
}
