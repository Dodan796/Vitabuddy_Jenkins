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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/supplement")
public class SupplementDetailController {

    @Autowired
    private SupplementDetailService supplementDetailService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/supplementDetail/{id}")
    public String getSupplementDetail(
            @PathVariable("id") int supplementDetailId, 
            @RequestParam(value = "page", defaultValue = "1") int page, 
            Model model) {

        SupplementDetailVO supplementDetail = supplementDetailService.getSupplementDetailById(supplementDetailId);
        if (supplementDetail.getSupImg() != null) {
            String base64Image = Base64.getEncoder().encodeToString(supplementDetail.getSupImg());
            model.addAttribute("supImgBase64", base64Image);
        } else {
            model.addAttribute("supImgBase64", "");
        }

        List<ReviewVO> reviews = reviewService.pagingReviewList(supplementDetailId, page);
        model.addAttribute("reviewList", reviews);

        int totalReviews = reviewService.countReviews(supplementDetailId);
        int totalPages = (int) Math.ceil((double) totalReviews / reviewService.getReviewsPerPage());

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("supplementDetail", supplementDetail);

        return "supplement/supplementDetail"; 
    }

    @GetMapping("/supplementDetail/{id}/reviews")
    @ResponseBody
    public Map<String, Object> getReviews(
            @PathVariable("id") int supplementDetailId, 
            @RequestParam(value = "page", defaultValue = "1") int page) {

        List<ReviewVO> reviews = reviewService.pagingReviewList(supplementDetailId, page);
        int totalReviews = reviewService.countReviews(supplementDetailId);
        int totalPages = (int) Math.ceil((double) totalReviews / reviewService.getReviewsPerPage());

        Map<String, Object> response = new HashMap<>();
        response.put("reviewList", reviews);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);

        return response;
    }

}
