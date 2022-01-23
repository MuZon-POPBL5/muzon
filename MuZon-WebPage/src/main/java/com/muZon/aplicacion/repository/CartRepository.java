package com.muZon.aplicacion.repository;


import javax.transaction.Transactional;

import com.muZon.aplicacion.entity.Cart;
import com.muZon.aplicacion.entity.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface CartRepository extends CrudRepository<Cart, Long>{

    void deleteByBoughtId(User user);

}
