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
	
	// 3. 상품상세페이지
	@RequestMapping("/prdDetail")
	public String prdDetail() {
		return"supplement/productDetailView";
	}
	
}
