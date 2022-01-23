package com.muZon.aplicacion.service;

import java.util.List;

import com.muZon.aplicacion.dto.ChangeAddressForm;
import com.muZon.aplicacion.entity.Address;
import com.muZon.aplicacion.entity.User;
import com.muZon.aplicacion.repository.AddressRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
	AddressRepository repository;

    @Autowired
    UserService userService;

    @Override
	public Address addAddress(ChangeAddressForm form) throws Exception {
		User user = userService.getUserById(form.getId());
		Address address = new Address(); 

        address.setUser(user);
		address.setAddress(form.getNewAddress());
		// user.setAddress("c " + form.getNewAddress() + form.getNewCity() + ", " +
		// form.getNewZipCode() + ", " + form.getNewCountry());
		return repository.save(address);
	}

    @Override
    public Address saveChanges(Address address, Long id) {
        repository.deleteById(id);

        return repository.save(address);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }    
}

