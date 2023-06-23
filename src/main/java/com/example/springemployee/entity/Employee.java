package com.example.springemployee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employee")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee implements Serializable {
    @NotNull(message = "id not null!")
    @NotEmpty(message = "id not empty!")
    @Size(min = 4, max = 4, message = "The string must have exactly 4 characters")
    @Id
    @Column(name="id",columnDefinition="char(4)")
    private String id;
    @Column(name = "name",length = 50)
    private String name;
    @Column(name = "photo",length = 500)
    private String photo;
    @Column(name = "gender")
    private boolean gender;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthday")
    private Date birthday;
    @Column(name="salary", columnDefinition="Decimal(20,2) default '0.0'")
    private float salary;
    @Length(min = 1 , max = 10,message = "limit 10 numbers")
    @Column(name = "phone",length = 10)
    private String phone;
    @Column(name = "note",length = 250)
    private String note;
    @Column(name = "status",columnDefinition = "integer default 0" )
    private int status;
    @ManyToOne
    @JoinColumn(name="account_id")
    private Account account;
    @ManyToOne
    @JoinColumn(name="department_id")
    private Department department;
    /*dto*/
    @Transient
    private String date;
    @Transient
    private MultipartFile file;
    @Transient
    private int account_id;
    @Transient
    private String department_id;
    @OneToMany( mappedBy="id",cascade = CascadeType.PERSIST)
    private Set<Reward> rewards = new HashSet<>();

    public String validId(String id) {
        String messError = "";
        if (!id.startsWith("NV")){
            messError = "*id must start with 2 character NV";
        }
        return messError;
    }
}
