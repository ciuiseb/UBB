package mobile.model.dtos;

import mobile.model.Book;
import java.time.LocalDate;

public class BookDTO {
    private Long id;
    private String name;
    private String author;
    private Boolean stars;
    private LocalDate publishingDate;
    private String userId;

    public BookDTO() {
    }

    public BookDTO(Long id, String name, String author, Boolean stars, LocalDate publishingDate, String userId) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.stars = stars;
        this.publishingDate = publishingDate;
        this.userId = userId;
    }

    public static BookDTO toDto(Book book) {
        return new BookDTO(
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.getStars(),
                book.getPublishingDate(),
                book.getUserId()
        );
    }

    public static Book fromDto(BookDTO dto) {
        return new Book(
                dto.getId(),
                dto.getName(),
                dto.getAuthor(),
                dto.getStars(),
                dto.getPublishingDate(),
                dto.getUserId()
        );
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Boolean getStars() { return stars; }
    public void setStars(Boolean stars) { this.stars = stars; }

    public LocalDate getPublishingDate() { return publishingDate; }
    public void setPublishingDate(LocalDate publishingDate) { this.publishingDate = publishingDate; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}