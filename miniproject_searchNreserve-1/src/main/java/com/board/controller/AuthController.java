package com.board.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.board.dtos.UserDto;
import com.board.service.UserService;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String loginPage(HttpSession session) {
		UserDto ldto = (UserDto) session.getAttribute("ldto");
		if (ldto != null) {
			return "redirect:/main";
		}
		return "login";
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

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
		return "redirect:/";
	}

	@GetMapping("/signup")
	public String signUp() {
		return "user/signup";
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
}
