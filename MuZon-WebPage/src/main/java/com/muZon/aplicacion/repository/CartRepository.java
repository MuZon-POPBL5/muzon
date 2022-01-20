package com.muZon.aplicacion.repository;

import java.util.List;

import com.muZon.aplicacion.entity.Cart;
import com.muZon.aplicacion.entity.Product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long>{

}
