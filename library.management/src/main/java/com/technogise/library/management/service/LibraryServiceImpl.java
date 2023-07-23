package com.technogise.library.management.service;

import java.util.List;

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

	@Override
	public List<Book> getAllBooks() {
		return libraryRepository.findAllByActive(true);
	}

	@Override
	public Book addBook(Book book) {
		return libraryRepository.save(book);
	}

	@Override
	public boolean borrowBook(String userId, String bookTitle) throws LibraryException {

		Boolean borrowedStatus = false;

		if (getBorrowedBooksCount(userId) >= 2) {
			throw new LibraryException("Borrowing limit exceed");
		}

		if (isBookBorrowedByUser(userId, bookTitle)) {
			throw new LibraryException("Book already borrowed");
		}

		List<Book> bookByTitle = libraryRepository.findByTitleAndActive(bookTitle, true);
		if (bookByTitle.size() > 0) {
			BorrowedBook borrowedBook = new BorrowedBook();
			borrowedBook.setUserId(userId);
			borrowedBook.setBookTitle(bookTitle);
			borrowedBookRepository.save(borrowedBook);

			bookByTitle.get(0).setActive(false);
			libraryRepository.save(bookByTitle.get(0));

			borrowedStatus = true;
		} else {
			throw new LibraryException("Book not present");
		}

		return borrowedStatus;
	}

	@Override
	public boolean returnBook(String userId, String bookTitle) throws LibraryException {
		Boolean returnStatus = false;
		if (!isBookBorrowedByUser(userId, bookTitle)) {
			throw new LibraryException("Book not borrowed by a user");
		}

		borrowedBookRepository.deleteByUserIdAndBookTitle(userId, bookTitle);
		List<Book> bookByTitle = libraryRepository.findByTitleAndActive(bookTitle, false);
		if (bookByTitle.size() > 0) {
			bookByTitle.get(0).setActive(true);
			libraryRepository.save(bookByTitle.get(0));
		}
		returnStatus = true;
		return returnStatus;
	}

	@Override
	public List<BorrowedBook> getBorrowedBooksByUser(String userId) {
		List<BorrowedBook> borrowedBooks = borrowedBookRepository.findByUserId(userId);
		return borrowedBooks;
	}

	public int getBorrowedBooksCount(String userId) {
		return (int) borrowedBookRepository.countByUserId(userId);
	}

	public boolean isBookBorrowedByUser(String userId, String bookTitle) {
		return borrowedBookRepository.existsByUserIdAndBookTitle(userId, bookTitle);
	}
}

