package com.example.springemployee.api;

import com.example.springemployee.entity.Department;
import com.example.springemployee.service.DepartmentService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DepartmentApiControllerTest {
    @MockBean
    private DepartmentService departmentService;

    @Autowired
    private MockMvc mvc;

    ObjectMapper om = new ObjectMapper();

    private static final String ID = "PB01";

    private final Logger logger = LoggerFactory.getLogger(DepartmentApiControllerTest.class);

    private static final Department department = Department.builder().id(ID).name("personnel").build();

    @Test
    public void test_insertDepartmentWhenNameDoesNotExistApi_ReturnSuccess() throws Exception {
        Mockito.when(departmentService.getAllDepartmentById("PB04")).thenReturn(List.of(department));
        Mockito.when(departmentService.getAllDepartmentByName("marketing")).thenReturn(List.of(department));
        Mockito.when(departmentService.saveDepartment(department)).thenReturn(department);
        mvc.perform(post("/api/v1/department/save")
                .content(asJsonString(department))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.name").value("personnel"));
    }
    @Test
    public void test_insertDepartmentWhenNameExistApi_ReturnFalse() throws Exception {
        Mockito.when(departmentService.getAllDepartmentByName("personnel")).thenReturn(List.of(department));
        Mockito.when(departmentService.saveDepartment(department)).thenReturn(department);
        mvc.perform(post("/api/v1/department/save")
                .content(asJsonString(department))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(false));
    }
    @Test
    public void test_updateOrInsertEmpApi_ReturnSuccess() throws Exception {
        Mockito.when(departmentService.insertOrUpdateDepartment(department)).thenReturn(department);
        mvc.perform(put("/api/v1/department/uploadOrInsert")
                .content(asJsonString(department))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.name").value("personnel"));
    }
    @Test
    public void test_WhenIdExistedApi_ReturnSuccess() throws Exception {
        Mockito.when(departmentService.findByIdDepartment(ID)).thenReturn(department);
        mvc.perform(get("/api/v1/department/{id}",ID)
                .content(asJsonString(department))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.name").value("personnel"));
    }
    @Test
    public void test_WhenIdDoseNotExistedApi_ReturnFalse() throws Exception {
        Mockito.when(departmentService.findByIdDepartment(ID)).thenReturn(department);
        mvc.perform(get("/api/v1/department/{id}","PB09")
                .content(asJsonString(department))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false));
    }
    @Test
    public void test_getAllDepartmentListApi_ReturnSuccess() throws Exception {
        Mockito.when(departmentService.getAllDepartment()).thenReturn(List.of(department));
        mvc.perform(get("/api/v1/department/all")
                .content(asJsonString(department))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data[0].id").value(ID))
                .andExpect(jsonPath("$.data[0].name").value("personnel"))
                .andExpect(jsonPath("$.data", IsCollectionWithSize.hasSize(1)));
    }
    @Test
    public void test_deleteDepartmentByIdWhenFoundApi_ReturnSuccess() throws Exception {
        Mockito.when(departmentService.deleteByIdDepartment(ID)).thenReturn(true);
        mvc.perform(delete("/api/v1/department/delete/{id}",ID)
                .content(asJsonString(department))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value(true));
    }
    @Test
    public void test_deleteDepartmentByIdWhenNotFoundApi_ReturnFalse() throws Exception {
        Mockito.when(departmentService.deleteByIdDepartment(ID)).thenReturn(true);
        mvc.perform(delete("/api/v1/department/delete/{id}","PB07")
                .content(asJsonString(department))
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
