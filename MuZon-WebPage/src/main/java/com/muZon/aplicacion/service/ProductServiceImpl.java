package com.muZon.aplicacion.service;

import java.util.Optional;

import com.muZon.aplicacion.entity.Buy;
import com.muZon.aplicacion.entity.Cart;
import com.muZon.aplicacion.entity.Product;
import com.muZon.aplicacion.entity.User;
import com.muZon.aplicacion.repository.BuyRepository;
import com.muZon.aplicacion.repository.CartRepository;
import com.muZon.aplicacion.repository.ProductRepository;
import com.muZon.aplicacion.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{

    String imgSrc;

    @Autowired
	UserRepository repository;

    @Autowired
	ProductRepository repositoryProduct;

	@Autowired
	CartRepository repositoryCart;

	@Autowired
	BuyRepository repositoryBuy;

    @Override
	public Product addProduct(User seller, Product product, String category) throws Exception {

		Product newProduct = new Product();
		newProduct.setName(product.getName());
		newProduct.setDescription(product.getDescription());
		newProduct.setPrice(product.getPrice());
		newProduct.setImgSrc(this.imgSrc);
		newProduct.setQuantity(product.getQuantity());
		newProduct.setCategory(category);
		Optional<User> sellerData = repository.findById(seller.getId());
		newProduct.setSellerId(sellerData.get());
		 
		return repositoryProduct.save(newProduct);
	}

	@Override
	public void save(String bytes) {
		this.imgSrc = bytes;
	}

	@Override
	public Cart addToCart(Product productToSave, Integer quantity, Optional<User> user) {
		Cart cart = new Cart();
		
		cart.setProductId(productToSave);
		cart.setQuantity(quantity);
		cart.setPrice(quantity*productToSave.getPrice());
		cart.setBoughtId(user.get());

		return repositoryCart.save(cart);
	}

	@Override
	public Buy addBuyNow(Product productToSave, Integer quantity, Optional<User> user) {
		Buy buy = new Buy();

		buy.setProductId(productToSave);
		buy.setQuantity(quantity);
		buy.setPrice(quantity*productToSave.getPrice());
		buy.setBoughtId(user.get());

		return repositoryBuy.save(buy);
	}

	@Override
	public void deleteCart(User user) {
		repositoryCart.deleteByBoughtId(user);
	}

	@Override
	public Product changeStock(Product product, int stock) {
		product.setQuantity(stock);

		return repositoryProduct.save(product);
	}
}