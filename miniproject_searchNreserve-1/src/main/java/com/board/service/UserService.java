package com.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.board.command.UserUpdateCommand;
import com.board.daos.UserDao;
import com.board.dtos.AccountDto;
import com.board.dtos.CalDto;
import com.board.dtos.ExpenseDto;
import com.board.dtos.ExpenseMonDto;
import com.board.dtos.UserDto;
import com.board.mapper.UserMapper;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserDao userDao;

	public boolean addUser(UserDto dto) {
		int count = userMapper.addUser(dto);
		return count > 0 ? true : false;
	}

	public UserDto loginUser(UserDto dto) {
		return userMapper.loginUser(dto);
	}

	public UserDto userInfo(HttpServletRequest request) {
		UserDto udto = (UserDto) request.getSession().getAttribute("ldto");
		String email = udto.getEmail();
		System.out.println(email);
		return userMapper.userInfo(email);
	}

	// 수정하기
	public boolean updateUser(UserUpdateCommand userUpdateCommand) {
		UserDto dto = new UserDto();
		dto.setEmail(userUpdateCommand.getEmail());
		dto.setName(userUpdateCommand.getName());
		dto.setAddress(userUpdateCommand.getAddress());
		dto.setBirth(userUpdateCommand.getBirth());
		dto.setPhone(userUpdateCommand.getPhone());
		return userMapper.updateUser(dto);
	}

	public List<CalDto> userReserve(String email) {
		return userMapper.userReserve(email);
	}

	public boolean checkPassword(String email, String password) {
		UserDto dto = userDao.getUserByEmail(email);
		return dto.getPassword().equals(password); // 비밀번호 확인
	}

	public void updatePassword(String email, String newPassword) {
		userDao.updatePassword(email, newPassword); // 비밀번호 업데이트
	}

	public void deleteAccount(String email) {
		userDao.deleteUser(email); // DB에서 사용자 삭제
	}

}