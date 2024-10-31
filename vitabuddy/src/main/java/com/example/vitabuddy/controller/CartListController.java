package com.example.vitabuddy.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.vitabuddy.dto.UserSupplementDTO;
import com.example.vitabuddy.model.CartListVO;
import com.example.vitabuddy.model.MemberVO;
import com.example.vitabuddy.model.OrderInfoVO;
import com.example.vitabuddy.service.CartListService;
import com.example.vitabuddy.service.MemberUpdateService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartListController {

	@Autowired
	CartListService cartListService;

	@Autowired
	MemberUpdateService memService; // orderForm 접속 시, 입력한 회원정보 데이터 노출시키기 위해 MemberUpdateService 활용한다

	// 임시 url - 나중에 수정해야 함 => /supplement 링크 추가(wishList와 통일시킴)
	@RequestMapping("/supplement/cartList")
	public String cartList(Model model, HttpSession session) {
		String userId = (String) session.getAttribute("sid"); // 로그인된 userId 추출
		
		ArrayList<CartListVO> cartLists = cartListService.cartList(userId);
		model.addAttribute("cartLists", cartLists);
		return "supplement/cartList";
	}

	// 장바구니 추가
	@ResponseBody
	@PostMapping("/api/addCart")
	public int addCart(@RequestBody CartListVO vo) { // 객체를 @RequestBody로 불러온다
		System.out.println("장바구니에 추가할 상품 ID: " + vo.getSupId()); // ajax 에서 잘 넘어왔는지 test 출력
		System.out.println("로그인된 사용자 ID: " + vo.getUserId()); // ajax 에서 잘 넘어왔는지 test 출력 (결과 : 잘 넘어옴)

		int result = 0;
		try {

			// 동일한 상품이 있는지 확인: 동일 상품 갯수 반환
			int count = cartListService.checkSupInCart(vo.getSupId(), vo.getUserId()); // 상품이 있으면 1, 없으면 0반환ㄴ
			System.out.println("***상품의 갯수는 " + count + " 개 입니다.");

			if (count == 0) {
				int cartQty = vo.getCartQty() + 1; // 1 증가를 하지 않고 add하면 cartQty 값이 0으로 저장됨
				vo.setCartQty(cartQty); // 1값으로 세팅
				System.out.println("***추가할 상품의 갯수는?********* " + vo.getCartQty() + " 개 입니다.");
				cartListService.addCartList(vo);
			} else {
				int cartQty = vo.getCartQty() + 1; // 1 증가를 하고 update 할 것
				vo.setCartQty(cartQty); // 1값으로 세팅
				System.out.println("***추가할 상품의 갯수는?********* " + vo.getCartQty() + " 개 입니다.");
				cartListService.updateQtyInCart(vo); // 기존 수량에 + 1 하는 효과
			}

			// 성공 시 1 반환
			result = 1;

		} catch (Exception e) {
			System.out.println("장바구니 처리 중 오류가 발생했습니다: " + e.getMessage());
			// 실패 시 0 반환 (기본값이 0이므로 catch에서 따로 설정 필요 없음)
		}

		return result;
	}

	// +/- 버튼 눌렀을 때 상품 수량 업데이트 하는 로직
	@ResponseBody
	@PostMapping("/updateCartQty")
	public int updateCartQty(@RequestParam("cartId") int cartId, @RequestParam("cartQty") int qty, CartListVO vo) {
		System.out.println("수량 변경했을 때, 업데이트할 cartId : " + vo.getCartId()); // ajax 에서 잘 넘어왔는지 test 출력
		System.out.println("수량 변경했을 때, 업데이트할 수량 " + vo.getCartQty()); // ajax 에서 잘 넘어왔는지 test 출력 (결과 : 잘 넘어옴)

		cartListService.changeQtyInCart(cartId, qty);
		return 1;

	}

	// 장바구니 삭제
	@ResponseBody
	@RequestMapping("/api/deleteCart")
	public int deleteCart(@RequestParam("cartId") String cartId) {

		System.out.println("삭제할 영양제는 : " + cartId + " 번 영양제 입니다.***"); // 파라미터 값으로 잘 넘어왔는지 테스트 출력
		int result = 0;

		if (cartId != null) {
			cartListService.deleteCart(cartId);
			result = 1;
		}

		return result;

	}

	// --------------------------------[주문하기] : 결제하기 버튼 눌렀을 때 결제 페이지로
	// 넘어감------------------------------------
	@RequestMapping("/supplement/orderForm")
	public String orderForm(HttpSession session, Model model) {
		// 주문자의 정보를 가지고 온다
		String userId = (String) session.getAttribute("sid");
		MemberVO myInfo = memService.myInfoUpdateForm(userId);
		model.addAttribute("myInfo", myInfo);

		// 장바구니에 있는 제품을 가지고 온다
		ArrayList<CartListVO> cartLists = cartListService.cartList(userId);
		model.addAttribute("cartLists", cartLists);

		return "supplement/orderForm";
	}

	// 주문 완료 로직 처리 - [결제하기] 버튼 눌렀을 때 로직 처리
	@RequestMapping("/supplement/orderComplete")
	public String orderComplete(OrderInfoVO vo, @RequestParam String ordRcvPh1, @RequestParam String ordRcvPh2,
			@RequestParam String ordRcvPh3, HttpSession session) {

		// 3개의 영역으로 분할된 전화번호 통합 및 vo 세팅
		String ordRcvPhone = ordRcvPh1 + "-" + ordRcvPh2 + "-" + ordRcvPh3;
		vo.setOrdRcvPhone(ordRcvPhone);

		// orderId 생성 로직 - [주문당시날짜시분초_랜덤숫자4개] 형식으로 진행할 예정임
		long timeNum = System.currentTimeMillis();
		// 날짜시간포맷 : MM(월) mm(분) HH(시간;24)
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmmss");
		String strTime = dayTime.format(new Date(timeNum));
		String rNum = "";
		for (int i = 1; i <= 4; i++) {
			rNum += ((int) (Math.random() * 10));
		}

		// orderId
		String orderId = strTime + "_" + rNum;
		vo.setOrderId(orderId);
		System.out.println("생성된 주문 번호는? " + orderId + " 입니다.");

		// 주문 정보 저장
		cartListService.insertOrderInfo(vo);
		return "supplement/orderComplete";
	}

}