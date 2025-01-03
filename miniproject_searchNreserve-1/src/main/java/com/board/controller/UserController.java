package com.board.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
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

	// 회원 탈퇴 처리
	@PostMapping("/deleteUser")
	public String deleteUser(HttpServletRequest request) {
		// 세션에서 로그인한 사용자 정보 가져오기
		UserDto userDto = (UserDto) request.getSession().getAttribute("ldto");

		if (userDto != null) {
			String userName = userDto.getEmail(); // 또는 getName()을 사용하여 이름을 가져올 수 있습니다.
			boolean isDeleted = userService.deleteUser(userName); // 서비스에서 회원 탈퇴 처리

			if (isDeleted) {
				return "redirect:/logout"; // 로그아웃 후 리다이렉트
			} else {
				// 탈퇴 실패 처리 (예: 실패 메시지 추가)
				return "error"; // 탈퇴 실패 시 에러 페이지로 이동
			}
		} else {
			// 로그인하지 않은 경우 처리
			return "redirect:/login"; // 로그인 페이지로 리다이렉트
		}
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

	// 마이페이지
	@GetMapping("/user/mypage")
	public String myPage() {
		return "user/mypage"; // mypage.html 템플릿 반환
	}

	// 유저 정보 수정 페이지 이동
	@GetMapping("/userInfo")
	public String userInfoPage(Model model, HttpServletRequest request) {
		// 세션에서 UserDto 가져오기
		UserDto dto = (UserDto) request.getSession().getAttribute("ldto");

		if (dto == null) {
			// 사용자가 로그인하지 않았다면 로그인 페이지로 리다이렉트
			return "redirect:/login";
		}

		// 정보 수정 페이지에 UserDto 전달
		model.addAttribute("dto", dto);
		return "user/userInfo";
	}

	// 유저 정보 수정 처리
	@PostMapping("/updateUser")
	public String updateUser(UserUpdateCommand userUpdateCommand, HttpServletRequest request, Model model) {
		// 세션에서 UserDto 가져오기
		UserDto user = (UserDto) request.getSession().getAttribute("ldto");

		if (user == null) {
			// 사용자가 로그인하지 않았다면 로그인 페이지로 리다이렉트
			return "redirect:/login";
		}

		// UserDto에 사용자 수정 정보 반영
		user.setName(userUpdateCommand.getName());
		user.setAddress(userUpdateCommand.getAddress());
		user.setPhone(userUpdateCommand.getPhone());
		user.setEmail(userUpdateCommand.getEmail()); // 이메일도 수정 반영
		user.setBirth(userUpdateCommand.getBirth()); // 생일 수정 반영

		// 생일 값이 null이 아니면 설정, 아니면 null 처리
		if (userUpdateCommand.getBirth() != null && !userUpdateCommand.getBirth().isEmpty()) {
			user.setBirth(userUpdateCommand.getBirth());
		} else {
			user.setBirth(null); // 생일이 비어 있으면 null 처리
		}

		// 서비스 호출하여 데이터베이스 업데이트
		boolean isUpdated = userService.updateUser(user);

		if (isUpdated) {
			// 수정 성공 시, 세션 정보도 갱신
			request.getSession().setAttribute("ldto", user);

			// 성공 메시지를 세션에 저장
			request.getSession().setAttribute("updateMessage", "정보가 성공적으로 수정되었습니다.");

			return "redirect:/userInfo"; // 수정된 정보로 다시 이동
		} else {
			// 수정 실패 시 에러 메시지 출력
			model.addAttribute("error", "수정에 실패했습니다.");
			return "error"; // 에러 페이지로 이동
		}
	}

	@GetMapping("/changepassword")
	public String changePasswordPage() {
		return "user/changePassword"; // 비밀번호 변경 화면으로 이동
	}

	@PostMapping("/changepassword")
	public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword,
			@RequestParam String confirmPassword, HttpServletRequest request, Model model) {

		HttpSession session = request.getSession();
		UserDto uDto = (UserDto) session.getAttribute("ldto");

		// 유효성 검사
		if (uDto == null) {
			model.addAttribute("error", "로그인 정보가 없습니다.");
			return "redirect:/login"; // 로그인 화면으로 리다이렉트
		}

		// 비밀번호 확인
		if (!newPassword.equals(confirmPassword)) {
			model.addAttribute("error", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
			return "user/changePassword"; // 비밀번호 변경 화면으로 돌아가기
		}

		// 현재 비밀번호가 맞는지 확인
		boolean isCurrentPasswordValid = userService.checkCurrentPassword(uDto.getEmail(), currentPassword);

		if (!isCurrentPasswordValid) {
			model.addAttribute("error", "현재 비밀번호가 잘못되었습니다.");
			return "user/changePassword"; // 비밀번호 변경 화면으로 돌아가기
		}

		// 새 비밀번호로 변경
		boolean isPasswordChanged = userService.changePassword(uDto.getEmail(), newPassword);

		if (isPasswordChanged) {
			model.addAttribute("success", "비밀번호가 성공적으로 변경되었습니다.");
			return "redirect:/mypage"; // 마이페이지로 리다이렉트 (사용자 경험을 개선)
		} else {
			model.addAttribute("error", "비밀번호 변경에 실패했습니다.");
			return "user/changePassword"; // 비밀번호 변경 화면으로 돌아가기
		}
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
	public String savemoney(String year, String month, Model model, HttpServletRequest request) {

		if (year == null || month == null) {
			Calendar cal = Calendar.getInstance();
			year = cal.get(Calendar.YEAR) + "";
			month = cal.get(Calendar.MONTH) + 1 + "";
		}
		HttpSession session = request.getSession();
		UserDto uDto = (UserDto) session.getAttribute("ldto");
		ExpenseMonDto dto = userService.monExpense(year, month, uDto.getEmail());
		// 만약 데이터가 없으면 기본값 설정
		if (dto == null) {
			dto = new ExpenseMonDto();
			dto.setCoffeeTotal(0);
			dto.setCigaretteTotal(0);
			dto.setTaxiTotal(0);
			dto.setAmountTotal(0);
		}
		model.addAttribute("dto", dto);
		return "savemoney"; // savemoney.html 템플릿으로 이동
	}

	@GetMapping("/savemoneycsr")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> savemoneyCSR(String year, String month, HttpServletRequest request) {
		System.out.println("year:" + year + "month:" + month);
		HttpSession session = request.getSession();
		UserDto uDto = (UserDto) session.getAttribute("ldto");

		ExpenseMonDto dto = userService.monExpense(year, month, uDto.getEmail());
		List<ExpenseDto> list = userService.monthlyExpenseList(year, month, uDto.getEmail());

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
		userService.saveExpense(expenses);
		return ResponseEntity.ok("등록완료");// 성공을 의미하는 200코드 반환
	}

}