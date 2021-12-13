package com.muZon.aplicacion.service;

import com.muZon.aplicacion.entity.User;

public interface UserService {

	public Iterable<User> getAllUsers();

    public User createUser(User formUser) throws Exception;
		
}