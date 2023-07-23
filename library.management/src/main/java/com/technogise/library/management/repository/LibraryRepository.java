package com.technogise.library.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technogise.library.management.entity.Book;

@Repository
public interface LibraryRepository extends JpaRepository<Book, Long> {

	int countById(Long bookId);

	List<Book> findByTitleAndActive(String bookTitle, Boolean active);

	List<Book> findAllByActive(Boolean active);
}


