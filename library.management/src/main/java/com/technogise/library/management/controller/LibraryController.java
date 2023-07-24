/**
* LibraryController
*
* @author  Ravina Motwani
* @version 1.0
* @since   2023-07-25
*/
package com.technogise.library.management.controller;

import java.util.List;

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
import com.technogise.library.management.model.request.BookModel;
import com.technogise.library.management.service.LibraryService;

@RestController
@RequestMapping("/library")
public class LibraryController {
	private final LibraryService libraryService;

	@Autowired
	public LibraryController(LibraryService libraryService) {
		this.libraryService = libraryService;
	}

	/**
	 * This endpoint is used to view all books present in the library
	 * 
	 * @return List of {@link Book}
	 */
	@GetMapping("/books")
	public List<Book> viewBooks() {
		return libraryService.getAllBooks();
	}

	/**
	 * This endpoint is used to add book in the library
	 * 
	 * @param book- {@link Book}
	 * @return {@link Book}
	 */
	@PostMapping("/add")
	public Book addBook(@RequestBody Book book) {
		return libraryService.addBook(book);
	}

	/**
	 * This endpoint is used to borrow book from the library
	 * 
	 * @param bookModel- {@link BookModel}
	 * @return Status of book borrowed
	 */
	@PostMapping("/borrow")
	public String borrowBook(@RequestBody BookModel bookModel) throws LibraryException {
		try {
			boolean bookBorrowed = libraryService.borrowBook(bookModel.getUserId(), bookModel.getBookTitle());
			if (bookBorrowed) {
				return "Book borrowed successfully";
			} else {
				return "Book not borrowed successfully";
			}
		} catch (LibraryException ex) {
			return ex.getMessage();
		}
	}

	/**
	 * This endpoint is used to return book to the library
	 * 
	 * @param bookModel- {@link BookModel}
	 * @return Status of book returned
	 */
	@PostMapping("/return")
	public String returnBook(@RequestBody BookModel bookModel) throws LibraryException {
		try {
			boolean bookReturned = libraryService.returnBook(bookModel.getUserId(), bookModel.getBookTitle());
			if (bookReturned) {
				return "Book returned successfully";
			} else {
				return "Book not returned successfully";
			}
		} catch (LibraryException ex) {
			return ex.getMessage();
		}
	}

	/**
	 * This endpoint is used to view the book borrowed from the library
	 * 
	 * @param userId- Id of the user
	 * @return List of {@link BorrowedBook}
	 */
	@GetMapping("/borrowedBooks")
	public List<BorrowedBook> viewBorrowedBooks(@RequestParam String userId) {
		return libraryService.getBorrowedBooksByUser(userId);
	}
}
