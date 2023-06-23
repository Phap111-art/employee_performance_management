package com.example.springemployee.service.impl;

import com.example.springemployee.dto.DepartmentDTO;
import com.example.springemployee.entity.Department;
import com.example.springemployee.exception.DataNotFound;

import com.example.springemployee.mapper.DepartmentMp;
import com.example.springemployee.repositories.DepartmentRepository;
import com.example.springemployee.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMp departmentMp;
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department saveDepartment(Department department) {
        department.setName(department.getName().toUpperCase(Locale.ROOT));
        return departmentRepository.save(department);
    }

    @Override
    public Department insertOrUpdateDepartment(Department department) {
        return departmentRepository.findById(department.getId()).map(data -> {
            data.setName(department.getName());
            return saveDepartment(data);
        }).orElseThrow(DataNotFound::new);
    }

    @Override
    public DepartmentDTO saveDepartmentDTO(DepartmentDTO dto) {
        Department department = departmentMp.toEntity(dto);
        department.setName(dto.getName().toUpperCase(Locale.ROOT));
        return departmentMp.toDto(departmentRepository.save(department));
    }

    @Override
    public DepartmentDTO insertOrUpdateDepartmentDTO(DepartmentDTO dto) {
        return departmentRepository.findById(dto.getId()).map(data -> {
            data.setName(dto.getName().toUpperCase(Locale.ROOT));
            return departmentMp.toDto(saveDepartment(data));
        }).orElseThrow(DataNotFound::new);
    }

    @Override
    public Department findByIdDepartment(String id) {
        return departmentRepository.findById(id).orElseGet(()->{
            throw new DataNotFound("id " + id + " not found in data!");
        });
    }

    @Override
    public boolean deleteByIdDepartment(String id) {
        boolean isDel = departmentRepository.existsById(id);
        if (isDel) {
            departmentRepository.deleteById(id);
            return true;
        }
        throw new IllegalArgumentException("id " + id + " do not exist!");
    }



    @Override
    public List<Department> getAllDepartment() {
        return departmentRepository.findAll();
    }

    @Override
    public List<Department> getAllDepartmentByName(String name) {
        return departmentRepository.findAllByName(name);
    }

    @Override
    public List<Department> getAllDepartmentById(String id) {
        return departmentRepository.findAllById(id);
    }


    @Override
    public Page<Department> getDepartmentPageDetailByName(int pageCurrent, int size, String search) {
        Pageable pageable = PageRequest.of(pageCurrent-1,size);
        return departmentRepository.findByNameLike(search + "%",pageable);
    }
}
