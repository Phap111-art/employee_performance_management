package com.example.springemployee.repositories;

import com.example.springemployee.entity.Reward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward,Integer> {
    @Query("SELECT r FROM Reward r WHERE  r.employee.id  =  :id")
    List<Reward> findByRewardByEmployeeId(@Param("id") String id);
    @Query("SELECT r FROM Reward r WHERE  r.employee.name  like :name")
    Page<Reward> findByRewardByEmployeeSearchNameLike(@Param("name") String name, Pageable pageable);
    @Query("SELECT r FROM Reward r ORDER BY (r.achievement - r.discipline) DESC LIMIT 10")
    List<Reward> showTop10Employee();

}
