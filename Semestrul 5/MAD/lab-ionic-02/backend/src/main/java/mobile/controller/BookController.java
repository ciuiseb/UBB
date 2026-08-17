package mobile.controller;

import mobile.model.Book;
import mobile.model.User;
import mobile.model.dtos.BookDTO;
import mobile.repository.BookRepository;
import mobile.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class BookController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/books")
    public Map<String, Object> getBooks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "year", required = false) Integer year,
            Principal principal
    ) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        LocalDate dateFilter = null;
        if (year != null) {
            dateFilter = LocalDate.of(year, 1, 1);
        }
        String searchTitle = (search == null) ? null : "%" + search + "%";
        Page<Book> pageResult = bookRepository.findByUserIdAndFilter(
                user.getId().toString(),
                searchTitle,
                dateFilter,
                pageable
        );

        Map<String, Object> response = new HashMap<>();
        response.put("content", pageResult.getContent().stream().map(BookDTO::toDto).toList());
        response.put("isLast", pageResult.isLast());
        response.put("totalItems", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());

        return response;
    }

    @GetMapping("/api/books/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(value -> ResponseEntity.ok(BookDTO.toDto(value)))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/api/books")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO dto, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Book newBook = BookDTO.fromDto(dto);
        newBook.setId(null);
        newBook.setUserId(user.getId().toString());
        Book savedBook = bookRepository.save(newBook);
        return new ResponseEntity<>(BookDTO.toDto(savedBook), HttpStatus.CREATED);
    }

    @PutMapping("/api/books/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO dto) {
        Optional<Book> existingBookOpt = bookRepository.findById(id);

        if (existingBookOpt.isPresent()) {
            Book book = existingBookOpt.get();

            book.setName(dto.getName());
            book.setAuthor(dto.getAuthor());
            book.setStars(dto.getStars());
            book.setPublishingDate(dto.getPublishingDate());

            Book updatedBook = bookRepository.save(book);

            BookDTO updatedDto = BookDTO.toDto(updatedBook);
            messagingTemplate.convertAndSend("/topic/updatedBooks", updatedDto);

            return ResponseEntity.ok(updatedDto);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);

            messagingTemplate.convertAndSend("/topic/deletedBooks", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}