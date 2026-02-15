package com.ecommerce.NikiCart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 characters")
    private String street;

    @NotBlank
    @Size(min = 5, message = "BuildingName name must be at least 5 characters")
    private String buildingName;

    @NotBlank
    @Size(min = 5, message = "City name must be at least 5 characters")
    private String city;

    @NotBlank
    @Size(min = 5, message = "State name must be at least 5 characters")
    private String state;

    @NotBlank
    @Size(min = 5, message = "Country name must be at least 5 characters")
    private String country;

    @NotBlank
    @Size(min = 6, max =6 , message = "PinCode must be at least 6 number")
    private String pinCode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    List<User> users = new ArrayList<>();

    public Address(String street, String buildingName, String city, String state, String country, String pinCode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
    }
}
