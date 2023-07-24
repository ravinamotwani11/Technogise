/**
* LibraryServiceTest
*
* @author  Ravina Motwani
* @version 1.0
* @since   2023-07-25
*/
package com.technogise.library.management.test.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.technogise.library.management.entity.Book;
import com.technogise.library.management.entity.BorrowedBook;
import com.technogise.library.management.exception.LibraryException;
import com.technogise.library.management.repository.BorrowedBookRepository;
import com.technogise.library.management.repository.LibraryRepository;
import com.technogise.library.management.service.LibraryServiceImpl;

public class LibraryServiceTest {

	@Mock
	private LibraryRepository libraryRepository;

	@Mock
	private BorrowedBookRepository borrowedBookRepository;

	@InjectMocks
	private LibraryServiceImpl libraryService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetAllBooks() {
		// arrange
		List<Book> books = Arrays.asList(new Book(1L, "Book 1", "Author 1"), new Book(2L, "Book 2", "Author 2"));

		Mockito.when(libraryRepository.findAllByActive(true)).thenReturn(books);

		// act
		List<Book> result = libraryService.getAllBooks();

		// assert
		assertEquals(2, result.size());
	}

	@Test
	public void testAddBook() {
		// arrange
		Book bookToAdd = new Book(1L, "Book 1", "Author 1");
		Mockito.when(libraryRepository.save(any(Book.class))).thenReturn(bookToAdd);

		// act
		Book result = libraryService.addBook(bookToAdd);

		// assert
		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("Book 1", result.getTitle());
		assertEquals("Author 1", result.getAuthor());
	}

	@Test
	public void testBorrowBook_SuccessfulBorrow() throws LibraryException {
		// arrange
		String userId = "userId";
		List<Book> bookByTitle = Arrays.asList(new Book(3L, "Book 3", "Author 1"));

		Mockito.when(borrowedBookRepository.existsByUserIdAndBookTitleAndActive(userId, "Book 1", true))
				.thenReturn(true);
		Mockito.when(borrowedBookRepository.countByUserId(userId)).thenReturn(1L);

		Mockito.when(libraryRepository.findByTitleAndActive("Book 3", true)).thenReturn(bookByTitle);
		Mockito.when(borrowedBookRepository.save(any(BorrowedBook.class)))
				.thenReturn(new BorrowedBook(2L, "userId", "Book 1", 2L, true));

		// assert
		boolean result = libraryService.borrowBook(userId, "Book 3");

		// assert
		assertTrue(result);
	}

	@Test
	public void testBorrowBook_BorrowLimitExceed() {
		// arrange
		String userId = "userId";
		String bookTitle = "Book 1";

		Mockito.when(borrowedBookRepository.countByUserId(userId)).thenReturn(1L);

		// act and assert
		assertThrows(LibraryException.class, () -> libraryService.borrowBook(userId, bookTitle));
	}

	@Test
	public void testBorrowBook_BorrowLimitExceedByThree() {
		// arrange
		String userId = "userId";
		String bookTitle = "Book 1";

		Mockito.when(borrowedBookRepository.countByUserId(userId)).thenReturn(3L);

		// act and assert
		assertThrows(LibraryException.class, () -> libraryService.borrowBook(userId, bookTitle));
	}

	@Test
	public void testBorrowBook_BookAlreadyBorrowed() {
		// arrange
		String userId = "userId";
		String bookTitle = "Book 1";

		Mockito.when(borrowedBookRepository.countByUserId(userId)).thenReturn(1L);
		Mockito.when(libraryService.isBookBorrowedByUser(userId, bookTitle)).thenReturn(true);

		// act and assert
		assertThrows(LibraryException.class, () -> libraryService.borrowBook(userId, bookTitle));
	}

	@Test
	public void testBorrowBook_BookNotPresent() {
		// arrange
		String userId = "userId";
		String bookTitle = "Book 1";

		Mockito.when(borrowedBookRepository.countByUserId(userId)).thenReturn(1L);
		Mockito.when(libraryService.isBookBorrowedByUser(userId, bookTitle)).thenReturn(false);
		Mockito.when(libraryRepository.findByTitleAndActive(bookTitle, true)).thenReturn(Arrays.asList());

		// act and assert
		assertThrows(LibraryException.class, () -> libraryService.borrowBook(userId, bookTitle));
	}

