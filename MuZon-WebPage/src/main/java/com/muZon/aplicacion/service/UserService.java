package com.muZon.aplicacion.service;


import java.util.Optional;

import com.muZon.aplicacion.dto.ChangePasswordForm;
import com.muZon.aplicacion.entity.Buy;
import com.muZon.aplicacion.entity.Cart;
import com.muZon.aplicacion.entity.Product;
import com.muZon.aplicacion.entity.User;

public interface UserService {

	public Iterable<User> getAllUsers();

	public User createUser(User user) throws Exception;

	public User getUserById(Long id) throws Exception;
	
	public User updateUser(User user) throws Exception;
	
	public void deleteUser(Long id) throws Exception;
	
	public User changePassword(ChangePasswordForm form) throws Exception;

	public User changePasswordById(Long id, String newPassword, String confirmPassowrd) throws Exception;

	public Product addProduct(User seller, Product product, String category) throws Exception;

	public void save(String bytes);

	public User changeEmail(User user, String newEmail);

    public Cart addToCart(Product productToSave, Integer quantity, Optional<User> user);

	public Buy addBuyNow(Product productToSave, Integer quantity, Optional<User> user);

    public Optional<User> getUserByUsername(String username);
}
