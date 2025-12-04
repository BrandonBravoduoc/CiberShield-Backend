package com.cibershield.cibershield.controller.product;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cibershield.cibershield.dto.productsDTO.TradeMarkDTO;
import com.cibershield.cibershield.service.product.TradeMarkService;

@RestController
@RequestMapping("/api/v1/trademarks")
public class TradeMarkController {

    @Autowired
    private TradeMarkService tradeMarkService;

    @GetMapping
    public ResponseEntity<List<TradeMarkDTO.Response>> listAll() {
        return ResponseEntity.ok(tradeMarkService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(tradeMarkService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            tradeMarkService.deleteTradeMark(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
