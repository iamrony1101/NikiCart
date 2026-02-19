package com.ecommerce.NikiCart.util;

import com.ecommerce.NikiCart.model.User;
import com.ecommerce.NikiCart.repository.UserReposiroty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private UserReposiroty userReposiroty;

    public String loggedInEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userReposiroty.findByUserName(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + authentication.getName()));

        return user.getEmail();
    }

    public Long loggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userReposiroty.findByUserName(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + authentication.getName()));

        return user.getUserId();
    }

    public User loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userReposiroty.findByUserName(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + authentication.getName()));
        return user;

    }
}
