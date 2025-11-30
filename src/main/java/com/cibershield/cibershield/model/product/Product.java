package com.cibershield.cibershield.model.product;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "stock", nullable = false)
    private Integer Stock;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "image_url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "id_sub_category", nullable = true)
    private SubCategory subCategory;

    @ManyToOne
    @JoinColumn(name = "id_trade_mark", nullable = true )
    private TradeMark tradeMark;


}
