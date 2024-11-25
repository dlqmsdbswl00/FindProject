package com.hk.find.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ControllerAdvice;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalControllerAdvice {
	 @ModelAttribute("userName")
	    public String addUserNameToModel(HttpSession session) {
	        // 세션에서 사용자 이름 가져오기
	        String userName = (String) session.getAttribute("userName");
	        return userName != null ? userName : "게스트"; // null일 경우 "게스트"로 표시
	    }
}
