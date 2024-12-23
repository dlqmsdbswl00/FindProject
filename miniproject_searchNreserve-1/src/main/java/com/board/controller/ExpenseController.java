package com.board.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.dtos.ExpenseDto;
import com.board.dtos.ExpenseMonDto;
import com.board.dtos.UserDto;
import com.board.service.ExpenseService;
import com.board.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class ExpenseController {
	@Autowired
	private ExpenseService expenseService;
	
	@GetMapping("/savemoney")
	public String savemoney(String year, String month, Model model, HttpServletRequest request) {

		if (year == null || month == null) {
			Calendar cal = Calendar.getInstance();
			year = cal.get(Calendar.YEAR) + "";
			month = cal.get(Calendar.MONTH) + 1 + "";
		}
		HttpSession session = request.getSession();
		UserDto uDto = (UserDto) session.getAttribute("ldto");
		ExpenseMonDto dto = expenseService.monExpense(year, month, uDto.getEmail());
		model.addAttribute("dto", dto);
		return "savemoney"; // savemoney.html 템플릿으로 이동
	}

	@GetMapping("/savemoneycsr")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> savemoneyCSR(String year, String month, HttpServletRequest request) {
		System.out.println("year:" + year + "month:" + month);
		HttpSession session = request.getSession();
		UserDto uDto = (UserDto) session.getAttribute("ldto");

		ExpenseMonDto dto = expenseService.monExpense(year, month, uDto.getEmail());
		List<ExpenseDto> list = expenseService.monthlyExpenseList(year, month, uDto.getEmail());

		System.out.println(list.size());
		System.out.println(dto);

		Map<String, Object> map = new HashMap<>();
		map.put("expenseList", list);
		map.put("dto", dto);

		return ResponseEntity.ok(map); // savemoney.html 템플릿으로 이동
	}

//   date: dateKey,
//   expenseType: expenseType
	@PostMapping("/api/expenses")
	@ResponseBody
	public ResponseEntity<String> saveExpenses(@RequestBody ExpenseDto expenses, HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserDto uDto = (UserDto) session.getAttribute("ldto");
		expenses.setEmail(uDto.getEmail());
		System.out.println("지출 ㅋㅋㅋ" + expenses);
		expenseService.saveExpense(expenses);
		return ResponseEntity.ok("등록완료");// 성공을 의미하는 200코드 반환
	}
}
