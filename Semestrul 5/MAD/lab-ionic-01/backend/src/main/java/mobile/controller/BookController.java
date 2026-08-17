package mobile.controller;

import mobile.model.Theme;
import mobile.model.dtos.BookDTO;
import mobile.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.Arrays;
import java.util.List;

@RestController
public class BookController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private BookService bookService;

    @GetMapping("/api/themes")
    public List<String> getThemes() {
        return Arrays.stream(Theme.values())
                .map(Theme::toString)
                .toList();
    }

    @GetMapping("/api/books")
    public List<BookDTO> getBooks() {
        return bookService.getAllBooks().stream().map(BookDTO::toDto).toList();
    }

    @PostMapping("api/books")
    public BookDTO createBook(@RequestBody BookDTO dto) {
        bookService.createBook(BookDTO.fromDto(dto));
        messagingTemplate.convertAndSend("/topic/newBooks", dto);
        return dto;
    }
}
