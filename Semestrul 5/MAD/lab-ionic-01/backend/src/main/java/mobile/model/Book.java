package mobile.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String author;

    @Column(name = "has_nobel_prize", nullable = false)
    private Boolean hasNobelPrize;

    @Column(name = "publishing_date")
    private LocalDate publishingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Theme theme;


    public Book() {
    }

    public Book(String name, String author, Boolean hasNobelPrize, LocalDate publishingDate, Theme theme) {
        this.name = name;
        this.author = author;
        this.hasNobelPrize = hasNobelPrize;
        this.publishingDate = publishingDate;
        this.theme = theme;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public Boolean getHasNobelPrize() {
        return hasNobelPrize;
    }

    public void setHasNobelPrize(Boolean hasNobelPrize) {
        this.hasNobelPrize = hasNobelPrize;
    }

    public LocalDate getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(LocalDate publishingDate) {
        this.publishingDate = publishingDate;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id + // Include ID in toString
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", theme=" + theme +
                '}';
    }
}