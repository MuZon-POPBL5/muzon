package com.muZon.aplicacion.service;

import java.util.Optional;

import com.muZon.aplicacion.dto.ChangeAddressForm;
import com.muZon.aplicacion.dto.ChangePasswordForm;
import com.muZon.aplicacion.entity.Address;
import com.muZon.aplicacion.entity.Buy;
import com.muZon.aplicacion.entity.Cart;
import com.muZon.aplicacion.entity.Product;
import com.muZon.aplicacion.entity.User;
import com.muZon.aplicacion.repository.AddressRepository;
import com.muZon.aplicacion.repository.BuyRepository;
import com.muZon.aplicacion.repository.CartRepository;
import com.muZon.aplicacion.repository.ProductRepository;
import com.muZon.aplicacion.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository repository;

	@Autowired
	AddressRepository repositoryAddress;

	@Override
	public Iterable<User> getAllUsers() {
		return repository.findAll();
	}

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	private boolean checkUsernameAvailable(User user) throws Exception {
		Optional<User> userFound = repository.findByUsername(user.getUsername());
		if (userFound.isPresent()) {
			throw new Exception("Username no disponible");
		}
		return true;
	}

	@Override
	public Optional<User> getUserByUsername(String username) {
		return repository.findByUsername(username);
	}

	@Override
	public User createUser(User user) throws Exception {
		if (checkUsernameAvailable(user)) {
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user = repository.save(user);
			Address address = new Address();

			address.setUser(user);
			address.setAddress("Default");
			repositoryAddress.save(address);
		}
		return user;
	}

	@Override
	public User getUserById(Long id) throws Exception {
		return repository.findById(id).orElseThrow(() -> new Exception("El usuario no existe."));
	}

	@Override
	public User updateUser(User fromUser) throws Exception {
		User toUser = getUserById(fromUser.getId());
		mapUser(fromUser, toUser);
		return repository.save(toUser);
	}

	/**
	 * Map everythin but the password.
	 * 
	 * @param from
	 * @param to
	 */
	protected void mapUser(User from, User to) {
		to.setUsername(from.getUsername());
		to.setFirstName(from.getFirstName());
		to.setEmail(from.getEmail());
		to.setRoles(from.getRoles());
	}

	@Override
	public void deleteUser(Long id) throws Exception {
		User user = getUserById(id);
		repository.delete(user);
	}

	@Override
	public User changePassword(ChangePasswordForm form) throws Exception {
		User user = getUserById(form.getId());

		if (user.getPassword().equals(form.getNewPassword())) {
			throw new Exception("Nuevo debe ser diferente al password actual.");
		}

		if (!form.getNewPassword().equals(form.getConfirmPassword())) {
			throw new Exception("Nuevo Password y Current Password no coinciden.");
		}
		String encodedPassword = bCryptPasswordEncoder.encode(form.getNewPassword());
		user.setPassword(encodedPassword);
		return repository.save(user);
	}

	@Override
	public User changePasswordById(Long id, String newPassword, String confirmPassowrd) throws Exception {
		User user = getUserById(id);

		if (user.getPassword().equals(newPassword)) {
			throw new Exception("Nuevo debe ser diferente al password actual.");
		}

		if (!newPassword.equals(confirmPassowrd)) {
			throw new Exception("Nuevo Password y Current Password no coinciden.");
		}
		String encodedPassword = bCryptPasswordEncoder.encode(confirmPassowrd);
		user.setPassword(encodedPassword);
		return repository.save(user);
	}

	@Override
	public User changeEmail(User user, String newEmail) {
		user.setEmail(newEmail);

		return repository.save(user);
	}
}

