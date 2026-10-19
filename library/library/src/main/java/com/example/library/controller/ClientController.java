package com.example.library.controller;

import com.example.library.entity.IssuedBookInfo;
import com.example.library.entity.Request;
import com.example.library.entity.User;
import com.example.library.service.interfaces.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/client")

@PreAuthorize("hasRole('CLIENT')")
public class ClientController {
    private final RequestService
            requestService;

    private final UserService
            userService;


    public ClientController(
            RequestService requestService,
            UserService userService
    ) {
        this.requestService =
                requestService;

        this.userService =
                userService;

    }

    //Показати активні запити поточного авторизованого користувача
    @GetMapping("/requests")
    public String requests(Authentication auth, Model model) {
        User user = userService.getByUsername(auth.getName());

        List<Request> userRequests = requestService.getUserActiveRequests(user.getUserID());

        if(userRequests.isEmpty()) {
            model.addAttribute("noActiveRequestsMessage",
                    "Немає активних запитів");
        }

        model.addAttribute("requests", requestService.getUserActiveRequests(user.getUserID()));


        return "client-requests";
    }

    //Замовити книгу і створити відповідний запит на видачу
    @PostMapping("/request/{bookID}")
    public String createRequest(@PathVariable Long bookID, Authentication auth,
                                Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getByUsername(auth.getName());

            requestService.createRequest(user.getUserID(), bookID);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Книгу успішно замовлено!");
            return "redirect:/client/requests";

        } catch (Exception e) {
            // при наявності несплачених штрафів/прострочених книг/відсутності доступних копій -
            // перекидає назад на сторінку книги з повідомленням про невдале замовлення
            redirectAttributes.addFlashAttribute("failedOperationMessage", e.getMessage());
            return "redirect:/books/" + bookID;

        }

    }
}
