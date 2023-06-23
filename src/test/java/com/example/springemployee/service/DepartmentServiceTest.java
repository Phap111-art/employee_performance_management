package com.example.springemployee.service;


import com.example.springemployee.entity.Department;
import com.example.springemployee.service.impl.DepartmentServiceImpl;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {
    @Mock
    private com.example.springemployee.repositories.DepartmentRepository departmentRepository;

    private DepartmentService departmentService;

    private static final String ID = "PD01";

    private final Logger logger = LoggerFactory.getLogger(DepartmentServiceTest.class);

    private static final Department department = Department.builder().id(ID).name("Ke toan").build();

    @BeforeEach
    void init() {
        departmentService = new DepartmentServiceImpl(departmentRepository);
    }

    @Test
    void test_insertDepartment_ReturnSuccess() {
        Mockito.when(departmentRepository.save(department)).thenReturn(department);
        Department actualDepart = departmentService.saveDepartment(department);
        Assert.assertNotNull(actualDepart);
        Assert.assertEquals(department, actualDepart);
        logger.info("data : " + actualDepart);
    }

    @Test
    void test_updateOrInsertDepartment_ReturnSuccess() {
        Mockito.when(departmentRepository.findById(ID)).thenReturn(Optional.of(department));
        Mockito.when(departmentRepository.save(department)).thenReturn(department);
        Department updateDepart = departmentService.insertOrUpdateDepartment(department);
        Assert.assertNotNull(updateDepart);
        Assert.assertEquals(department, updateDepart);
        logger.info("data : " + updateDepart);
    }

    @Test
    void test_deleteDepartmentById_ReturnSuccess() {
        Mockito.when(departmentRepository.existsById(ID)).thenReturn(true);
        boolean isDel = departmentService.deleteByIdDepartment(ID);
        Assert.assertEquals(isDel, true);
        logger.info("id del is : " + isDel);
    }

    @Test
    void test_deleteDepartmentDoseNotExist_ReturnFalse() {
        Mockito.when(departmentRepository.existsById(ID)).thenReturn(false);
        try {
            boolean isDel = departmentService.deleteByIdDepartment(ID);
            Assert.assertEquals(isDel, false);
        } catch (Exception e) {
            logger.info("" + e);
        }
    }

    @Test
    void test_findDepartmentById_ReturnSuccess() {
        Mockito.when(departmentRepository.findById(ID)).thenReturn(Optional.of(department));
        Department actualDepart = departmentService.findByIdDepartment(ID);
        Assert.assertEquals(actualDepart, department);
        logger.info("" + actualDepart);
    }

    @Test
    void test_departmentPageDetail_ReturnSuccess(){

    }
    @Test
    void test_getAllDepartmentList_ReturnSuccess(){
        Mockito.when(departmentRepository.findAll()).thenReturn(List.of(department));
        List<Department> departments = departmentService.getAllDepartment();
        Assert.assertThat(departments.size(), Matchers.greaterThanOrEqualTo(1));
        logger.info("size : " + departments.size());
    }
}
