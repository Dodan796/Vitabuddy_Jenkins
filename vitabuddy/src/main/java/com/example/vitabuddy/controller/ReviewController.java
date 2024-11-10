package com.example.vitabuddy.controller;

import com.example.vitabuddy.model.ReviewVO;
import com.example.vitabuddy.model.SupplementDetailVO;
import com.example.vitabuddy.service.IReviewService;
import com.example.vitabuddy.service.SupplementDetailService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/supplement")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;
    
    @Autowired
	private SupplementDetailService supplementDetailService;

    // 파일이 저장될 경로
    private static final String UPLOAD_DIR = "C:/Review_Upload/"; 

    // 1. 리뷰 작성
    @PostMapping("/supplementDetail/{supId}/review")
    public String insertReview(@PathVariable("supId") int supId,
                               @RequestParam("reviewTitle") String reviewTitle,
                               @RequestParam("rating") String rating,
                               @RequestParam("reviewHashtag") String reviewHashtag,
                               @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                               @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                               @RequestParam("content") String content,
                               @RequestParam(value = "reviewImg", required = false) List<MultipartFile> reviewImgFiles,
                               HttpSession session) {

        String userId = (String) session.getAttribute("sid");
        if (userId == null) {
            return "redirect:/intro"; // 비회원일 경우 intro 페이지로 리다이렉트
        }

        ReviewVO reviewVO = new ReviewVO();
        String reviewNo = UUID.randomUUID().toString();
        reviewVO.setReviewNo(reviewNo);
        reviewVO.setSupId(supId);
        reviewVO.setUserId(userId);
        reviewVO.setReviewDate(new Date());
        reviewVO.setReviewTitle(reviewTitle);
        reviewVO.setRating(rating);
        reviewVO.setReviewHashtag(reviewHashtag);
        reviewVO.setStartDate(startDate);
        reviewVO.setEndDate(endDate);
        reviewVO.setContent(content);

        // 이미지 파일 처리
        StringBuilder imageNames = new StringBuilder();
        
        String DEFAULT_IMAGE = "defaultImage.PNG";  // 기본 이미지 파일 이름
        if (reviewImgFiles == null || reviewImgFiles.isEmpty()) {  //로직 추가
        	
        	imageNames.append(DEFAULT_IMAGE);
        	
        } 
        else if (reviewImgFiles != null && !reviewImgFiles.isEmpty()) {
            for (int i = 0; i < Math.min(3, reviewImgFiles.size()); i++) {
                MultipartFile file = reviewImgFiles.get(i);  //이미지 처리 
                
                if (!file.isEmpty()) {
                    String originalFilename = file.getOriginalFilename();
                    String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    String baseName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
                    String timestamp = String.valueOf(System.currentTimeMillis());
                    String uniqueFileName = baseName + "_" + timestamp + extension; 

                    try {
                        File destFile = new File(UPLOAD_DIR + uniqueFileName);
                        file.transferTo(destFile);
                        imageNames.append(uniqueFileName);
                        if (i < Math.min(3, reviewImgFiles.size()) - 1) {
                            imageNames.append(";"); 
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "error/fileUploadError";
                    }
                }
            }
        }
        //reviewVO.setReviewImg(imageNames.length() > 0 ? imageNames.toString() : "");
        reviewVO.setReviewImg(imageNames.length() > 0 ? imageNames.toString() : DEFAULT_IMAGE);
        System.out.println("이미지 출력 테스트 코드 입니다. " + reviewVO.getReviewImg());  //출력 test코드 추가

        int result = reviewService.insertReview(reviewVO);
        if (result == -1) {
            return "error/fileUploadError";
        }

        return "redirect:/api/supplement/supplementDetail/" + supId;
    }

    // 2. 리뷰 삭제
    @PostMapping("/supplementDetail/{supId}/review/{reviewNo}/delete")
    public String deleteReview(@PathVariable("supId") int supId, @PathVariable("reviewNo") String reviewNo, HttpSession session) {
        String userId = (String) session.getAttribute("sid");
        if (userId == null) {
            return "redirect:/intro";
        }
        
        int result = reviewService.deleteReview(reviewNo, userId);
        if (result == 0) {
            return "error/deleteError";
        }
        
        return "redirect:/api/supplement/supplementDetail/" + supId;
    }

	// 3. 리뷰 수정
	@SuppressWarnings("unused")
	@PostMapping("/supplementDetail/{supId}/review/{reviewNo}/edit")
	public String updateReview(@PathVariable("supId") int supId, 
	                           @PathVariable("reviewNo") String reviewNo,
	                           @RequestParam("reviewTitle") String reviewTitle, 
	                           @RequestParam("rating") String rating,
	                           @RequestParam("reviewHashtag") String reviewHashtag,
	                           @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
	                           @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
	                           @RequestParam("content") String content,
	                           @RequestParam(value = "reviewImg", required = false) List<MultipartFile> reviewImgFiles,
	                           HttpSession session) {

	    String userId = (String) session.getAttribute("sid");
	    if (userId == null) {
	        return "redirect:/intro";
	    }
	    
	    ReviewVO existingReview = reviewService.getReviewByNo(reviewNo);  // 로직 추가 - id로 리뷰 가지고 오기

	    ReviewVO reviewVO = new ReviewVO();
	    reviewVO.setReviewNo(reviewNo);
	    reviewVO.setSupId(supId);
	    reviewVO.setUserId(userId);
	    reviewVO.setReviewDate(new Date());
	    reviewVO.setReviewTitle(reviewTitle);
	    reviewVO.setRating(rating);
	    reviewVO.setReviewHashtag(reviewHashtag);
	    reviewVO.setStartDate(startDate);
	    reviewVO.setEndDate(endDate);
	    reviewVO.setContent(content);
	    
	    
	    
	    StringBuilder imageNames = new StringBuilder();
	    String DEFAULT_IMAGE = "defaultImage.PNG";  // 기본 이미지 파일 이름
	    
	    //String existingImages = reviewVO.getReviewImg();  // 기존에 입력했던 리뷰 이미지 얻어오기
	    //System.out.println("기존에 입력했던 이미지 입니다. " + existingImages);
	    
        if (reviewImgFiles == null || reviewImgFiles.isEmpty()) {  //로직 추가
        	  // 기존의 이미지가 있는 경우 그대로 유지하고, 없는 경우만 기본 이미지 설정
            if (existingReview.getReviewImg() != null && !existingReview.getReviewImg().isEmpty()) {
                imageNames.append(existingReview.getReviewImg());
            } else {
                imageNames.append(DEFAULT_IMAGE);
            }
        } 
        else if (reviewImgFiles != null && !reviewImgFiles.isEmpty()) {
	        for (int i = 0; i < Math.min(3, reviewImgFiles.size()); i++) {
	            MultipartFile file = reviewImgFiles.get(i);
	            if (!file.isEmpty()) {
	                String originalFilename = file.getOriginalFilename();
	                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
	                String baseName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
	                String timestamp = String.valueOf(System.currentTimeMillis());
	                String uniqueFileName = baseName + "_" + timestamp + extension;

	                try {
	                    File destFile = new File(UPLOAD_DIR + uniqueFileName);
	                    file.transferTo(destFile);
	                    imageNames.append(uniqueFileName);
	                    if (i < Math.min(3, reviewImgFiles.size()) - 1) {
	                        imageNames.append(";");
	                    }
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    return "error/fileUploadError";
	                }
	            }
	        }
	    }

	    //수정
	    //reviewVO.setReviewImg(imageNames.length() > 0 ? imageNames.toString() : DEFAULT_IMAGE);
        // 기존에 저장된 이미지를 유지하도록 설정 - 수정 시, 사용자가 업로드한 사진이 기본 defaultImage 로 변경되는 이슈
        //로직 추가
        if (imageNames.length() > 0) {
            reviewVO.setReviewImg(imageNames.toString());  // 새로 추가된 이미지가 있으면 그 값을 설정
        } else {
            // 새 이미지가 없을 경우 기존 이미지를 유지
            reviewVO.setReviewImg(existingReview.getReviewImg() != null ? existingReview.getReviewImg() : DEFAULT_IMAGE);
        }
        
        
        
	    int result = reviewService.updateReview(reviewVO);
	    if (result == 0) {
	        return "error/fileUploadError";
	    }

	    return "redirect:/api/supplement/supplementDetail/" + supId;
	}
	
	
	
	

	// 4. 리뷰 수정 폼 조회
	@GetMapping("/supplementDetail/{supId}/review/{reviewNo}/editForm")
	public String showEditReviewPage(@PathVariable("supId") int supId, 
	                                 @PathVariable("reviewNo") String reviewNo,
	                                 Model model) {

	    ReviewVO review = reviewService.getReviewByNo(reviewNo);
	    if (review == null) {
	        throw new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다. reviewNo: " + reviewNo);
	    }

	    SupplementDetailVO sup = supplementDetailService.getSupplementDetailById(supId);
	    if (sup == null) {
	        throw new IllegalArgumentException("해당 보충제를 찾을 수 없습니다. supId: " + supId);
	    }

	    model.addAttribute("review", review);
	    model.addAttribute("sup", sup);

	    return "supplement/editReview";
	}
}