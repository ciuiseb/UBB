package mobile.model.dtos;

import mobile.model.Book;
import mobile.model.Theme;

import java.time.LocalDate;

public class BookDTO {
    private String name;
    private String author;
    private String theme;
    private String datePublished;
    private String hasNobelPrize;

    public BookDTO() {
    }

    public BookDTO(String name, String author, String theme, String datePublished, String hasNobelPrize) {
        this.name = name;
        this.author = author;
        this.theme = theme;
        this.datePublished = datePublished;
        this.hasNobelPrize = hasNobelPrize;
    }

    public static BookDTO toDto(Book book) {
        return new BookDTO(
                book.getName(),
                book.getAuthor(),
                book.getTheme().toString(),
                book.getHasNobelPrize().toString(),
                book.getPublishingDate().toString()
        );
    }

    public static Book fromDto(BookDTO dto) {
        return new Book(
                dto.getName(),
                dto.getAuthor(),
                Boolean.valueOf(dto.hasNobelPrize),
                LocalDate.parse(dto.datePublished),
                Theme.valueOf(dto.getTheme().toUpperCase().replace(" ", "_"))
        );
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public String getHasNobelPrize() {
        return hasNobelPrize;
    }

    public void setHasNobelPrize(String hasNobelPrize) {
        this.hasNobelPrize = hasNobelPrize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", theme='" + theme + '\'' +
                '}';
    }
}

