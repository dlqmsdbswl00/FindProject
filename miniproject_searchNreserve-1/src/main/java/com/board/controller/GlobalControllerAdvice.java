package com.board.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.board.dtos.UserDto;
@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("userName")
    public String addUserNameToModel(HttpSession session) {
        UserDto ldto = (UserDto) session.getAttribute("ldto");
        return (ldto != null) ? ldto.getName() : "게스트"; // 로그인된 사용자 이름 반환, 없으면 "게스트"
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
}
