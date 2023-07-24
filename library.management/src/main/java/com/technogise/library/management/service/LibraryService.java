/**
* LibraryService
*
* @author  Ravina Motwani
* @version 1.0
* @since   2023-07-25
*/
package com.technogise.library.management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.technogise.library.management.entity.Book;
import com.technogise.library.management.entity.BorrowedBook;
import com.technogise.library.management.exception.LibraryException;

@Service
public interface LibraryService {

	/**
	 * This method is used to get all books from the library
	 * 
	 * @return List of {@link Book}
	 */
	public List<Book> getAllBooks();

	/**
	 * This method is used to add book in the library
	 * 
	 * @param book - {@link Book}
	 * @return {@link Book}
	 */
	public Book addBook(Book book);

	/**
	 * This method is used to borrow book from the library
	 * 
	 * @param userId-    id of the user
	 * @param bookTitle- title of the book
	 * @return {@link Boolean}
	 * @throws LibraryException
	 */
	public boolean borrowBook(String userId, String bookTitle) throws LibraryException;

	/**
	 * This method is used to return book to the library
	 * 
	 * @param userId-    id of the user
	 * @param bookTitle- title of the book
	 * @return {@link Boolean}
	 * @throws LibraryException
	 */
	public boolean returnBook(String userId, String bookTitle) throws LibraryException;

	/**
	 * This method is used to view the book borrowed from the library
	 * 
	 * @param userId- id of the user
	 * @return List of {@link BorrowedBook}
	 */
	public List<BorrowedBook> getBorrowedBooksByUser(String userId);

}
