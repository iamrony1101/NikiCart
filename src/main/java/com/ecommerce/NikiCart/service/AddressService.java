package com.ecommerce.NikiCart.service;

import com.ecommerce.NikiCart.DTO.AddressDTO;
import com.ecommerce.NikiCart.model.User;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(Long addressId);


    List<AddressDTO> findByUserUserId(Long userId);

    AddressDTO deleteUserAddress(User loggedInUser, Long addressId);

    List<AddressDTO> getUserAddresses(User user);

    AddressDTO updateAddressById(User user, Long addressId,AddressDTO addressDTO);
}
