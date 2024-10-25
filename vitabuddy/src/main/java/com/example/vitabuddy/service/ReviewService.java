package com.example.vitabuddy.service;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.vitabuddy.dao.IReviewDAO;
import com.example.vitabuddy.model.ReviewVO;
import com.example.vitabuddy.model.SupplementStoreVO;

@Service
public class ReviewService implements IReviewService {

    @Autowired
    @Qualifier("IReviewDAO")
    IReviewDAO dao;

    // 특정 제품에 대한 리뷰 조회 기능
    @Override
    public List<ReviewVO> reviewLists(int supId) {
        return dao.reviewLists(supId);
    }

    // 리뷰 작성 기능
    @Override
    public int insertReview(ReviewVO review) {
        return dao.insertReview(review);
    }
    
    // 리뷰 삭제 기능
    @Override
    public int deleteReview(String reviewNo, String userId) {
        return dao.deleteReview(reviewNo, userId);
    }
    
    // 리뷰 수정 기능
    @Override
    public int updateReview(ReviewVO review) {
        return dao.updateReview(review);
    }

    // 리뷰 번호를 통한 리뷰 조회
    @Override
    public ReviewVO getReviewByNo(String reviewNo) {
        return dao.getReviewByNo(reviewNo);
    }

    // 특정 사용자에 대한 리뷰 조회 기능
    public List<ReviewVO> getUserReviews(String userId) {
        return dao.getReviewsByUserId(userId);
    }
    

    @Override
    public List<SupplementStoreVO> getTopSupplementsByBrand() {
        List<SupplementStoreVO> supplements = dao.getTopSupplementsByBrand();

        for (SupplementStoreVO supplement : supplements) {
            try {
                // SupID에 해당하는 이미지를 byte[]로 가져옴
                byte[] image = dao.getSupplementImageById(supplement.getSupId());

                if (image != null) {
                    // Base64로 인코딩된 이미지 저장
                    String base64Image = Base64.getEncoder().encodeToString(image);
                    supplement.setBase64SupImg(base64Image);
                } else {
                    System.out.println("No image found for SupID " + supplement.getSupId());
                }
            } catch (Exception e) {
                System.err.println("Error fetching image for SupID: " + supplement.getSupId() + " - " + e.getMessage());
                e.printStackTrace();
            }
        }
        return supplements;
    }
}