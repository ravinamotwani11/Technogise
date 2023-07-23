package com.technogise.library.management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.technogise.library.management.entity.Book;
import com.technogise.library.management.entity.BorrowedBook;
import com.technogise.library.management.exception.LibraryException;

@Service
public interface LibraryService {

	public Book addBook(Book book);
	public List<Book> getAllBooks();
	public boolean borrowBook(String userId, String bookTitle) throws LibraryException;
	public boolean returnBook(String userId, String bookTitle) throws LibraryException;
	public List<BorrowedBook> getBorrowedBooksByUser(String userId);
	
}