	@Test
	public void testReturnBook_SuccessfulReturn() throws LibraryException {
		// arrange
		String userId = "1";
		String bookTitle = "Book 1";
		BorrowedBook borrowedBook = new BorrowedBook(1L, "1", "Book 1", 1L, true);

		Mockito.when(borrowedBookRepository.countByUserId(userId)).thenReturn(1L);
		Mockito.when(borrowedBookRepository.existsByUserIdAndBookTitleAndActive(userId, bookTitle, true))
				.thenReturn(true);
		Mockito.when(borrowedBookRepository.findByUserIdAndBookTitleAndActive(userId, bookTitle, true))
				.thenReturn(Optional.of(borrowedBook));
		Mockito.when(libraryRepository.findById(borrowedBook.getBookId()))
				.thenReturn(Optional.of(new Book(1L, "Book 1", "Author 1")));
		Mockito.when(libraryRepository.save(any(Book.class))).thenReturn(new Book(1L, "Book 1", "Author 1"));

		// act
		boolean result = libraryService.returnBook(userId, bookTitle);

		// assert
		assertTrue(result);
	}
	
	@Test
	public void testReturnBook_WhenUserWithBookTitleNotPresent() throws LibraryException {
		// arrange
		String userId = "1";
		String bookTitle = "Book 1";

		Mockito.when(borrowedBookRepository.countByUserId(userId)).thenReturn(1L);
		Mockito.when(borrowedBookRepository.existsByUserIdAndBookTitleAndActive(userId, bookTitle, true))
				.thenReturn(true);
		Mockito.when(borrowedBookRepository.findByUserIdAndBookTitleAndActive(userId, bookTitle, true))
				.thenReturn(Optional.ofNullable(null));

		// act
		boolean result = libraryService.returnBook(userId, bookTitle);

		// assert
		assertFalse(result);
	}
	
	@Test
	public void testReturnBook_WhenBookNotPresentInLibrary() throws LibraryException {
		// arrange
		String userId = "1";
		String bookTitle = "Book 1";
		BorrowedBook borrowedBook = new BorrowedBook(1L, "1", "Book 1", 1L, true);

		Mockito.when(borrowedBookRepository.countByUserId(userId)).thenReturn(1L);
		Mockito.when(borrowedBookRepository.existsByUserIdAndBookTitleAndActive(userId, bookTitle, true))
				.thenReturn(true);
		Mockito.when(borrowedBookRepository.findByUserIdAndBookTitleAndActive(userId, bookTitle, true))
				.thenReturn(Optional.of(borrowedBook));
		Mockito.when(libraryRepository.findById(borrowedBook.getBookId()))
				.thenReturn(Optional.ofNullable(null));

		// act
		boolean result = libraryService.returnBook(userId, bookTitle);

		// assert
		assertFalse(result);
	}

	@Test
	public void testReturnBook_BookNotBorrowedByUser() {
		// arrange
		String userId = "userId";
		String bookTitle = "Book 1";

		Mockito.when(libraryService.isBookBorrowedByUser(userId, bookTitle)).thenReturn(false);

		// act and assert
		assertThrows(LibraryException.class, () -> libraryService.returnBook(userId, bookTitle));
	}

	@Test
	public void testGetBorrowedBooksByUser() {
		// arrange
		String userId = "2";
		List<BorrowedBook> borrowedBooks = Arrays.asList(new BorrowedBook(1L, "2", "book", 1L, true),
				new BorrowedBook(2L, "2", "book", 1L, true));
		Mockito.when(borrowedBookRepository.findByUserIdAndActive(userId, true)).thenReturn(borrowedBooks);

		// act
		List<BorrowedBook> result = libraryService.getBorrowedBooksByUser(userId);

		// assert
		assertEquals(2, result.size());
	}

	@Test
	public void testGetBorrowedBooksCount() {
		// arrange
		String userId = "userId";
		Mockito.when(borrowedBookRepository.countByUserId(userId)).thenReturn(2L);

		// act
		int result = libraryService.getBorrowedBooksCount(userId);

		// assert
		assertEquals(2, result);
	}

	@Test
	public void testIsBookBorrowedByUser() {
		// arrange
		String userId = "userId";
		String bookTitle = "Book 1";
		Mockito.when(borrowedBookRepository.existsByUserIdAndBookTitleAndActive(userId, bookTitle, true))
				.thenReturn(true);

		// act
		boolean result = libraryService.isBookBorrowedByUser(userId, bookTitle);

		// assert
		assertTrue(result);
	}
}