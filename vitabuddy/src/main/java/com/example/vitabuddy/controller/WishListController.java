package com.example.vitabuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wishlist")
public class WishListController {

    @GetMapping
    public String getWishList(Model model) {
        
        return "supplement/wishList"; 
    }
}
