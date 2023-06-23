package com.example.springemployee.api;

import com.example.springemployee.entity.*;
import com.example.springemployee.utility.DateUtils;
import com.example.springemployee.service.EmployeeServiceTest;
import com.example.springemployee.service.RewardService;
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
public class RewardApiControllerTest {
    @MockBean
    private RewardService rewardService;

    @Autowired
    private MockMvc mvc;

    ObjectMapper om = new ObjectMapper();


    private static final String ID_EMPLOYEE = "NV01";
    private static final String ID_DEPARTMENT = "PB01";
    private static final int ID_ACCOUNT = 1;
    private static final int ID_ROLE = 1;
    private static final int ID_REWARD = 1;

    private final Logger logger = LoggerFactory.getLogger(EmployeeServiceTest.class);

    private static final DateUtils CONFIG_DATE = DateUtils.getInstance();
    private static final Role role = Role.builder().id(ID_ROLE).name("USER").build();
    private static final Account account = Account.builder().id(ID_ACCOUNT).username("Mandy123456").password("123456").roles(Set.of(role)).build();
    private static final Department department = Department.builder().id(ID_DEPARTMENT).name("personnel").build();
    private static final Employee employee = Employee.builder().id(ID_EMPLOYEE).name("Mr. Mandy").birthday(CONFIG_DATE.getStringToDate("19/11/2000"))
            .account(account).department(department).build();
    private static final Reward reward = Reward.builder().id(ID_REWARD).employee(employee).achievement((short) 1).discipline((short) 0)
            .recordDate(CONFIG_DATE.getCurrentDate()).reason("asd").build();



    @org.junit.Test
    public void test_insertRewardWhenEmployeeExistApi_ReturnSuccess() throws Exception {
        Mockito.when(rewardService.insertOrUpdateReward(reward)).thenReturn(reward);
        mvc.perform(put("/api/v1/reward/insertOrUpdate")
                .content(asJsonString(reward))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.employee.id").value(ID_EMPLOYEE));

    }

    @org.junit.Test
    public void test_WhenIdExistedApi_ReturnSuccess() throws Exception {
        Mockito.when(rewardService.findByIdReward(ID_REWARD)).thenReturn(reward);
        mvc.perform(get("/api/v1/reward/{id}", ID_REWARD)
                .content(asJsonString(reward))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.employee.id").value(ID_EMPLOYEE));
    }

    @Test
    public void test_WhenIdDoseNotExistedApi_ReturnFalse() throws Exception {
        Mockito.when(rewardService.findByIdReward(ID_REWARD)).thenReturn(reward);
        mvc.perform(get("/api/v1/reward/{id}",8)
                .content(asJsonString(reward))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false));
    }

    @org.junit.Test
    public void test_getAllRewardListApi_ReturnSuccess() throws Exception {
        Mockito.when(rewardService.getAllReward()).thenReturn(List.of(reward));
        mvc.perform(get("/api/v1/reward/all")
                .content(asJsonString(reward))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data[0].id").value(ID_REWARD))
                .andExpect(jsonPath("$.data[0].employee.id").value(ID_EMPLOYEE))
                .andExpect(jsonPath("$.data", IsCollectionWithSize.hasSize(1)));
    }

    @org.junit.Test
    public void test_deleteRewardByIdWhenFoundApi_ReturnSuccess() throws Exception {
        Mockito.when(rewardService.deleteByIdReward(ID_REWARD)).thenReturn(true);
        mvc.perform(delete("/api/v1/reward/delete/{id}", ID_REWARD)
                .content(asJsonString(reward))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    public void test_deleteRewardByIdWhenNotFoundApi_ReturnFalse() throws Exception {
        Mockito.when(rewardService.deleteByIdReward(ID_REWARD)).thenReturn(true);
        mvc.perform(delete("/api/v1/reward/delete/{id}", 7)
                .content(asJsonString(reward))
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
