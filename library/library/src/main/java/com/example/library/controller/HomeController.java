package com.example.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

//    @GetMapping("/task")
//    public String task(Model model) {
//
//        model.addAttribute(
//                "list",
//                сервіс.Метод()
//        );
//
//        return "task";
//    }
}
