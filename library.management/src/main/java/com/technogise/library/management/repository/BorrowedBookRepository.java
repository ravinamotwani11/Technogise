/**
* BorrowedBookRepository
*
* @author  Ravina Motwani
* @version 1.0
* @since   2023-07-25
*/
package com.technogise.library.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technogise.library.management.entity.BorrowedBook;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {

	/**
	 * This method is used to find BorrowedBook by user id and active
	 * 
	 * @param userId
	 * @param active
	 * @return {@link BorrowedBook}
	 */
	List<BorrowedBook> findByUserIdAndActive(String userId, Boolean active);

	/**
	 * This method is used to count BorrowedBook by user id
	 * 
	 * @param userId
	 * @return count in long
	 */
	long countByUserId(String userId);

	/**
	 * This method is used to check if book borrowed by user id, title and active
	 * 
	 * @param userId
	 * @param bookTitle
	 * @param active
	 * @return Boolean flag
	 */
	boolean existsByUserIdAndBookTitleAndActive(String userId, String bookTitle, Boolean active);

	/**
	 * This method is used to get BorrowedBook by user id, title and active
	 * 
	 * @param userId
	 * @param bookTitle
	 * @param active
	 * @return Optional of {@link BorrowedBook}
	 */
	Optional<BorrowedBook> findByUserIdAndBookTitleAndActive(String userId, String bookTitle, Boolean active);
}
