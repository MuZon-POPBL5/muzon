package com.muZon.aplicacion.service;

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
    public Address getAddressByUserId(User user) {
        return repository.findByUser(user);
    }

    @Override
	public Address changeAddress(ChangeAddressForm form) throws Exception {
		User user = userService.getUserById(form.getId());
		Address address = new Address(); 

        address.setUser(user);
		address.setAddress(form.getNewAddress());
		// user.setAddress("c " + form.getNewAddress() + form.getNewCity() + ", " +
		// form.getNewZipCode() + ", " + form.getNewCountry());
		return repository.save(address);
	}    
}
