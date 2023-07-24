/**
* LibraryService Implementation
*
* @author  Ravina Motwani
* @version 1.0
* @since   2023-07-25
*/
package com.technogise.library.management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.technogise.library.management.entity.Book;
import com.technogise.library.management.entity.BorrowedBook;
import com.technogise.library.management.exception.LibraryException;
import com.technogise.library.management.repository.BorrowedBookRepository;
import com.technogise.library.management.repository.LibraryRepository;

@Service
public class LibraryServiceImpl implements LibraryService {
	private LibraryRepository libraryRepository;
	private BorrowedBookRepository borrowedBookRepository;

	@Autowired
	public LibraryServiceImpl(LibraryRepository libraryRepository, BorrowedBookRepository borrowedBookRepository) {
		this.libraryRepository = libraryRepository;
		this.borrowedBookRepository = borrowedBookRepository;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Book> getAllBooks() {
		return libraryRepository.findAllByActive(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Book addBook(Book book) {
		return libraryRepository.save(book);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean borrowBook(String userId, String bookTitle) throws LibraryException {

		Boolean borrowedStatus = false;

		// Check if user has borrowed more than 2 books
		if (getBorrowedBooksCount(userId) >= 2) {
			throw new LibraryException("Borrowing limit exceed");
		}

		// Check if user has already borrowed a copy of book
		if (isBookBorrowedByUser(userId, bookTitle)) {
			throw new LibraryException("Book already borrowed");
		}

		List<Book> bookByTitle = libraryRepository.findByTitleAndActive(bookTitle, true);
		if (bookByTitle.size() > 0) {
			BorrowedBook borrowedBook = new BorrowedBook();
			borrowedBook.setUserId(userId);
			borrowedBook.setBookTitle(bookTitle);
			borrowedBook.setActive(true);
			borrowedBook.setBookId(bookByTitle.get(0).getId());
			borrowedBookRepository.save(borrowedBook);

			// Remove borrowed book from the library
			bookByTitle.get(0).setActive(false);
			libraryRepository.save(bookByTitle.get(0));

			borrowedStatus = true;
		} else {
			throw new LibraryException("Book not present");
		}

		return borrowedStatus;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean returnBook(String userId, String bookTitle) throws LibraryException {
		Boolean returnStatus = false;

		// Check if user borrowed a book to return
		if (!isBookBorrowedByUser(userId, bookTitle)) {
			throw new LibraryException("Book not borrowed by a user");
		}

		Optional<BorrowedBook> borrowedBook = borrowedBookRepository.findByUserIdAndBookTitleAndActive(userId,
				bookTitle, true);
		if (borrowedBook.isPresent()) {

			borrowedBook.get().setActive(false);

			Optional<Book> findBookById = libraryRepository.findById(borrowedBook.get().getBookId());
			if (findBookById.isPresent()) {

				// Add returned book in the library
				findBookById.get().setActive(true);
				libraryRepository.save(findBookById.get());
			} else {
				return returnStatus;
			}
			returnStatus = true;
		}
		return returnStatus;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BorrowedBook> getBorrowedBooksByUser(String userId) {
		List<BorrowedBook> borrowedBooks = borrowedBookRepository.findByUserIdAndActive(userId, true);
		return borrowedBooks;
	}

	/**
	 * This method is used to get the count of book borrowed by a user
	 * 
	 * @param userId- id of a user
	 * @return count in integer
	 */
	public int getBorrowedBooksCount(String userId) {
		return (int) borrowedBookRepository.countByUserId(userId);
	}

	/**
	 * This method is used to check if book is borrowed by a user or not
	 * 
	 * @param userId-    id of a user
	 * @param bookTitle- title of the book
	 * @return {@link Boolean}
	 */
	public boolean isBookBorrowedByUser(String userId, String bookTitle) {
		return borrowedBookRepository.existsByUserIdAndBookTitleAndActive(userId, bookTitle, true);
	}
}

