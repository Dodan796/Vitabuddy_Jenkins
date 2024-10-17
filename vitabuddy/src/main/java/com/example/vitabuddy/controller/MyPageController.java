package com.example.vitabuddy.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.vitabuddy.model.ReviewVO;
import com.example.vitabuddy.service.ReviewService;

@Controller
@RequestMapping("/myPage")
public class MyPageController {

    @Autowired
    private ReviewService reviewService;

    // 마이페이지로 이동 및 리뷰 목록 조회
    @GetMapping("")
    public String myPage(Model model, HttpSession session) {
        // 세션에서 사용자 ID를 가져옴
        String userId = (String) session.getAttribute("userId"); 
        System.out.println("UserId from session: " + userId);  // 로그 출력

        // userId가 null인지 확인
        if (userId == null) {
            System.out.println("User is not logged in.");
            return "redirect:/intro";  // 로그인 페이지로 리다이렉트
        }

        // 리뷰 목록을 가져오고 모델에 추가
        List<ReviewVO> userReviews = reviewService.getUserReviews(userId);
        model.addAttribute("reviews", userReviews);

        return "member/myPage";  // myPage.jsp로 이동
    }
}
