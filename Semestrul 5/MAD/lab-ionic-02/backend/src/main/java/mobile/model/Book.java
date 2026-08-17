package mobile.model;

import jakarta.persistence.*; // 1. Import JPA
import java.time.LocalDate;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String author;
    private Boolean stars;

    @Column(name = "publishing_date")
    private LocalDate publishingDate;

    @Column(name = "user_id")
    private String userId;
    public Book() {
    }

    public Book(String name, String author, Boolean stars, LocalDate publishingDate, String userId) {
        this.name = name;
        this.author = author;
        this.stars = stars;
        this.publishingDate = publishingDate;
        this.userId = userId;
    }

    public Book(Long id, String name, String author, Boolean stars, LocalDate publishingDate, String userId) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.stars = stars;
        this.publishingDate = publishingDate;
        this.userId = userId;
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

    public Boolean getStars() {
        return stars;
    }

    public void setStars(Boolean stars) {
        this.stars = stars;
    }

    public LocalDate getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(LocalDate publishingDate) {
        this.publishingDate = publishingDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", stars=" + stars +
                ", publishingDate=" + publishingDate +
                ", userId='" + userId + '\'' +
                '}';
    }
}