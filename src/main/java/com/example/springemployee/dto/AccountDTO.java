package com.example.springemployee.dto;

import com.example.springemployee.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AccountDTO {
    private int id;
    @Pattern(regexp = "^[a-zA-Z0-9_.-]*$", message = "Username cannot contain special characters")
    private String username;
    //    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[A-Z])(?=.*[-\\#\\$\\.\\%\\&\\*])(?=.*[a-zA-Z]).{8,16}$", message =
//                    "*At least 8 - 16 characters,\n" +
//                    "*must contain at least 1 uppercase letter,\n" +
//                    "*must contain at least 1 lowercase letter,\n" +
//                    "*and 1 number\n" +
//                    "*Can contain any of this special characters $ % # * & - .")
    private String password;
    @Length(min = 0, max = 50 , message = "FullName must not exceed 50 characters")
    private String fullName;
    @Email
    @NotEmpty(message = "email is not empty")
    private String email;
    private int status;
    private boolean isActive;
    private Set<Integer> roleId = new HashSet<>();
}
