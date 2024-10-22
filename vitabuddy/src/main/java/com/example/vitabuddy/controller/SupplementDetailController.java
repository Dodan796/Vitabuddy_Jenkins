package com.example.vitabuddy.controller;

import com.example.vitabuddy.model.ReviewVO;
import com.example.vitabuddy.model.SupplementDetailVO;
import com.example.vitabuddy.service.ReviewService;
import com.example.vitabuddy.service.SupplementDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/api/supplement")
public class SupplementDetailController {

    @Autowired
    private SupplementDetailService supplementDetailService;
    
    @Autowired
    private ReviewService reviewService;
    
    @GetMapping("/supplementDetail/{id}")
    public String getSupplementDetail(@PathVariable("id") int supplementDetailId, Model model) {
        // 1. 해당 ID에 맞는 영양제 상세 정보 조회
        SupplementDetailVO supplementDetail = supplementDetailService.getSupplementDetailById(supplementDetailId);
        
        // 2. 이미지 처리 (Base64 인코딩)
        if (supplementDetail.getSupImg() != null) {
            String base64Image = Base64.getEncoder().encodeToString(supplementDetail.getSupImg());
            model.addAttribute("supImgBase64", base64Image);
        } else {
            model.addAttribute("supImgBase64", ""); // 이미지가 없는 경우 빈 문자열
        }

        // 3. 해당 영양제에 대한 리뷰 목록 조회
        List<ReviewVO> reviews = reviewService.reviewLists(supplementDetailId); // supId에 해당하는 리뷰 목록 조회
        model.addAttribute("reviewList", reviews);
        
        // 4. 영양제 상세 정보를 모델에 추가
        model.addAttribute("supplementDetail", supplementDetail);
        
        // 5. supplementDetail.jsp 뷰로 이동
        return "supplement/supplementDetail"; 
    }
}