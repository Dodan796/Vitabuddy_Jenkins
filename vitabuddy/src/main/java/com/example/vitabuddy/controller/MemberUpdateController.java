package com.example.vitabuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.vitabuddy.dto.UserSupplementDTO;
import com.example.vitabuddy.model.MemberVO;
import com.example.vitabuddy.service.MemberUpdateService;
import com.example.vitabuddy.service.SupplementService;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class MemberUpdateController {

	@Autowired
	MemberUpdateService memService;

	@Autowired

	SupplementService supService;

	// 마이페이지 > 회원정보 수정폼으로 이동
	@RequestMapping("/member/myInfoChangeForm")
	public String myInfoChangeForm(HttpSession session, Model model) {

		String userId = (String) session.getAttribute("sid");
		MemberVO myInfo = memService.myInfoUpdateForm(userId);
		model.addAttribute("myInfo", myInfo);

		// 복용 중인 영양제 리스트를 가져옴 => 추가된 코드.
		List<UserSupplementDTO> userSupplements = supService.getUserSupplements(userId);
		model.addAttribute("userSupplements", userSupplements);

		return "member/infoChange"; // JSP 파일 이름에 맞게 경로 수정
	}

	// 회원 정보 수정 처리
	@RequestMapping("/member/myInfoUpdate")
	public String myInfoUpdate(MemberVO vo, @RequestParam("userPh1") String userPh1,
			@RequestParam("userPh2") String userPh2, @RequestParam("userPh3") String userPh3, HttpSession session) {
		String userId = (String) session.getAttribute("sid");
		vo.setUserId(userId); // vosetUserId 추가 1017

		String userPh = userPh1 + "-" + userPh2 + "-" + userPh3;
		vo.setUserPh(userPh);
		memService.myInfoUpdate(vo);
		return "redirect:/member/myPage"; // 수정 후 마이페이지로 리다이렉트
	}

}