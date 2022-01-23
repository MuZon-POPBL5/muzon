package com.muZon.aplicacion.service;

import java.util.List;

import com.muZon.aplicacion.dto.ChangeAddressForm;
import com.muZon.aplicacion.entity.Address;
import com.muZon.aplicacion.entity.User;

public interface AddressService {

    public Address addAddress(ChangeAddressForm form) throws Exception;

    public  Address saveChanges(Address address, Long id);

    public void delete(Long id);
}

