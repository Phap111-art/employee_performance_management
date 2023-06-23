package com.example.springemployee.api;

import com.example.springemployee.entity.Reward;
import com.example.springemployee.model.ResponseObject;
import com.example.springemployee.service.EmployeeService;
import com.example.springemployee.service.RewardService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/reward")
public class RewardControllerApi {
    private final RewardService rewardService;
    private final EmployeeService employeeService;
    final Logger logger = LoggerFactory.getLogger(RewardControllerApi.class);

    public RewardControllerApi(RewardService rewardService, EmployeeService employeeService) {
        this.rewardService = rewardService;
        this.employeeService = employeeService;
    }

    @PutMapping("/insertOrUpdate")
    public ResponseEntity<ResponseObject> insertOrUpdateReward(@Valid @RequestBody Reward reward) {
        employeeService.updateStatusEmployee(reward.getEmployeeId(),1);
        if (!Objects.nonNull(reward)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject(" reward not null !", false, "")
            );
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject("insert Reward successfully!", true, rewardService.insertOrUpdateReward(reward))
        );

    }



    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findByIdReward(@PathVariable("id") int id) {
        try {
            Reward reward = rewardService.findByIdReward(id);
            if (Objects.nonNull(reward)) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("found reward id is " + id, true, reward)
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("reward id not found " + id, false, e)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("account id not found " + id, false, "")
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteByIdReward(@PathVariable("id") int id) {
        employeeService.updateStatusEmployee(rewardService.findByIdReward(id).getEmployee().getId(),0);
        boolean isDel = rewardService.deleteByIdReward(id);
        if (isDel) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    new ResponseObject("Deleting reward id " + id + " successfully", true, "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("reward id not found " + id, false, "")
        );
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllReward() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("return all reward successfully!", true, rewardService.getAllReward())
        );
    }

    @GetMapping("/detail_page")
    public ResponseEntity<ResponseObject> getRewardPageDetail() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Reward detail page !", true, rewardService.getDetailPageReward())
        );
    }
}
