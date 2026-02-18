package com.ecommerce.NikiCart.repository;

import com.ecommerce.NikiCart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserReposiroty extends JpaRepository<User,Long> {

    Optional<User> findByUserName(String userName);

    boolean existsByEmail( String email);

    boolean existsByUserName(String userName);
}
