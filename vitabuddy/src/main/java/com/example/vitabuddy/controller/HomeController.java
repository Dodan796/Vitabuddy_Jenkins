package com.example.vitabuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;
import java.util.List;

import com.example.vitabuddy.model.SupplementStoreVO;
import com.example.vitabuddy.service.IReviewService;

@Controller
public class HomeController {

    // 1. 인트로 화면 (로그인 화면)
    @RequestMapping("/intro")
    public String intro() {
        return "intro";
    }

    // 2. 메인 홈페이지
    @RequestMapping("/home")
    public String home(Model model) {
        // 브랜드별 상위 1개의 상품을 가져옴
        List<SupplementStoreVO> topSupplementsByBrand = reviewService.getTopSupplementsByBrand();

        for (SupplementStoreVO supplement : topSupplementsByBrand) {
            if (supplement.getSupImg() != null) {
                // 이미지 데이터를 Base64로 인코딩
                String base64Image = Base64.getEncoder().encodeToString(supplement.getSupImg());
                supplement.setBase64SupImg(base64Image);
            } else {
                // 이미지가 없을 경우 로그로 확인
                System.out.println("No image found for SupID " + supplement.getSupId());
            }
        }

        // 모델에 담아 JSP로 전달
        model.addAttribute("topSupplements", topSupplementsByBrand);
        return "home";
    }

    // 4. 상점 페이지 추가
    @RequestMapping("/supplementList")
    public String supplementList() {
        return "supplement/supplementList";  // supplementList.jsp로 연결
    }

    // 위시리스트 페이지 추가
    @RequestMapping("/wish")
    public String wish() {
        return "member/wishlistView";
    }

    // IReviewService 의존성 주입
    @Autowired
    private IReviewService reviewService;
}
