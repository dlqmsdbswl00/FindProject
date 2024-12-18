package com.board.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.board.command.UserUpdateCommand;
import com.board.dtos.CalDto;
import com.board.dtos.UserDto;
import com.board.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
//@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	// 루트 URL 접근 시 로그인 화면으로 이동
	@GetMapping("/")
	public String loginPage(HttpSession session) {
		UserDto ldto = (UserDto) session.getAttribute("ldto");
		if (ldto != null) {
			// 이미 로그인된 사용자라면 메인 페이지로 리다이렉트
			return "redirect:/main";
		}
		return "login"; // 로그인 화면 반환
	}

	// 로그인 처리
	@GetMapping("/user/login")
	public String loginPage() {
		return "login"; // 로그인 페이지 반환
	}

	@PostMapping("/user/login")
	public String login(UserDto dto, HttpServletRequest request, Model model) {
		UserDto ldto = userService.loginUser(dto);

		if (ldto == null) {
			model.addAttribute("errorMessage", "이메일 또는 비밀번호가 올바르지 않습니다.");
			return "login"; // 로그인 실패 시 로그인 화면으로 이동
		}

		HttpSession session = request.getSession();
		session.setAttribute("ldto", ldto);
		session.setMaxInactiveInterval(60 * 10); // 세션 유지 시간 10분

		return "redirect:/main"; // 로그인 성공 시 메인 페이지로 이동
	}

	// 로그아웃 처리
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate(); // 세션 초기화
		return "redirect:/"; // 로그아웃 후 로그인 화면으로 이동
	}

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

	// 회원가입 화면 이동
	@GetMapping("/signup")
	public String signUp() {
		return "user/signup"; // 회원가입 화면 반환
	}

	// 회원가입 처리
	@PostMapping("/addUser")
	public String addUser(UserDto dto) {
		System.out.println(dto);
		boolean isS = userService.addUser(dto);
		if (isS) {
			System.out.println("회원가입 성공");
			return "redirect:/"; // 회원가입 성공 후 로그인 화면으로 이동
		}
		System.out.println("회원가입 실패");
		return "error"; // 회원가입 실패 시 에러 페이지
	}

	// 사용자 인증 후 토큰 발급 처리
	@GetMapping("/authresult")
	public String authResult(String code, Model model) throws IOException, ParseException {
		HttpURLConnection conn;
		JSONObject result;

		// 인증 요청 URL
		URL url = new URL("https://testapi.openbanking.or.kr/oauth/2.0/token?" + "code=" + code
				+ "&client_id=4987e938-f84b-4e23-b0a2-3b15b00f4ffd"
				+ "&client_secret=3ff7570f-fdfb-4f9e-8f5a-7b549bf2adec"
				+ "&redirect_uri=http://localhost:8087/authresult" + "&grant_type=authorization_code");

		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setDoOutput(true);

		// 응답 코드 확인
		int responseCode = conn.getResponseCode();
		System.out.println("응답 코드: " + responseCode); // 응답 코드 출력

		// 정상 응답이 아닐 경우 에러 응답 확인
		BufferedReader br;
		if (responseCode != 200) {
			br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
			StringBuilder errorResponse = new StringBuilder();
			String errorLine;
			while ((errorLine = br.readLine()) != null) {
				errorResponse.append(errorLine.trim());
			}
			System.out.println("에러 응답 내용: " + errorResponse.toString()); // 에러 응답 출력

			// 에러 응답 JSON 파싱 후 코드와 메시지 출력
			JSONObject errorResult = (JSONObject) new JSONParser().parse(errorResponse.toString());
			System.out.println("에러 코드: " + errorResult.get("rsp_code"));
			System.out.println("에러 메시지: " + errorResult.get("rsp_message"));
		} else {
			// 정상 응답 처리
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			StringBuilder response = new StringBuilder();
			String responseLine;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			System.out.println("응답 내용: " + response.toString()); // 성공 응답 출력

			result = (JSONObject) new JSONParser().parse(response.toString());
			model.addAttribute("access_token", result.get("access_token").toString());
			model.addAttribute("refresh_token", result.get("refresh_token").toString());
			model.addAttribute("user_seq_no", result.get("user_seq_no").toString());
		}

		return "user/authresult";
	}

	// 유저 정보 수정 페이지 이동
	@GetMapping("/userInfo")
	public String userInfoPage(Model model, HttpServletRequest request) {
		UserDto dto = userService.userInfo(request);
		model.addAttribute("dto", dto);
		return "user/userInfo";
	}

	// 유저 정보 수정 처리
	@PostMapping("/userUpdate")
	public String updateUser(@Validated UserUpdateCommand userUpdateCommand, BindingResult result) {
		if (result.hasErrors()) {
			System.out.println("수정 내용이 잘못되었습니다.");
			return "user/userInfo";
		}
		userService.updateUser(userUpdateCommand);
		return "redirect:/userInfo";
	}

	// 기타 기능
	@GetMapping("/usermain")
	public String userMain(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String email = ((UserDto) session.getAttribute("ldto")).getEmail();

		List<CalDto> list = userService.userReserve(email);
		model.addAttribute("list", list);

		return "user/usermain";
	}

	@GetMapping("/useraccount")
	public String userAccount() {
		return "user/useraccount";
	}

	@GetMapping("/userUseMoney")
	public String userUseMoney() {
		return "user/userUseMoney";
	}

	@GetMapping("/account")
	public String account() {
		return "account"; // account.html 템플릿으로 이동
	}

	@GetMapping("/calmoney")
	public String calmoney() {
		return "calmoney"; // calmoney.html 템플릿으로 이동
	}

	@GetMapping("/analysis")
	public String analysis() {
		return "analysis"; // analysis.html 템플릿으로 이동
	}

	@GetMapping("/savemoney")
	public String savemoney() {
		return "savemoney"; // savemoney.html 템플릿으로 이동
	}
}
