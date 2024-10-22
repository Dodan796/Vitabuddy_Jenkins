package com.example.vitabuddy.controller;


import com.example.vitabuddy.dto.UserSupplementDTO;
import com.example.vitabuddy.service.SupplementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/supplement")
public class SupplementController {

    private final SupplementService supplementService;

    @Autowired
    public SupplementController(SupplementService supplementService) {
        this.supplementService = supplementService;
    }

    @GetMapping("/search")
    public List<UserSupplementDTO> searchSupplements(
        @RequestParam String keyword, 
        @RequestParam String brand) {
        return supplementService.searchSupplements(keyword, brand);
    }
    
    // 사용자가 복용 중인 영양제를 삭제하는 메서드
    @DeleteMapping("/delete")
    public String deleteSupplement(@RequestParam String userId, @RequestParam Integer supId) {
        boolean deleted = supplementService.deleteSupplement(userId, supId);
        return deleted ? "영양제가 성공적으로 삭제되었습니다." : "영양제 삭제에 실패했습니다.";
    }
    
    @PostMapping("/add")
    public ResponseEntity<String> addSupplement(@RequestParam String userId, @RequestParam Integer supId) {
        boolean added = supplementService.addSupplement(userId, supId);
        if (added) {
            return ResponseEntity.ok("영양제가 성공적으로 추가되었습니다.");
        } else {
            System.out.println("영양제 추가 실패: userId = " + userId + ", supId = " + supId);
            return ResponseEntity.status(500).body("영양제 추가에 실패했습니다.");
        }
    }


}