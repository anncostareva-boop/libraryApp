package com.example.library.controller;

import jakarta.validation.Valid;
import org.springframework.ui.Model;
import com.example.library.entity.User;
import com.example.library.enums.Role;
import com.example.library.form.RegisterForm;
import com.example.library.service.interfaces.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
public class AuthController {
    private final AuthService authService;

    public AuthController(
            AuthService authService
    ) {
        this.authService = authService;
    }

    // показати сторінку реєстрації
    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    // реєстрація
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterForm form, Model model,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("registerForm", form);
            model.addAttribute("failedOperationMessage",
                    bindingResult.getAllErrors().stream()
                            .map(ObjectError::getDefaultMessage)
                            .collect(Collectors.joining("\n")));
            return "register";
        }

        if(!form.getPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("passwordNotEqual",
                    "Паролі не співпадають");

            model.addAttribute("registerForm", form);

            return "register";
        }

        try {

            User user = new User();

            user.setUserName(form.getUsername());

            user.setEmail(form.getEmail());

            user.setPassword(form.getPassword());

            user.setRole(Role.CLIENT);

            authService.register(user);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Успішно зареєстровано");
            return "redirect:/login";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());

            return "register";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
