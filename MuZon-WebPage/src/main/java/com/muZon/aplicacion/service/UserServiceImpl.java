package com.muZon.aplicacion.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import com.muZon.aplicacion.dto.ChangeAddressForm;
import com.muZon.aplicacion.dto.ChangePasswordForm;
import com.muZon.aplicacion.entity.Product;
import com.muZon.aplicacion.entity.User;
import com.muZon.aplicacion.repository.ProductRepository;
import com.muZon.aplicacion.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	byte[] imgSrc;

	@Autowired
	UserRepository repository;

	@Autowired
	ProductRepository repositoryProduct;

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
	public User createUser(User user) throws Exception {
		if (checkUsernameAvailable(user)) {
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user = repository.save(user);
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
	public User changeAddress(ChangeAddressForm form) throws Exception {
		User user = getUserById(form.getId());

		user.setAddress(form.getNewAddress());
		// user.setAddress("c " + form.getNewAddress() + form.getNewCity() + ", " +
		// form.getNewZipCode() + ", " + form.getNewCountry());
		return repository.save(user);
	}

	private boolean isLoggedUserADMIN() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails loggedUser = null;
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;

			loggedUser.getAuthorities().stream()
					.filter(x -> "ADMIN".equals(x.getAuthority()))
					.findFirst().orElse(null); // loggedUser = null;
		}
		return loggedUser != null ? true : false;
	}

	@Override
	public Product addProduct(User seller, Product product) throws Exception {

		Product newProduct = new Product();
		newProduct.setName(product.getName());
		newProduct.setDescription(product.getDescription());
		newProduct.setPrice(product.getPrice());
		newProduct.setImgSrc(this.imgSrc);
		newProduct.setQuantity(product.getQuantity());
		Optional<User> sellerData = repository.findById(seller.getId());
		newProduct.setSellerId(sellerData.get());
		 
		return repositoryProduct.save(newProduct);
	}

	@Override
	public void save(byte[] bytes) {
		this.imgSrc = bytes;
	}

	@Override
	public User changeEmail(User user, String newEmail) {
		user.setEmail(newEmail);

		return repository.save(user);
	}
}
