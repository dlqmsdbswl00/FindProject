package com.hk.find.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        // 사용자 정보를 모델에 추가 (임시 데이터)
        model.addAttribute("userName", "아무개");
        model.addAttribute("accountBalance", "10,000,000 원");
        model.addAttribute("monthlyExpense", "180,000 원");
        model.addAttribute("mainSpending", "식비 40%");
        model.addAttribute("habitSaving", "1,000 원");

        return "home"; // home.html 템플릿으로 이동
    }
}