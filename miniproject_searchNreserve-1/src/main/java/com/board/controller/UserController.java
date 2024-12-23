package com.board.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.board.command.UserUpdateCommand;
import com.board.dtos.CalDto;
import com.board.dtos.ExpenseDto;
import com.board.dtos.ExpenseMonDto;
import com.board.dtos.UserDto;
import com.board.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	// 메인 페이지
	@GetMapping("/main")
	public String mainPage(HttpSession session, Model model) {
		UserDto ldto = (UserDto) session.getAttribute("ldto");
		if (ldto == null) {
			// 로그인되지 않은 사용자는 로그인 화면으로 리다이렉트
			return "redirect:/";
		}
		model.addAttribute("userName", ldto.getName());

		// 메인 페이지에 표시할 데이터 추가 (예제 데이터)
		model.addAttribute("accountBalance", "10,000,000 원");
		model.addAttribute("monthlyExpense", "180,000 원");
		model.addAttribute("mainSpending", "식비 40%");
		model.addAttribute("habitSaving", "1,000 원");

		return "main"; // 메인 화면 반환
	}


	// 마이페이지
	@GetMapping("/user/mypage")
	public String myPage() {
		return "user/mypage"; // mypage.html 템플릿 반환
	}

	// 유저 정보 수정 페이지 이동
	@GetMapping("/userInfo")
	public String userInfoPage(Model model, HttpServletRequest request) {
		UserDto dto = userService.userInfo(request);
		model.addAttribute("dto", dto);
		return "user/userInfo"; // userInfo.html 템플릿 반환
	}

	// 유저 정보 수정 처리
	@PostMapping("/userUpdate")
	public String updateUser(@Validated UserUpdateCommand userUpdateCommand, BindingResult result) {
		if (result.hasErrors()) {
			System.out.println("수정 내용이 잘못되었습니다.");
			return "user/userInfo";
		}
		userService.updateUser(userUpdateCommand); // DB 업데이트
		return "redirect:/userInfo"; // 수정 후 다시 사용자 정보 페이지로 이동
	}

	// 비밀번호 변경 페이지 이동
	@GetMapping("/changePassword")
	public String passwordChangePage() {
		return "user/changePassword"; // 비밀번호 변경 페이지 반환
	}

	// 비밀번호 변경 처리
	@PostMapping("/updatePassword")
	public String updatePassword(String oldPassword, String newPassword, HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		UserDto dto = (UserDto) session.getAttribute("ldto");

		boolean isPasswordCorrect = userService.checkPassword(dto.getEmail(), oldPassword);
		if (!isPasswordCorrect) {
			model.addAttribute("errorMessage", "현재 비밀번호가 맞지 않습니다.");
			return "user/changePassword"; // 비밀번호 오류 시 다시 변경 페이지로
		}

		// 비밀번호 변경
		userService.updatePassword(dto.getEmail(), newPassword);
		return "redirect:/userInfo"; // 비밀번호 변경 후 사용자 정보 페이지로 이동
	}

	// 회원 탈퇴 처리
	@GetMapping("/deleteAccount")
	public String deleteAccount(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserDto dto = (UserDto) session.getAttribute("ldto");

		userService.deleteAccount(dto.getEmail()); // 계정 삭제
		session.invalidate(); // 세션 무효화
		return "redirect:/"; // 탈퇴 후 로그인 화면으로 이동
	}

//	@GetMapping("/useraccount")
//	public String userAccount() {
//		return "user/useraccount";
//	}
//
//	@GetMapping("/userUseMoney")
//	public String userUseMoney() {
//		return "user/userUseMoney";
//	}



}
