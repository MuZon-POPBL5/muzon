package com.muZon.aplicacion.repository;

import com.muZon.aplicacion.entity.Cart;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long>{
    
}
