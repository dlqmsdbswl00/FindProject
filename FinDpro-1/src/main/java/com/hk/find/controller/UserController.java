package com.hk.find.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.hk.find.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    // 로그인 페이지
    @GetMapping("/User/login")
    public String loginPage() {
        return "User/login"; // login.html 템플릿 반환
    }

//    // 로그인 성공 시 세션 설정
//    @PostMapping("/User/login")
//    public String login(String id, HttpSession session) {
//        // 로그인 로직
//        String userName = UserService.getUserNameById(id); // 사용자 이름 가져오기
//        session.setAttribute("userName", userName); // 세션에 저장
//        return "redirect:/";
//    }

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
    public String myPage() {
        return "User/mypage"; // mypage.html 템플릿 반환
    }
    
    // 로그아웃
    @GetMapping("/User/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 초기화
        return "redirect:/";
    }

}
