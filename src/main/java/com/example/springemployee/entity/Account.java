package com.example.springemployee.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Scope;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "account")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "username", length = 50)
    private String username;
    @Column(name = "password", length = 250)
    private String password;
    @Column(name = "fullName", length = 50)
    private String fullName;
    @Column(name = "email", length = 50)
    private String email;
    @Column(name = "status",columnDefinition = "integer default 0" )
    private int status;
    @Column(name = "photo", length = 500)
    private String photo;
    @Column(name = "is_active")
    private boolean isActive;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @Transient
    private Set<Integer> roleId = new HashSet<>();
    @OneToMany(mappedBy = "id",cascade = CascadeType.PERSIST)
    private Set<Employee> employees = new HashSet<>();


}