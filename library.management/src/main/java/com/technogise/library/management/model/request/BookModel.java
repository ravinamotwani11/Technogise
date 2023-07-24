/**
* BookModel
*
* @author  Ravina Motwani
* @version 1.0
* @since   2023-07-25
*/
package com.technogise.library.management.model.request;

public class BookModel {

	private String userId;
	private String bookTitle;

	// Constructors
	public BookModel() {

	}

	public BookModel(String userId, String bookTitle) {
		super();
		this.userId = userId;
		this.bookTitle = bookTitle;
	}

	// Getters and setters
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

}
