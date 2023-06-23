package com.example.springemployee.mapper;

import com.example.springemployee.dto.AccountDTO;
import com.example.springemployee.dto.DepartmentDTO;
import com.example.springemployee.entity.Account;
import com.example.springemployee.entity.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMp extends EntityMapper<Department, DepartmentDTO> {

}
