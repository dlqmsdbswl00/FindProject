package com.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.board.dtos.AccountDto;
import com.board.dtos.CalDto;
import com.board.dtos.ExpenseDto;
import com.board.dtos.ExpenseMonDto;
import com.board.dtos.UserDto;

@Mapper
public interface UserMapper {
	public int addUser(UserDto dto);

	public UserDto loginUser(UserDto dto);

	public UserDto userInfo(String email);

	// 정보 수정
	public boolean updateUser(UserDto dto);

	public List<CalDto> userReserve(String email);

}