package com.example.vitabuddy.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.vitabuddy.service.LoginService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	@Autowired 
	LoginService memService;
	
	// 1. 로그인 기능
	@ResponseBody
	@RequestMapping("/intro/login")
	public String loginCheck(@RequestParam HashMap<String, Object> param, HttpSession session) {
		
		String result = memService.login(param);
		if(result.equals("success")) {
			session.setAttribute("sid", param.get("id"));
			result = "success";
		}
		return result;
	}
	
	// 2. 로그아웃 기능
	@RequestMapping("/member/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/home";
	}
	
	
	
	
	
	
	
}