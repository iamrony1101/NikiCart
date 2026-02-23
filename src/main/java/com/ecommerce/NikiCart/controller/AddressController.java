package com.ecommerce.NikiCart.controller;

import com.ecommerce.NikiCart.DTO.AddressDTO;
import com.ecommerce.NikiCart.model.User;
import com.ecommerce.NikiCart.service.AddressService;
import com.ecommerce.NikiCart.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    AuthUtil authUtil;
    @Autowired
    private AddressService addressService;

    @PostMapping("/address")
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO){
        User user = authUtil.loggedInUser();
        AddressDTO savedAddress = addressService.createAddress(addressDTO,user);
        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses(){
        List<AddressDTO> getAddresses = addressService.getAllAddresses();
        return new ResponseEntity<>(getAddresses, HttpStatus.OK);
    }

    @GetMapping("/address/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){
        AddressDTO getAddressesById = addressService.getAddressById(addressId);
        return new ResponseEntity<>(getAddressesById, HttpStatus.OK);
    }

    @GetMapping("/user/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddresses(){
        User user = authUtil.loggedInUser();
        List<AddressDTO> addressDTOS = addressService.getUserAddresses(user);
        return new ResponseEntity<>(addressDTOS,HttpStatus.OK);
    }

    @PutMapping("/user/address/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressByUserId(@PathVariable Long addressId,@RequestBody AddressDTO addressDTO){
        User loggedInUser = authUtil.loggedInUser();
        AddressDTO updateAddressById = addressService.updateAddressById(loggedInUser,addressId,addressDTO);
        return new ResponseEntity<>(updateAddressById, HttpStatus.OK);
    }

    @GetMapping("/address/user/{userId}")
    public ResponseEntity<List<AddressDTO>> findAddressByUserId(@PathVariable Long userId){
        User user = authUtil.loggedInUser();
        List<AddressDTO> findAddressByUserId = addressService.findByUserUserId(userId);
        return new ResponseEntity<>(findAddressByUserId, HttpStatus.OK);
    }

    @DeleteMapping("/user/address/{addressId}")
    public ResponseEntity<AddressDTO> deleteUserAddress( @PathVariable Long addressId){
        User loggedInUser = authUtil.loggedInUser();
        AddressDTO deleteAddress = addressService.deleteUserAddress(loggedInUser, addressId);
        return new ResponseEntity<>(deleteAddress,HttpStatus.OK);
    }























}
