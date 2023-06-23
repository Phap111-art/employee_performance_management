package com.example.springemployee.service;

import com.example.springemployee.entity.Account;
import com.example.springemployee.entity.Department;
import com.example.springemployee.entity.Employee;
import com.example.springemployee.entity.Role;
import com.example.springemployee.utility.DateUtils;
import com.example.springemployee.repositories.EmployeeRepository;
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
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeService employeeService;

    private static final String ID_EMPLOYEE = "NV01";
    private static final String ID_DEPARTMENT = "PB01";
    private static final int ID_ACCOUNT = 1;
    private static final int ID_ROLE = 1;

    private final Logger logger = LoggerFactory.getLogger(EmployeeServiceTest.class);

    private static final DateUtils CONFIG_DATE = DateUtils.getInstance();
    private static final Role role = Role.builder().id(ID_ROLE).name("USER").build();
    private static final Account account = Account.builder().id(ID_ACCOUNT).username("Mandy123456").password("123456").roles(Set.of(role)).build();
    private static final Department department = Department.builder().id(ID_DEPARTMENT).name("personnel").build();
    private static final Employee employee = Employee.builder().id(ID_EMPLOYEE).name("Mr. Mandy").birthday(CONFIG_DATE.getStringToDate("19/11/2000"))
                                            .account(account).department(department).build();
    @BeforeEach
    void init() {
//        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    @Test
    void test_insertEmployee_ReturnSuccess() {
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);
        Employee actualDepart = employeeService.saveEmployee(employee);
        Assert.assertNotNull(actualDepart);
        Assert.assertEquals(employee, actualDepart);
        logger.info("data : " + actualDepart);
    }

    @Test
    void test_updateOrInsertEmployee_ReturnSuccess() {
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);
        Employee updateEmployee = employeeService.insertOrUpdateEmployee(employee);
        Assert.assertNotNull(updateEmployee);
        Assert.assertEquals(employee, updateEmployee);
        logger.info("data : " + updateEmployee);
    }

    @Test
    void test_deleteEmployeeById_ReturnSuccess() {
        Mockito.when(employeeRepository.existsById(ID_EMPLOYEE)).thenReturn(true);
        boolean isDel = employeeService.deleteByIdEmployee(ID_EMPLOYEE);
        Assert.assertEquals(isDel, true);
        logger.info("id del is : " + isDel);
    }

    @Test
    void test_deleteEmployeeDoseNotExist_ReturnFalse() {
        Mockito.when(employeeRepository.existsById(ID_EMPLOYEE)).thenReturn(false);
        try {
            boolean isDel = employeeService.deleteByIdEmployee(ID_EMPLOYEE);
            Assert.assertEquals(isDel, false);
        } catch (Exception e) {
            logger.info("" + e);
        }
    }

    @Test
    void test_findEmployeeById_ReturnSuccess() {
        Mockito.when(employeeRepository.findById(ID_EMPLOYEE)).thenReturn(Optional.of(employee));
        Employee actualEmployee = employeeService.findByIdEmployee(ID_EMPLOYEE);
        Assert.assertEquals(actualEmployee, employee);
        logger.info("" + actualEmployee);
    }

    @Test
    void test_departmentPageDetail_ReturnSuccess(){

    }
    @Test
    void test_getAllEmployeeList_ReturnSuccess(){
        Mockito.when(employeeRepository.findAll()).thenReturn(List.of(employee));
        List<Employee> departments = employeeService.getAllEmployee();
        Assert.assertThat(departments.size(), Matchers.greaterThanOrEqualTo(1));
        logger.info("size : " + departments.size());
    }
}
