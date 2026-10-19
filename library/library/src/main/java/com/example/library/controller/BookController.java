package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.form.BookSearchForm;
import com.example.library.service.interfaces.BookService;
import com.example.library.service.interfaces.CopyService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    private final CopyService copyService;

    public BookController(
            BookService bookService,
            CopyService copyService
    ) {

        this.bookService = bookService;
        this.copyService = copyService;
    }

    // відображення книг в каталозі
    @GetMapping
    public String books(Model model) {

        model.addAttribute(
                "books",
                bookService.getAllBooks()
        );

        return "books";
    }

    // Деталі книги. Для клієнта або гостя відображаються просто деталі книги, для адміна + список копій книги
    @GetMapping("/{id}")
    public String showBook(@PathVariable Long id, Authentication auth, Model model) {

        Book book = bookService.getBookByID(id);
        model.addAttribute("book", book);
        model.addAttribute("available", book.getStatus().getLabel());

        boolean isAdmin = false;

        if(auth != null) {
            isAdmin = auth
                    .getAuthorities()
                    .stream()
                    .anyMatch(a ->
                            a.getAuthority()
                                    .equals("ROLE_ADMIN")
                    );
        }

        // Якщо поточний користувач - адміністратор, показує список копій даної книги
        if(isAdmin) {
            model.addAttribute("copies", copyService.showCopies(id));
        }

        return "book-details";
    }

    // пошук за декількома параметрами
    @GetMapping("/search")
    public String search(BookSearchForm form, Model model) {

        model.addAttribute("books",
                bookService.searchBook(form.getTitle(), form.getAuthor(), form.getGenre()));

        return "books";
    }
}
