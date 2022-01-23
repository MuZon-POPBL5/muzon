package com.muZon.aplicacion.repository;

import java.util.List;

import com.muZon.aplicacion.entity.Product;
import com.muZon.aplicacion.entity.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>{

    public List<Product> findByCategory(String category);

}
