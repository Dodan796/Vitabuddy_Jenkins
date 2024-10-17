package com.example.vitabuddy.dao;

import com.example.vitabuddy.model.ReviewVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IReviewDAO {

    // 리뷰 조회 기능
    List<ReviewVO> reviewLists(int supId);

    // 리뷰 작성 기능
    int insertReview(ReviewVO review);

    // 리뷰 삭제 기능
    int deleteReview(String reviewNo, String userId);

    // 리뷰 수정 기능
    int updateReview(ReviewVO review);

    // 리뷰 번호를 통한 리뷰 조회.
    ReviewVO getReviewByNo(String reviewNo);

    // 특정 사용자에 대한 리뷰 조회
    List<ReviewVO> getReviewsByUserId(@Param("userId") String userId);
}
