/**
* LibraryRepository
*
* @author  Ravina Motwani
* @version 1.0
* @since   2023-07-25
*/
package com.technogise.library.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technogise.library.management.entity.Book;

@Repository
public interface LibraryRepository extends JpaRepository<Book, Long> {

	/**
	 * This method is used to get list of Books by title and active
	 * 
	 * @param bookTitle
	 * @param active
	 * @return List of {@link Book}
	 */
	List<Book> findByTitleAndActive(String bookTitle, Boolean active);

	/**
	 * This method is used to find all active books
	 * 
	 * @param active
	 * @return List of {@link Book}
	 */
	List<Book> findAllByActive(Boolean active);

}


