package com.example.springemployee.service;

import com.example.springemployee.entity.*;
import com.example.springemployee.utility.DateUtils;
import com.example.springemployee.repositories.RewardRepository;
import com.example.springemployee.service.impl.RewardServiceImpl;
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
public class RewardServiceTest {
    @Mock
    private RewardRepository rewardRepository;


    private RewardService rewardService;

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

    @BeforeEach
    void init() {
        rewardService = new RewardServiceImpl(rewardRepository);
    }

    @Test
    void test_insertOrUpdateReward_ReturnSuccess() {
        Mockito.when(rewardRepository.save(reward)).thenReturn(reward);
        Reward actualReward = rewardService.insertOrUpdateReward(reward);
        Assert.assertNotNull(actualReward);
        Assert.assertEquals(reward, actualReward);
        logger.info("data : " + actualReward);
    }


    @Test
    void test_deleteRewardById_ReturnSuccess() {
        Mockito.when(rewardRepository.existsById(ID_REWARD)).thenReturn(true);
        boolean isDel = rewardService.deleteByIdReward(ID_REWARD);
        Assert.assertEquals(isDel, true);
        logger.info("id del is : " + isDel);
    }

    @Test
    void test_deleteRewardDoseNotExist_ReturnFalse() {
        Mockito.when(rewardRepository.existsById(ID_REWARD)).thenReturn(false);
        try {
            boolean isDel = rewardService.deleteByIdReward(ID_REWARD);
            Assert.assertEquals(isDel, false);
        } catch (Exception e) {
            logger.info("" + e);
        }
    }

    @Test
    void test_findRewardById_ReturnSuccess() {
        Mockito.when(rewardRepository.findById(ID_REWARD)).thenReturn(Optional.of(reward));
        Reward actualReward = rewardService.findByIdReward(ID_REWARD);
        Assert.assertEquals(actualReward, reward);
        logger.info("" + actualReward);
    }

    @Test
    void test_accountPageDetail_ReturnSuccess() {

    }

    @Test
    void test_getAllRewardList_ReturnSuccess() {
        Mockito.when(rewardRepository.findAll()).thenReturn(List.of(reward));
        List<Reward> reward = rewardService.getAllReward();
        Assert.assertThat(reward.size(), Matchers.greaterThanOrEqualTo(1));
        logger.info("size : " + reward.size());
    }
}
