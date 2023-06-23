package com.example.springemployee.repositories;

import com.example.springemployee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,String> {
    List<Employee> findAllById(String id);
    List<Employee> findAllByName(String name);
    List<Employee> findByAccount_Id(int id);
    List<Employee> findByDepartment_Id(String id);
    Page<Employee> findByNameLike(String username, Pageable pageable);
    @Query("SELECT e FROM Employee e WHERE  e.account.username  = :username")
    Employee findByEmployeeByAccountUsername(@Param("username") String username);
    @Query("SELECT e FROM Employee e WHERE  e.account.email  = :email")
    Employee findByEmployeeByAccountEmail(@Param("email") String email);
    @Query("SELECT e FROM Employee e WHERE  e.id  = :id")
    Employee getByIdEmployee(@Param("id") String id);
}
