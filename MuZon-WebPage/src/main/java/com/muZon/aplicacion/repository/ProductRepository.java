package com.muZon.aplicacion.repository;

import java.util.List;
import java.util.Optional;

import com.muZon.aplicacion.entity.Product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>{

    public List<Product> findByCategory(String category);
}
