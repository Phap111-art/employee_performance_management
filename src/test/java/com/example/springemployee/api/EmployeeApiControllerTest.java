package com.example.springemployee.api;

import com.example.springemployee.entity.*;
import com.example.springemployee.entity.Employee;
import com.example.springemployee.utility.DateUtils;
import com.example.springemployee.service.EmployeeService;
import com.example.springemployee.service.EmployeeServiceTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeApiControllerTest {
    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mvc;

    ObjectMapper om = new ObjectMapper();

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

    @Test
    public void test_insertEmployeeWhenNameDoesNotExistApi_ReturnSuccess() throws Exception {
        Mockito.when(employeeService.getAllEmployeeById(ID_EMPLOYEE)).thenReturn(List.of(employee));
        Mockito.when(employeeService.saveEmployee(employee)).thenReturn(employee);
        mvc.perform(post("/api/v1/employee/save")
                .content(asJsonString(employee))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.name").value("Mr. Mandy"));
    }
    @Test
    public void test_insertEmployeeWhenNameExistApi_ReturnFalse() throws Exception {
        Mockito.when(employeeService.getAllEmployeeById(ID_EMPLOYEE)).thenReturn(List.of(employee));
        Mockito.when(employeeService.saveEmployee(employee)).thenReturn(employee);
        mvc.perform(post("/api/v1/employee/save")
                .content(asJsonString(employee))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(false));
    }
    @Test
    public void test_updateOrInsertEmpApi_ReturnSuccess() throws Exception {
        Mockito.when(employeeService.insertOrUpdateEmployee(employee)).thenReturn(employee);
        mvc.perform(put("/api/v1/employee/uploadOrInsert")
                .content(asJsonString(employee))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.name").value("Mr. Mandy"));
    }
    @Test
    public void test_WhenIdExistedApi_ReturnSuccess() throws Exception {
        Mockito.when(employeeService.findByIdEmployee(ID_EMPLOYEE)).thenReturn(employee);
        mvc.perform(get("/api/v1/employee/{id}",ID_EMPLOYEE)
                .content(asJsonString(employee))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.name").value("Mr. Mandy"));
    }
    @Test
    public void test_WhenIdDoseNotExistedApi_ReturnFalse() throws Exception {
        Mockito.when(employeeService.findByIdEmployee(ID_EMPLOYEE)).thenReturn(employee);
        mvc.perform(get("/api/v1/employee/{id}","PB09")
                .content(asJsonString(employee))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false));
    }
    @Test
    public void test_getAllEmployeeListApi_ReturnSuccess() throws Exception {
        Mockito.when(employeeService.getAllEmployee()).thenReturn(List.of(employee));
        mvc.perform(get("/api/v1/employee/all")
                .content(asJsonString(employee))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data[0].id").value(ID_EMPLOYEE))
                .andExpect(jsonPath("$.data[0].name").value("Mr. Mandy"))
                .andExpect(jsonPath("$.data", IsCollectionWithSize.hasSize(1)));
    }
    @Test
    public void test_deleteEmployeeByIdWhenFoundApi_ReturnSuccess() throws Exception {
        Mockito.when(employeeService.deleteByIdEmployee(ID_EMPLOYEE)).thenReturn(true);
        mvc.perform(delete("/api/v1/employee/delete/{id}",ID_EMPLOYEE)
                .content(asJsonString(employee))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value(true));
    }
    @Test
    public void test_deleteEmployeeByIdWhenNotFoundApi_ReturnFalse() throws Exception {
        Mockito.when(employeeService.deleteByIdEmployee(ID_EMPLOYEE)).thenReturn(true);
        mvc.perform(delete("/api/v1/employee/delete/{id}","PB07")
                .content(asJsonString(employee))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false));
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
