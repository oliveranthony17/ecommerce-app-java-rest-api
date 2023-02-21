package com.olbot.ecommerce.dao;

import com.olbot.ecommerce.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("http://localhost:4200") // tells Spring to accept calls from web browsers for this origin
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategoryId(@Param("id") Long id, Pageable pageable);

    Page<Product> findByNameContaining(@Param("name") String name, Pageable pageable);
    // the second "name" tells Spring to run SQL query similar to ...
    // SELECT * FROM Product p WHERE p.name LIKE CONCAT('%', :name, '%')
    // url will be .../api/products/search/findByNameContaining?name=Python

}
