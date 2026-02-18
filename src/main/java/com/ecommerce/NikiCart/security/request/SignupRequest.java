package com.ecommerce.NikiCart.security.request;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotBlank
    @Size(min = 4, max = 15)
    private String userName;

    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    private String password;

    private Set<String> role;


}
