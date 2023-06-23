package com.example.springemployee.repositories;

import com.example.springemployee.entity.Account;
import com.example.springemployee.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,String> {
    List<Department> findAllByName(String name);
    List<Department> findAllById(String id);
    Page<Department> findByNameLike(String username, Pageable pageable);
}
