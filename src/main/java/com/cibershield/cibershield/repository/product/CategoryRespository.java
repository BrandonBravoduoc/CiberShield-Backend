package com.cibershield.cibershield.repository.product;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cibershield.cibershield.model.product.Category;

@Repository
public interface CategoryRespository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE LOWER(c.categoryName) = LOWER(:categoryName)")
    Optional<Category> findByCategoryName(@Param("categoryName") String categoryName);

}
