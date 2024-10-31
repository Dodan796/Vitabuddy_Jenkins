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
import com.example.vitabuddy.model.InteractionVO;
import com.example.vitabuddy.model.PurchaseHistoryVO;
import com.example.vitabuddy.model.ReviewVO;
import com.example.vitabuddy.model.RecommendVO;
import com.example.vitabuddy.service.CartListService;
import com.example.vitabuddy.service.IReviewService;
import com.example.vitabuddy.service.InteractionService;
import com.example.vitabuddy.service.RecommendService;
import com.example.vitabuddy.service.SupplementService;
import com.example.vitabuddy.model.SupplementStoreVO;

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
        model.addAttribute("ingredientNames", ingredientNames);
        model.addAttribute("interactions", interactions);

        // 추천 성분 및 관련 상위 제품 조회
        ArrayList<RecommendVO> recommendIngredientList = recommendService.recommendIngredients(userId);
        Map<String, SupplementStoreVO> topProductsByIngredient = new HashMap<>();
        Map<Object, ArrayList<RecommendVO>> allRecommendLists = new HashMap<>();

        for (int i = 0; i < recommendIngredientList.size(); i++) {
            String ingredientId = recommendIngredientList.get(i).getIngredientId();
            ArrayList<RecommendVO> recommendLists = recommendService.interactionRecommend(ingredientId);
            allRecommendLists.put(i, recommendLists);

            // 각 성분별 최상위 상품 조회 (상품명만 추가)
            SupplementStoreVO topProduct = reviewService.getTopProductByIngredient(ingredientId);
            if (topProduct != null) {
                topProductsByIngredient.put(ingredientId, topProduct);
                
                // 콘솔 출력
                System.out.println("Ingredient ID: " + ingredientId);
                System.out.println("Top Product Name: " + topProduct.getSupName());
            } else {
                System.out.println("No top product found for Ingredient " + ingredientId);
            }
        }

        // 모델에 데이터 추가
        model.addAttribute("recommendIngredientList", recommendIngredientList);
        model.addAttribute("topProductsByIngredient", topProductsByIngredient);
        model.addAttribute("allRecommendLists", allRecommendLists);

        // 사용자가 작성한 리뷰 목록을 조회
        List<ReviewVO> userReviews = reviewService.getUserReviews(userId);
        model.addAttribute("reviews", userReviews);

        // 구매내역 출력 (1개월, 1~3개월, 3개월 이상)
        ArrayList<PurchaseHistoryVO> myPagePurchaseLists = cartService.getUserPurchaseHistory(userId);
        LocalDate today = LocalDate.now();
        LocalDate oneMonthAgo = today.minusMonths(1);
        LocalDate threeMonthsAgo = today.minusMonths(3);

        ArrayList<PurchaseHistoryVO> recentPurchases = new ArrayList<>();
        ArrayList<PurchaseHistoryVO> midTermPurchases = new ArrayList<>();
        ArrayList<PurchaseHistoryVO> oldPurchases = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (PurchaseHistoryVO purchase : myPagePurchaseLists) {
            LocalDate orderDate = LocalDate.parse(purchase.getOrderId(), formatter);
            if (orderDate.isAfter(oneMonthAgo)) {
                recentPurchases.add(purchase);
            } else if (orderDate.isAfter(threeMonthsAgo)) {
                midTermPurchases.add(purchase);
            } else {
                oldPurchases.add(purchase);
            }
        }

        model.addAttribute("recentPurchases", recentPurchases);
        model.addAttribute("midTermPurchases", midTermPurchases);
        model.addAttribute("oldPurchases", oldPurchases);

        return "member/myPage";
    }
}
