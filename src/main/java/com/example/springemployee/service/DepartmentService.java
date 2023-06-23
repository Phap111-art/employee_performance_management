package com.example.springemployee.service;

import com.example.springemployee.dto.DepartmentDTO;
import com.example.springemployee.entity.Department;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DepartmentService {
    Department saveDepartment(Department department);
    Department insertOrUpdateDepartment(Department department);
    DepartmentDTO saveDepartmentDTO(DepartmentDTO department);
    DepartmentDTO insertOrUpdateDepartmentDTO(DepartmentDTO department);
    Department findByIdDepartment(String id);
    boolean deleteByIdDepartment(String id);
    List<Department> getAllDepartment();
    List<Department> getAllDepartmentByName(String name);
    List<Department> getAllDepartmentById(String id);
    Page<Department> getDepartmentPageDetailByName(int pageCurrent,int size,String search);
}
