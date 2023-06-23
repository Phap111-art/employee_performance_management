package com.example.springemployee.mapper;

import com.example.springemployee.dto.AccountDTO;
import com.example.springemployee.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMp extends EntityMapper<Account, AccountDTO> {

}
