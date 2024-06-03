package com.example.user.repository;

import com.example.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for {@link User} instances.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations for User entities.
 * It also includes a custom method to find a user by their username.
 * </p>
 */
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Finds a user by their username.
	 *
	 * @param username the username of the user to find
	 * @return the user with the given username, or null if not found
	 */
	User findByUsername(String username);
}
