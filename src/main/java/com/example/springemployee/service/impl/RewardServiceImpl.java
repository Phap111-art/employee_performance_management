package com.example.springemployee.service.impl;

import com.example.springemployee.entity.Reward;
import com.example.springemployee.exception.DataNotFound;

import com.example.springemployee.utility.DateUtils;
import com.example.springemployee.repositories.EmployeeRepository;
import com.example.springemployee.repositories.RewardRepository;
import com.example.springemployee.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RewardServiceImpl implements RewardService {
    @Autowired
    private EmployeeRepository employeeRepository;

    private final RewardRepository rewardRepository;

    public RewardServiceImpl(RewardRepository rewardRepository) {
        this.rewardRepository = rewardRepository;
    }


    @Override
    public Reward insertOrUpdateReward(Reward reward) {
        List<Reward> rewards = rewardRepository.findByRewardByEmployeeId(reward.getEmployeeId());
        if (!rewards.isEmpty()) { //update
            return  rewardRepository.findById(rewards.get(0).getId()).map(data -> {
                data.setEmployee(employeeRepository.findById(reward.getEmployeeId()).orElseThrow(DataNotFound::new));
                if (reward.isType()) {
                    data.setAchievement((short) (data.getAchievement() + (short) 1));
                } else {
                    data.setDiscipline((short) (data.getDiscipline() + (short) 1));
                }
                data.setReason(reward.getReason());
                return rewardRepository.save(data);
            }).orElseThrow(DataNotFound::new);
        }
        /*add*/
        reward.setEmployee(employeeRepository.findById(reward.getEmployeeId()).orElseThrow(DataNotFound::new));
        if (reward.isType()) {
            reward.setAchievement((short) 1);
        } else {
            reward.setDiscipline((short) 1);
        }
        reward.setRecordDate(DateUtils.getInstance().getCurrentDate());
        reward.setReason(reward.getReason());
        return rewardRepository.save(reward);
    }


    @Override
    public Reward findByIdReward(int id) {
        return rewardRepository.findById(id).orElseGet(() -> {
            throw new DataNotFound("id " + id + " not found in data");
        });
    }

    @Override
    public boolean deleteByIdReward(int id) {
        boolean isDel = rewardRepository.existsById(id);
        if (isDel) {
            rewardRepository.deleteById(id);
            return true;
        }
        throw new IllegalArgumentException("id " + id + " do not exist!");
    }

    @Override
    public List<Reward> getAllReward() {
        return rewardRepository.findAll();
    }

    @Override
    public List<Reward> getTop10Employee() {
        return rewardRepository.showTop10Employee();
    }

    @Override
    public Page<Reward> getDetailPageReward() {
        Pageable pageable = PageRequest.of(0, 5);
        return rewardRepository.findAll(pageable);
    }
    @Override
    public Page<Reward> getAllDetailPageBySearchEmpSearchNameLike(int pageCurrent, int size, String search) {
        Pageable pageable = PageRequest.of(pageCurrent-1,size);
        return rewardRepository.findByRewardByEmployeeSearchNameLike(search + "%",pageable);
    }

}
