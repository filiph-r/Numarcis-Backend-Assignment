package com.example.user.service;

import com.example.user.model.User;
import com.example.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing custom user details.
 * <p>
 * This service implements {@link UserDetailsService} to load user-specific data during authentication.
 * It also provides methods for saving users and finding users by their username.
 * </p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	/**
	 * Loads the user details by username.
	 * <p>
	 * This method fetches the user from the database and converts the user's roles to GrantedAuthority objects.
	 * </p>
	 *
	 * @param username the username of the user
	 * @return the user details
	 * @throws UsernameNotFoundException if the user is not found
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}

		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}

	/**
	 * Saves a user in the repository.
	 *
	 * @param user the user to save
	 * @return the saved user
	 */
	public User save(User user) {
		return userRepository.save(user);
	}

	/**
	 * Finds a user by their username.
	 *
	 * @param username the username of the user to find
	 * @return the user with the given username, or null if not found
	 */
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
}
