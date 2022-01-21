package com.muZon.aplicacion.service;

import java.util.List;

import com.muZon.aplicacion.dto.ChangeAddressForm;
import com.muZon.aplicacion.entity.Address;
import com.muZon.aplicacion.entity.User;

public interface AddressService {

    List<Address> getAddressByUserId(User user);

    public Address changeAddress(ChangeAddressForm form) throws Exception;
}
