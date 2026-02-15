package com.ecommerce.NikiCart.security.services;

import com.ecommerce.NikiCart.model.User;
import com.ecommerce.NikiCart.repository.UserReposiroty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserReposiroty userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User Not Found with username" + username));
        return UserDetailsImpl.build(user);
    }
}
