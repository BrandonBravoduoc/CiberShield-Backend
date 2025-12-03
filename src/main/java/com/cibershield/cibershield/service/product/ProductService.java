package com.cibershield.cibershield.service.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibershield.cibershield.dto.productsDTO.ProductDTO;
import com.cibershield.cibershield.model.product.Product;
import com.cibershield.cibershield.model.product.SubCategory;
import com.cibershield.cibershield.model.product.TradeMark;
import com.cibershield.cibershield.repository.product.ProductRepository;
import com.cibershield.cibershield.repository.product.SubCategoryRepository;
import com.cibershield.cibershield.repository.product.TradeMarkRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private TradeMarkRepository tradeMarkRepository;

    public List<ProductDTO.Response> findAll() {
        return productRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ProductDTO.Response findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));
        return mapToResponse(product);
    }

    public ProductDTO.Response createProduct(ProductDTO.Create dto) {
        if (productRepository.findByProductName(dto.productName()).isPresent()) {
            throw new RuntimeException("Ya existe un producto con ese nombre.");
        }
        SubCategory subCategory = subCategoryRepository.findById(dto.subCategoryId())
                .orElseThrow(() -> new RuntimeException("La subcategoría no existe."));

        TradeMark tradeMark = tradeMarkRepository.findById(dto.tradeMarkId())
                .orElseThrow(() -> new RuntimeException("La marca no existe."));

        Product product = new Product();
        product.setProductName(dto.productName());
        product.setStock(dto.stock());
        product.setPrice(dto.price());
        product.setUrl(dto.url());
        product.setSubCategory(subCategory);
        product.setTradeMark(tradeMark);

        product = productRepository.save(product);

        return mapToResponse(product);
    }

    public ProductDTO.Response updateProduct(Long id, ProductDTO.Update dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));

        if (dto.productName() != null && !dto.productName().isBlank()) {
            product.setProductName(dto.productName());
        }
        if (dto.stock() != null) {
            product.setStock(dto.stock());
        }
        if (dto.price() != null) {
            product.setPrice(dto.price());
        }
        if (dto.url() != null) {
            product.setUrl(dto.url());
        }

        if (dto.subCategoryId() != null) {
            SubCategory subCat = subCategoryRepository.findById(dto.subCategoryId())
                    .orElseThrow(() -> new RuntimeException("Subcategoría no encontrada"));
            product.setSubCategory(subCat);
        }
        if (dto.tradeMarkId() != null) {
            TradeMark tradeMark = tradeMarkRepository.findById(dto.tradeMarkId())
                    .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
            product.setTradeMark(tradeMark);
        }

        product = productRepository.save(product);
        return mapToResponse(product);
    }

    public void reduceStock(Long productId, Integer amount) {
        if (amount < 0)
            throw new RuntimeException("La cantidad no puede ser negativa");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        int newStock = product.getStock() - amount;

        if (newStock < 0) {
            throw new RuntimeException("Stock insuficiente para el producto: " + product.getProductName());
        }

        product.setStock(newStock);
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productRepository.deleteById(id);
    }

    private ProductDTO.Response mapToResponse(Product p) {
        return new ProductDTO.Response(
                p.getId(),
                p.getProductName(),
                p.getStock(),
                p.getPrice(),
                p.getUrl(),
                p.getSubCategory() != null ? p.getSubCategory().getSubCategoryName() : "Sin Categoría",
                p.getSubCategory() != null && p.getSubCategory().getCategory() != null
                        ? p.getSubCategory().getCategory().getCategoryName()
                        : "Sin Categoría",
                p.getTradeMark() != null ? p.getTradeMark().getTradeMarkName() : "Sin Marca");
    }
}