package com.muZon.aplicacion.service;


import javax.validation.Valid;

import com.muZon.aplicacion.dto.ChangeAddressForm;
import com.muZon.aplicacion.dto.ChangePasswordForm;
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

	public User changeAddress(ChangeAddressForm form) throws Exception;

	public Product addProduct(User seller, Product product) throws Exception;
}
