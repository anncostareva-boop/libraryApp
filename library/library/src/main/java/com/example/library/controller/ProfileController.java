package com.example.library.controller;

import com.example.library.config.CustomUserDetails;
import com.example.library.entity.IssuedBookInfo;
import com.example.library.entity.User;
import com.example.library.enums.Role;
import com.example.library.form.EditProfileForm;
import com.example.library.service.interfaces.AuthService;
import com.example.library.service.interfaces.FineService;
import com.example.library.service.interfaces.IssuanceService;
import com.example.library.service.interfaces.UserService;

import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final IssuanceService issuanceService;
    private final FineService fineService;
    private final AuthService authService;

    public ProfileController(
            UserService userService,
            IssuanceService issuanceService,
            FineService fineService,
            AuthService authService
    ) {
        this.userService = userService;
        this.issuanceService = issuanceService;
        this.fineService = fineService;
        this.authService = authService;
    }

    @GetMapping
    public String profile(Authentication auth, Model model) {

        User user = userService.getByUsername(auth.getName());

        if (issuanceService.hasOverdueBooks(user.getUserID())) {
            model.addAttribute(
                    "overdueWarning",
                    "У вас є прострочені книги!"
            );
        }

        if (fineService.hasUnpaidFines(user.getUserID())) {
            model.addAttribute(
                    "unpaidFinesWarning",
                    "У вас є несплачені штрафи!"
            );
        }

        model.addAttribute("user", user);

        model.addAttribute(
                "fines",
                fineService.getUserFines(user.getUserID())
        );

        List<IssuedBookInfo> issuedBooks =
                issuanceService.getUserIssuedBooks(user.getUserID());

        if (issuedBooks.isEmpty()) {
            model.addAttribute(
                    "noIssuedBooksMessage",
                    "Немає виданих книг"
            );
        }

        model.addAttribute("issuedBooks", issuedBooks);

        return "profile";
    }

    @GetMapping("/edit")
    public String editPage(Authentication auth, Model model) {

        User user = userService.getByUsername(auth.getName());

        EditProfileForm form = new EditProfileForm();

        form.setUsername(user.getUserName());
        form.setEmail(user.getEmail());

        model.addAttribute("form", form);

        return "edit-profile";
    }

    @PostMapping("/edit")
    public String edit(Authentication auth,
                       @Valid @ModelAttribute("form") EditProfileForm form,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            model.addAttribute("failedOperationMessage",
                    bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("\n")));
            return "edit-profile";
        }

        User user = userService.getByUsername(auth.getName());

        if(user == null) {
            return "redirect:/login";
        }

        if(!user.getUserName().equals(form.getUsername())
                && authService.usernameExists(form.getUsername())) {

            model.addAttribute("form", form);

            model.addAttribute("usernameExists",
                    "Користувач з таким іменем вже існує");

            return "edit-profile";
        }

        if(!user.getEmail().equals(form.getEmail())
                && authService.emailExists(form.getEmail())) {

            model.addAttribute("form", form);

            model.addAttribute("emailExists",
                    "Користувач з такою електронною адресою вже існує");

            return "edit-profile";
        }

        String oldUsername = user.getUserName();

        user.setUserName(form.getUsername());
        user.setEmail(form.getEmail());

        userService.editProfile(user);

        if(!oldUsername.equals(user.getUserName())) {
            return "redirect:/logout";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Профіль успішно оновлено"
        );

        if (user.getRole() == Role.CLIENT) {
            return "redirect:/profile";
        }
        else {
            return "redirect:/admin/profile";
        }
    }
}