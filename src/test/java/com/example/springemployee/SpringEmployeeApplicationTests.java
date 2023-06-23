package com.example.springemployee;

import com.example.springemployee.entity.*;
import com.example.springemployee.utility.DateUtils;
import com.example.springemployee.repositories.EmployeeRepository;
import com.example.springemployee.repositories.RewardRepository;
import com.example.springemployee.service.AccountService;
import com.example.springemployee.service.EmployeeServiceTest;
import com.example.springemployee.service.RewardService;
import com.example.springemployee.service.StorageService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class SpringEmployeeApplicationTests {
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
    @Autowired
    private AccountService accountService;
    @Autowired
    private RewardRepository rewardRepository;
    @Autowired
    private RewardService rewardService;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private StorageService storageService;
    @Test
    void contextLoads() {



    }
    private static <T> List<T> findDifference(List<T> first, List<T> second)
    {
        List<T> diff = new ArrayList<>(first);
        diff.removeAll(second);
        return diff;
    }
}
