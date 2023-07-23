/**
* Book Model
*
* @author  Ravina Motwani
* @version 1.0
* @since   2023-07-23
*/
package com.technogise.library.management.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String author;
	private Boolean active = true;
	private Integer count;

	// Constructors
	public Book() {
		
	}
	public Book(Long id, String title, String author, Integer count) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.count = count;
	}

	// Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active=active;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
}
