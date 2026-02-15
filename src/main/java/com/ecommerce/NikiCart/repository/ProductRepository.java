package com.ecommerce.NikiCart.repository;

import com.ecommerce.NikiCart.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    Page<Product> findByCategoryCategoryId(Long categoryId, Pageable pageable);

    Page<Product> findByProductNameLikeIgnoreCase(String keyword,Pageable pageable);
    boolean existsByProductNameIgnoreCase(String productName);

}
