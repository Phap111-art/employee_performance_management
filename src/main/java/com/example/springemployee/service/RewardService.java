package com.example.springemployee.service;

import com.example.springemployee.entity.Reward;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RewardService {
//    RewardDTO insertOrUpdateRewardDTO(RewardDTO reward);
    Reward insertOrUpdateReward(Reward reward);
    Reward findByIdReward(int id);
    boolean deleteByIdReward(int id);
    List<Reward> getAllReward();
    List<Reward> getTop10Employee();
    Page<Reward> getDetailPageReward();
    Page<Reward> getAllDetailPageBySearchEmpSearchNameLike(int pageCurrent, int size, String search);



}
