package com.ecommerce.NikiCart.service;

import com.ecommerce.NikiCart.DTO.AddressDTO;
import com.ecommerce.NikiCart.exceptions.APIException;
import com.ecommerce.NikiCart.exceptions.ResourceNotFoundException;
import com.ecommerce.NikiCart.model.Address;
import com.ecommerce.NikiCart.model.User;
import com.ecommerce.NikiCart.repository.AddressRepository;
import com.ecommerce.NikiCart.repository.UserReposiroty;
import com.ecommerce.NikiCart.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private UserReposiroty userReposiroty;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO,User user) {
        Address address = modelMapper.map(addressDTO,Address.class);

        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);

    }

    @Override
    public List<AddressDTO> getAllAddresses() {
       List<Address> addressList = addressRepository.findAll();
       List<AddressDTO> addressDTOS = addressList.stream().map(address->
           modelMapper.map(address, AddressDTO.class)).toList();

        return addressDTOS;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));

        AddressDTO addressById = modelMapper.map(address, AddressDTO.class);
        return addressById;
    }

//    @Override
//    public AddressDTO updateAddressByUserId( Long userId, Long addressId, AddressDTO addressDTO) {
//
//        User existingUser = userReposiroty.findById(userId).orElseThrow(()->
//                new ResourceNotFoundException("User","userId",userId));
//
//        Address existingAddress = addressRepository.findById(addressId)
//                .orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));
//
//        if(!existingAddress.getUser().getUserId().equals(userId)){
//            throw new APIException("Address does not belong to this user");
//        }
//
//        existingAddress.setBuildingName(addressDTO.getBuildingName());
//        existingAddress.setCity(addressDTO.getCity());
//        existingAddress.setStreet(addressDTO.getStreet());
//        existingAddress.setState(addressDTO.getState());
//        existingAddress.setCountry(addressDTO.getCountry());
//        existingAddress.setPinCode(addressDTO.getPinCode());
//
//        Address savedAddress = addressRepository.save(existingAddress);
//        return modelMapper.map(savedAddress, AddressDTO.class);
//    }

    @Override
    public List<AddressDTO> findByUserUserId(Long userId) {

        User existingUser= userReposiroty.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException("User", "userId", userId)
        );
        List<Address> existingAddress = addressRepository.findByUserUserId(userId);
        if(existingAddress.isEmpty())
            throw new ResourceNotFoundException("Address","userId",userId);
        return existingAddress.stream().map(address-> modelMapper.map(address, AddressDTO.class)).toList();
    }

    @Override
    public AddressDTO deleteUserAddress( User loggedInUser, Long addressId) {

        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Address","addressId",addressId));

        if(!existingAddress.getUser().getUserId().equals(loggedInUser.getUserId())){
            throw new APIException("You are not allowed to delete this address");
        }

       addressRepository.delete(existingAddress);
        return modelMapper.map(existingAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {

        List<Address> userAddresses = user.getAddresses();
        if(userAddresses.isEmpty()){
            throw new APIException("No Adddress Exist");
        }

        return userAddresses.stream().map(address->
                modelMapper.map(address, AddressDTO.class)).toList();
    }

    @Override
    public AddressDTO updateAddressById(User user, Long addressId,AddressDTO addressDTO) {

        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(()->new APIException("No Address exist with addressId: "+addressId));

        if(!existingAddress.getUser().getUserId().equals(user.getUserId())){
            throw new APIException("You are not allowed to update this address");
        }

        existingAddress.setBuildingName(addressDTO.getBuildingName());
        existingAddress.setCity(addressDTO.getCity());
        existingAddress.setStreet(addressDTO.getStreet());
        existingAddress.setState(addressDTO.getState());
        existingAddress.setCountry(addressDTO.getCountry());
        existingAddress.setPinCode(addressDTO.getPinCode());

        Address updatedAddress = addressRepository.save(existingAddress);
        return modelMapper.map(updatedAddress, AddressDTO.class);
    }


}
