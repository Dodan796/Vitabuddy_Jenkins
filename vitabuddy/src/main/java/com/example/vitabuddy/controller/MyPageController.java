package com.example.vitabuddy.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.vitabuddy.dto.UserSupplementDTO;
import com.example.vitabuddy.model.CartListVO;
import com.example.vitabuddy.model.InteractionVO;
import com.example.vitabuddy.model.PurchaseHistoryVO;
import com.example.vitabuddy.model.ReviewVO;
import com.example.vitabuddy.model.RecommendVO;
import com.example.vitabuddy.model.SupplementStoreVO;
import com.example.vitabuddy.service.CartListService;
import com.example.vitabuddy.service.IReviewService;
import com.example.vitabuddy.service.InteractionService;
import com.example.vitabuddy.service.RecommendService;
import com.example.vitabuddy.service.SupplementService;

@Controller
@RequestMapping("/member")
public class MyPageController {

    @Autowired
    private SupplementService supService;

    @Autowired
    private IReviewService reviewService;

    @Autowired
    private InteractionService intService;

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private CartListService cartService; 

    // 마이페이지로 이동
    @GetMapping("/myPage")
    public String myPage(HttpSession session, Model model) {
        // 세션에서 사용자 ID를 가져옴
        String userId = (String) session.getAttribute("sid");
        if (userId == null) {
            return "redirect:/intro"; // 로그인 페이지로 리다이렉트
        }

        // 사용자가 복용 중인 영양제 목록을 조회
        List<UserSupplementDTO> userSupplements = supService.getUserSupplements(userId);
        model.addAttribute("userSupplements", userSupplements);

        // 상호작용 정보 조회
        List<String> ingredientNames = intService.getIngredientNames(userId);
        List<InteractionVO> interactions = intService.getInteractionDetails(userId);
        System.out.println("Interactions 데이터: " + interactions);

        model.addAttribute("ingredientNames", ingredientNames);
        model.addAttribute("interactions", interactions);

        // 추천 성분 목록을 가져옴
        ArrayList<RecommendVO> recommendIngredientList = recommendService.recommendIngredients(userId);
        model.addAttribute("recommendIngredientList", recommendIngredientList);

        // 각 추천 성분의 상위 제품을 가져와 맵에 저장
        Map<String, SupplementStoreVO> topProductsByIngredient = new HashMap<>();
        for (RecommendVO recommendIngredient : recommendIngredientList) {
            String ingredientId = recommendIngredient.getIngredientId();
            System.out.println("Processing ingredientId for recommendation: " + ingredientId);

            // 추천 성분(주성분)을 기준으로 최상위 제품을 조회
            SupplementStoreVO topProduct = reviewService.getTopProductByIngredient(ingredientId);
            if (topProduct != null) {
                topProductsByIngredient.put(ingredientId, topProduct);
                System.out.println("Top Product for Recommended Ingredient " + ingredientId + ": " + topProduct.getSupName());
            } else {
                System.out.println("No top product found for Recommended Ingredient ID: " + ingredientId);
            }
        }

        // JSP에 전달할 모델 추가
        model.addAttribute("topProductsByIngredient", topProductsByIngredient);


        // 추천 성분에 따른 상호작용 추천 리스트 생성
        Map<Object, ArrayList<RecommendVO>> allRecommendLists = new HashMap<>();
        for (int i = 0; i < recommendIngredientList.size(); i++) {
            String ingredientId = recommendIngredientList.get(i).getIngredientId();
            System.out.println("Processing ingredientId for interaction recommendations: " + ingredientId);

            // 각 성분의 추천 리스트 조회
            ArrayList<RecommendVO> recommendLists = recommendService.interactionRecommend(ingredientId);
            System.out.println("Interaction recommendations: " + recommendLists);

            // Map에 추천 리스트 저장
            allRecommendLists.put(i, recommendLists);
        }

        // 모델에 데이터 추가
        model.addAttribute("allRecommendLists", allRecommendLists);

        // 사용자가 작성한 리뷰 목록을 조회
        List<ReviewVO> userReviews = reviewService.getUserReviews(userId);
        model.addAttribute("reviews", userReviews);

        // 구매내역 출력 (1개월, 1~3개월, 3개월 이상)
        ArrayList<PurchaseHistoryVO> myPagePurchaseLists = cartService.getUserPurchaseHistory(userId);
        LocalDate today = LocalDate.of(2024, 11, 27); // 2024년 11월 27일로 설정
        LocalDate oneMonthAgo = today.minusMonths(1); // 1개월 전
        LocalDate threeMonthsAgo = today.minusMonths(3); // 3개월 전

        ArrayList<PurchaseHistoryVO> recentPurchases = new ArrayList<>(); // 1개월 구매내역
        ArrayList<PurchaseHistoryVO> midTermPurchases = new ArrayList<>(); // 1~3개월 구매내역
        ArrayList<PurchaseHistoryVO> oldPurchases = new ArrayList<>(); // 3개월 구매내역

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // formatter

        for (PurchaseHistoryVO myPagePurchaseList : myPagePurchaseLists) {
            String dateString = myPagePurchaseList.getOrderId(); // 날짜 형식 (String형)
            LocalDate orderDate = LocalDate.parse(dateString, formatter); // 날짜 형식으로 변환
            if (orderDate.isAfter(oneMonthAgo)) {
                recentPurchases.add(myPagePurchaseList); // 1개월 이내
            } else if (orderDate.isAfter(threeMonthsAgo)) {
                midTermPurchases.add(myPagePurchaseList); // 1~3개월
            } else {
                oldPurchases.add(myPagePurchaseList); // 3개월 이전
            }
        }
        model.addAttribute("recentPurchases", recentPurchases);
        model.addAttribute("midTermPurchases", midTermPurchases);
        model.addAttribute("oldPurchases", oldPurchases);

        return "member/myPage";
    }
}
