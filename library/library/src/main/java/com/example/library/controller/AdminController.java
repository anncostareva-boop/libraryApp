package com.example.library.controller;

import com.example.library.dao.interfaces.IFineDAO;
import com.example.library.entity.Fine;
import com.example.library.entity.Notification;
import com.example.library.entity.User;
import com.example.library.enums.FineReason;
import com.example.library.form.ReturnBookForm;
import com.example.library.service.interfaces.FineService;
import com.example.library.service.interfaces.IssuanceService;
import com.example.library.service.interfaces.NotificationService;
import com.example.library.service.interfaces.RequestService;
import com.example.library.service.interfaces.UserService;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final RequestService requestService;

    private final IssuanceService issuanceService;

    private final UserService userService;

    private final FineService fineService;


    public AdminController(
            RequestService requestService,
            IssuanceService issuanceService,
            UserService userService,
            FineService fineService
    ) {

        this.requestService = requestService;

        this.issuanceService = issuanceService;

        this.userService = userService;

        this.fineService = fineService;

    }

    // всі активні запити
    @GetMapping("/requests")
    public String requests(Model model) {

        model.addAttribute(
                "requests",
                requestService.getActiveRequests()
        );

        return "admin-requests";
    }

    // схвалення запиту
    @PostMapping("/request/{id}/approve")
    public String approve(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        try {

            requestService.approveRequest(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Запит успішно прийнято! Копія книги заброньована"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "failedOperationMessage",
                    e.getMessage()
            );
        }

        return "redirect:/admin/requests";
    }

    // відхилення запиту
    @PostMapping("/request/{id}/reject")
    public String reject(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        try {

            requestService.rejectRequest(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Запит успішно відхилено"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "failedOperationMessage",
                    e.getMessage()
            );
        }

        return "redirect:/admin/requests";
    }

    // видача книги
    @PostMapping("/issue/{id}")
    public String issue(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        try {

            issuanceService.issueBook(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Видачу успішно зареєстровано"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "failedOperationMessage",
                    e.getMessage()
            );
        }

        return "redirect:/admin/requests";
    }

    // профіль користувача
    @GetMapping("/user/{id}")
    public String user(
            @PathVariable Long id,
            Model model
    ) {

        User user = userService.getUserByID(id);

        model.addAttribute("user", user);

        model.addAttribute(
                "fines",
                fineService.getUserFines(id)
        );

        model.addAttribute(
                "issuedBooks",
                issuanceService.getUserIssuedBooks(id)
        );

        return "admin-user-profile";
    }

    // сторінка повернення книги
    @GetMapping("/return")
    public String returnPage(Model model) {

        model.addAttribute(
                "form",
                new ReturnBookForm()
        );

        return "return-book";
    }

    // реєстрація повернення
    @PostMapping("/return")
    public String returnBook(
            @Valid @ModelAttribute ReturnBookForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            model.addAttribute("failedOperationMessage",
                    bindingResult.getAllErrors().stream()
                            .map(ObjectError::getDefaultMessage)
                            .collect(Collectors.joining("\n")));
            return "return-book";
        }

        if(form.getDamaged() == true &&
                (form.getFineAmount() == null || form.getFineAmount().signum() == 0)) {
            model.addAttribute("form", form);
            model.addAttribute("failedOperationMessage", "Введіть розмір штрафу");
            return "return-book";
        }
        else if(form.getDamaged() == false && form.getFineAmount() != null
                && form.getFineAmount().signum() != 0) {
            model.addAttribute("form", form);
            model.addAttribute("failedOperationMessage",
                    "Поставте відмітку що книгу пошкоджено");
            return "return-book";
        }

        try {

            issuanceService.returnBook(
                    form.getUserID(),
                    form.getCopyID(),
                    form.getDamaged()
            );

            if (form.getDamaged() == true
                    && form.getFineAmount() != null
                    && form.getFineAmount().signum() != 0) {
                Fine fine = new Fine();
                fine.setUserID(form.getUserID());
                fine.setAmount(form.getFineAmount());
                fine.setReason(FineReason.DAMAGE);
                fineService.createFine(fine);
            }

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Повернення успішно зареєстровано"
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "failedOperationMessage",
                    e.getMessage()
            );
        }

        return "redirect:/admin/return";
    }

    // всі штрафи
    @GetMapping("/fines")
    public String fines(Model model) {

        List<Fine> fines = fineService.getAllFines();

        model.addAttribute("fines", fines);

        return "admin-fines";
    }

    @GetMapping("/users")
    public String users(Model model) {

        List<User> users = userService.getAllUsers();

        model.addAttribute("users", users);

        return "admin-users";
    }

    @GetMapping("/issuances")
    public String issuances(Model model) {

        model.addAttribute(
                "issuances",
                issuanceService.getAllActiveIssuances()
        );

        return "admin-issuance";
    }

    @GetMapping("/profile")
    public String adminProfile(Authentication auth, Model model) {

        User admin = userService.getByUsername(auth.getName());
        model.addAttribute("user", admin);
        return "admin-profile";
    }

}