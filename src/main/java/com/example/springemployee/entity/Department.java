package com.example.springemployee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "department")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @Column(name="id",columnDefinition="char(4)")
    private String id;
    @Column(name = "name",length = 50)
    private String name;
    @Column(name = "status",columnDefinition = "integer default 0" )
    private int status;
    @OneToMany(mappedBy="id",cascade = CascadeType.PERSIST)
    private Set<Employee> employees = new HashSet<>();

}
