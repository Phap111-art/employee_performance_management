package com.example.springemployee.service;

import com.example.springemployee.entity.Employee;

public interface UpdateProfileService {
    Employee updateProfileEmployee(Employee employee);
    Employee findByIdEmployee(String id);
    Employee findByEmployeeByAccountEmail(String email);
}
