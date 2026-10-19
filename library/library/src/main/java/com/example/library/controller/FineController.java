package com.example.library.controller;

import com.example.library.entity.User;
import com.example.library.service.interfaces.FineService;
import com.example.library.service.interfaces.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/fines")
public class FineController {
    private final FineService
            fineService;

    private final UserService
            userService;

    public FineController(
            FineService fineService,
            UserService userService
    ) {

        this.fineService =
                fineService;

        this.userService =
                userService;
    }

    // показати штрафи користувача
    @GetMapping
    public String fines(Authentication auth, Model model) {

        User user = userService.getByUsername(auth.getName());

        model.addAttribute("fines",
                fineService.getUserFines(user.getUserID()));

        return "fines";
    }

    // сплата штрафу
    @PostMapping("/pay/{id}")
    public String pay(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        fineService.payFine(id);

        redirectAttributes.addFlashAttribute("successMessage",
                "Штраф успішно сплачено!");
        return "redirect:/fines";
    }
}
