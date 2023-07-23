package com.technogise.library.management.controller;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.technogise.library.management.entity.Book;
import com.technogise.library.management.entity.BorrowedBook;
import com.technogise.library.management.exception.LibraryException;
import com.technogise.library.management.service.LibraryService;
import com.technogise.library.management.service.LoggerService;

@RestController
@RequestMapping("/library")
public class LibraryController {
	private final LibraryService libraryService;
	private final Logger logger;

	@Autowired
	public LibraryController(LibraryService libraryService, LoggerService loggerService) {
		this.libraryService = libraryService;
		this.logger = loggerService.getLogger(LibraryController.class);
	}

	@GetMapping("/books")
	public List<Book> viewBooks() {
		logger.traceEntry("Received request for viewBooks");
		return logger.traceExit("Exit from viewBooks() with respose: {]", libraryService.getAllBooks());
	}

	@PostMapping("/add")
	public Book addBook(@RequestBody Book book) {
		logger.traceEntry("Received request to addBook(book={})", book);
		return logger.traceExit("Exit from addBook() with respose: {]", libraryService.addBook(book));
	}

	@PostMapping("/borrow")
	public String borrowBook(@RequestParam String userId, @RequestParam String bookTitle) throws LibraryException {
		logger.traceEntry("Received request to borrowBook(userId={}, bookTitle={}): {}", userId, bookTitle);
		try {
			boolean bookBorrowed = libraryService.borrowBook(userId, bookTitle);
			if (bookBorrowed) {
				return logger.traceExit("Exit from borrowBook() with respose: {}", "Book borrowed successfully");
			} else {
				return logger.traceExit("Exit from borrowBook() with respose: {}", "Book not borrowed successfully");
			}
		} catch (LibraryException ex) {
			return logger.traceExit("Exit from borrowBook() with catch respose: {}", ex.getMessage());
		}
	}

	@PostMapping("/return")
	public String returnBook(@RequestParam String userId, @RequestParam String bookTitle) throws LibraryException {
		logger.traceEntry("Received request to returnBook(userId={}, bookTitle={}): {}", userId, bookTitle);
		try {
			boolean bookReturned = libraryService.returnBook(userId, bookTitle);
			if (bookReturned) {
				return logger.traceExit("Exit from returnBook() with respose: {}", "Book returned successfully");
			} else {
				return logger.traceExit("Exit from returnBook() with respose: {}", "Book not returned successfully");
			}
		} catch (LibraryException ex) {
			return logger.traceExit("Exit from returnBook() with catch respose: {}", ex.getMessage());
		}
	}

	@GetMapping("/borrowedBooks")
	public List<BorrowedBook> viewBorrowedBooks(@RequestParam String userId) {
		logger.traceEntry("Received request to viewBorrowedBooks(userId={})", userId);
		return logger.traceExit("Exit from viewBorrowedBooks() with response: {}",
				libraryService.getBorrowedBooksByUser(userId));
	}
}
