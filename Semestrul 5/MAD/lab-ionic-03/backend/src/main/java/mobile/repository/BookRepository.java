package mobile.repository;

import mobile.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE b.userId = :userId " +
            "AND (:title IS NULL OR LOWER(b.name) LIKE LOWER(CAST(:title AS string))) " +
            "AND (:year IS NULL OR b.publishingDate >= :year)")
    Page<Book> findByUserIdAndFilter(
            @Param("userId") String userId,
            @Param("title") String title,
            @Param("year") LocalDate year,
            Pageable pageable
    );
    List<Book> findByUserId(String userId);
}