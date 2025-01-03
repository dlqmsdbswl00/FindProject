package com.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.jcajce.spec.UserKeyingMaterialSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.command.UserUpdateCommand;
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
	public boolean updateUser(UserDto dto) {
		// 새로운 UserDto 객체 udto를 만들고, 기존 dto로부터 데이터를 넣음
		UserDto udto = new UserDto();
		udto.setEmail(dto.getEmail());
		udto.setName(dto.getName());
		udto.setAddress(dto.getAddress());
		udto.setBirth(dto.getBirth());
		udto.setPhone(dto.getPhone());

		// userMapper를 통해 데이터베이스 업데이트
		return userMapper.updateUser(udto); // 변경된 객체를 전달
	}

	// 현재 비밀번호가 맞는지 확인하는 메서드
	public boolean checkCurrentPassword(String email, String currentPassword) {
		UserDto udto = userMapper.findByEmail(email);
		return udto != null && udto.getPassword().equals(currentPassword);
	}

	// 비밀번호 변경 메서드
	public boolean changePassword(String email, String newPassword) {
		UserDto udto = userMapper.findByEmail(email);
		if (udto != null) {
			udto.setPassword(newPassword); // 새 비밀번호로 업데이트
			userMapper.updatePassword(udto); // 비밀번호 저장
			return true;
		}
		return false;
	}

	// 회원 탈퇴 처리
	@Transactional
	public boolean deleteUser(String email) {
		 // 관련 데이터 삭제
        int deletedExpenses = userMapper.deleteExpenseByEmail(email);

        // 사용자 정보 삭제
        int deletedUser = userMapper.deleteUser(email);

        // 삭제 여부 반환 (사용자 삭제가 성공해야만 true 반환)
        return deletedUser > 0;
	}

	public List<CalDto> userReserve(String email) {
		return userMapper.userReserve(email);
	}

	public boolean addAccount(String money, String fintech_use_num, String bank_name, int userseqno,
			String account_num_masked) {
		Map<String, Object> map = new HashMap<>();
		map.put("money", money);
		map.put("fintech_use_num", fintech_use_num);
		map.put("bank_name", bank_name);
		map.put("userseqno", userseqno);
		map.put("account_num_masked", account_num_masked);
		int count = userMapper.addAccount(map);
		return count > 0 ? true : false;
	}

	public int totalMoney(HttpServletRequest requset) {
		UserDto udto = (UserDto) requset.getSession().getAttribute("ldto");
		int userseqno = udto.getUserseqno();
		return userMapper.totalMoney(userseqno);
	}

	public List<AccountDto> getMyAccount(int userseqno) {
		System.out.println("service까지" + userseqno);
		return userMapper.getMyAccount(userseqno);
	}

	public String CheckAccount(String fintech_use_num) {
		return userMapper.CheckAccount(fintech_use_num);
	}

	public List<Map<String, Object>> dayUseMoney(String email) {
		return userMapper.dayUseMoney(email);
	}

	public List<Map<String, Object>> UpdateUseMoney(Map<String, String> map) {
		return userMapper.UpdateUseMoney(map);
	}

	public boolean saveExpense(ExpenseDto expense) {
		return userMapper.insertExpense(expense);
	}

	public ExpenseMonDto monExpense(String year, String month, String email) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("year", year);
		map.put("month", month);
		map.put("email", email);
		return userMapper.monthlyExpense(map);
	}

	public List<ExpenseDto> monthlyExpenseList(String year, String month, String email) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("year", year);
		map.put("month", month);
		map.put("email", email);
		return userMapper.monthlyExpenseList(map);
	}

}