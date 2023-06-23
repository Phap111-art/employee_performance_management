package com.example.springemployee.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {
    @NotNull(message = "id not null!")
    @NotEmpty(message = "id not empty!")
    @Size(min = 4, max = 4, message = "The Department code must have exactly 4 characters")
    private String id;
    @Length(min = 4,max = 20,message = "*Department name character range from 4 to 20")
    private String name;
    private String status;
    public String validId(String id) {
        String messError = "";
        if (!id.startsWith("PB")){
            messError = "*id must start with 2 character PB";
        }
        return messError;
    }
}
