package com.example.vitabuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	// 1. 인트로 화면 (로그인화면)
	@RequestMapping("/intro") // intro 추가
	public String intro() {
		return "intro";
	}
	
	// 2. 메인홈페이지.
	@RequestMapping("/home")
	public String home() {
		return "home";
	}
	
    // 4. 상점 페이지 추가
    @RequestMapping("/supplementList")
    public String supplementList() {
        return "supplement/supplementList";  // supplementList.jsp로 연결
    }
    
    @RequestMapping("/wish")
	public String wish() {
		return "member/wishlistView";
	}
}