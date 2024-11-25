package com.hk.find.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    // 로그인 페이지
    @GetMapping("/User/login")
    public String loginPage() {
        return "User/login"; // login.html 템플릿 반환
    }

    // 회원가입 페이지
    @GetMapping("/User/register")
    public String registerPage() {
        return "User/register"; // register.html 템플릿 반환
    }

    // 회원가입 처리
    @PostMapping("/User/register")
    public String registerUser(Model model) {
        // TODO: 회원가입 처리 로직 (DB 저장 등)
        model.addAttribute("message", "회원가입 성공!");
        return "redirect:/User/login"; // 회원가입 후 로그인 페이지로 리다이렉트
    }

    // 마이페이지
    @GetMapping("/User/mypage")
    public String myPage(Model model /*, @AuthenticationPrincipal CustomUserDetails userDetails */) {
        // TODO: 로그인한 사용자 정보 가져오는 로직
        // 임시 데이터로 사용자 이름 전달
        model.addAttribute("userName", "가나다");
        return "User/mypage"; // mypage.html 템플릿 반환
    }
}
