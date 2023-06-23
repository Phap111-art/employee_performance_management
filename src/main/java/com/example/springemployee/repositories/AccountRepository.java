package com.example.springemployee.repositories;

import com.example.springemployee.entity.Account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
    List<Account> findAllByUsername(String username);
    List<Account> findAllByEmail(String email);
    @Query("SELECT a FROM Account a WHERE  a.username  = :username")
    Account findByUsernameAccount(@Param("username") String username);
    Page<Account> findByUsernameLike(String username, Pageable pageable);
    Account findByEmail(String email);
}
