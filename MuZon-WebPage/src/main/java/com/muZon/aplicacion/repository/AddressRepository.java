package com.muZon.aplicacion.repository;

import java.util.List;

import com.muZon.aplicacion.entity.Address;
import com.muZon.aplicacion.entity.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long>{

    public List<Address> findByUser(User user);
}

