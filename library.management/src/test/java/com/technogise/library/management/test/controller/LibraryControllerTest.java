/**
* LibraryControllerTest
*
* @author  Ravina Motwani
* @version 1.0
* @since   2023-07-25
*/
package com.technogise.library.management.test.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.technogise.library.management.controller.LibraryController;
import com.technogise.library.management.entity.Book;
import com.technogise.library.management.entity.BorrowedBook;
import com.technogise.library.management.exception.LibraryException;
import com.technogise.library.management.service.LibraryService;

public class LibraryControllerTest {

	@Mock
	private LibraryService libraryService;

	@InjectMocks
	private LibraryController libraryController;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(libraryController).build();
	}

	@Test
	public void testViewBooks() throws Exception {
		// arrange
		List<Book> books = Arrays.asList(new Book(1L, "Book 1", "Author 1"), new Book(2L, "Book 2", "Author 2"));

		when(libraryService.getAllBooks()).thenReturn(books);

		// act
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/library/books").accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		// assert
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	public void testAddBook() throws Exception {
		// arrange
		Book bookToAdd = new Book(1L, "Book 1", "Author 1");
		when(libraryService.addBook(any(Book.class))).thenReturn(bookToAdd);

		// act
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/library/add")
				.content("{\"id\":\"1\",\"title\":\"Book 1\",\"author\":\"Author 1\"}")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		// assert
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	public void testBorrowBook_SuccessfulBorrow() throws Exception {
		// arrange
		when(libraryService.borrowBook(anyString(), anyString())).thenReturn(true);

		// act
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/library/borrow")
				.content("{\"userId\":\"userId\",\"bookTitle\":\"Book 1\"}").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		// assert
		assertEquals(200, result.getResponse().getStatus());
	}
	
	@Test
	public void testBorrowBook_NotSuccessfulBorrow() throws Exception {
		// arrange
		when(libraryService.borrowBook(anyString(), anyString())).thenReturn(false);

		// act
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/library/borrow")
				.content("{\"userId\":\"userId\",\"bookTitle\":\"Book 1\"}").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		// assert
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	public void testBorrowBook_BorrowLimitExceed() throws Exception {
		// arrange
		when(libraryService.borrowBook(anyString(), anyString()))
				.thenThrow(new LibraryException("Borrowing limit exceed"));

		// act
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/library/borrow")
				.content("{\"userId\":\"userId\",\"bookTitle\":\"Book 1\"}").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		// assert
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	public void testBorrowBook_BookAlreadyBorrowed() throws Exception {
		// arrange
		when(libraryService.borrowBook(anyString(), anyString()))
				.thenThrow(new LibraryException("Book already borrowed"));

		// act
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/library/borrow")
				.content("{\"userId\":\"userId\",\"bookTitle\":\"Book 1\"}").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		// assert
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	public void testReturnBook_SuccessfulReturn() throws Exception {
		// arrange
		when(libraryService.returnBook(anyString(), anyString())).thenReturn(true);

		// act
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/library/return")
				.content("{\"userId\":\"userId\",\"bookTitle\":\"Book 1\"}").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		// assert
		assertEquals(200, result.getResponse().getStatus());
	}
	
	@Test
	public void testReturnBook_NotSuccessfulReturn() throws Exception {
		// arrange
		when(libraryService.returnBook(anyString(), anyString())).thenReturn(false);

		// act
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/library/return")
				.content("{\"userId\":\"userId\",\"bookTitle\":\"Book 1\"}").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		// assert
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	public void testReturnBook_BookNotBorrowedByUser() throws Exception {
		// arrange
		when(libraryService.returnBook(anyString(), anyString()))
				.thenThrow(new LibraryException("Book not borrowed by a user"));

		// act
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/library/return")
				.content("{\"userId\":\"userId\",\"bookTitle\":\"Book 1\"}").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		// assert
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	public void testViewBorrowedBooks() throws Exception {
		// arrange
		String userId = "userId";
		List<BorrowedBook> borrowedBooks = Arrays.asList(new BorrowedBook(1L, "userId", "Book 1", 1L, true),
				new BorrowedBook(2L, "userId", "Book 2", 2L, true));

		when(libraryService.getBorrowedBooksByUser(userId)).thenReturn(borrowedBooks);

		// act
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/library/borrowedBooks").param("userId", userId)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		// assert
		assertEquals(200, result.getResponse().getStatus());
	}

}