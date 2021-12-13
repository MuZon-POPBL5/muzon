package com.muZon.aplicacion.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.muZon.aplicacion.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

	public User findByUsername(String username);
	
	public User findByIdAndPassword(Long id, String password);

}