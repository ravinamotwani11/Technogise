package com.technogise.library.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technogise.library.management.entity.BorrowedBook;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {
    List<BorrowedBook> findByUserId(String userId);

    long countByUserId(String userId);

    long countByBookTitle(String bookTitle); 

    boolean existsByUserIdAndBookTitle(String userId, String bookTitle);

    void deleteByUserIdAndBookTitle(String userId, String bookTitle);
}
