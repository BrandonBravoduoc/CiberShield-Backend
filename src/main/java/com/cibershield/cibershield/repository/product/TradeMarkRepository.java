package com.cibershield.cibershield.repository.product;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cibershield.cibershield.model.product.TradeMark;

@Repository
public interface TradeMarkRepository extends JpaRepository<TradeMark, Long> {

    @Query("SELECT m FROM TradeMark m WHERE LOWER(m.tradeMarkName) = LOWER(:tradeMarkName)")
    Optional<TradeMark> findByTradeMarkName(@Param("tradeMarkName") String tradeMarkName);

    boolean existsByTradeMarkName(String tradeMarkName);
}