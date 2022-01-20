package com.muZon.aplicacion.repository;

import com.muZon.aplicacion.entity.Buy;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyRepository extends CrudRepository<Buy, Long>{
    
}
