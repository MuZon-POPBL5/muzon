package com.muZon.aplicacion.service;

import java.util.Optional;

import com.muZon.aplicacion.entity.Buy;
import com.muZon.aplicacion.entity.Cart;
import com.muZon.aplicacion.entity.Product;
import com.muZon.aplicacion.entity.User;

import com.muZon.aplicacion.entity.Buy;
import com.muZon.aplicacion.entity.Cart;
import com.muZon.aplicacion.entity.Product;

public interface ProductService {

    public Product addProduct(User seller, Product product, String category) throws Exception;

	public void save(String bytes);

    public Cart addToCart(Product productToSave, Integer quantity, Optional<User> user);

	public Buy addBuyNow(Product productToSave, Integer quantity, Optional<User> user);
    
}
