package com.ecommerce.NikiCart.repository;

import com.ecommerce.NikiCart.model.AppRole;
import com.ecommerce.NikiCart.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(AppRole appRole);
}
