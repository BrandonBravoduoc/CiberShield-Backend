package com.cibershield.cibershield.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibershield.cibershield.model.product.TradeMark;
import com.cibershield.cibershield.repository.product.TradeMarkRepository;

import jakarta.transaction.TransactionScoped;

@Service
@TransactionScoped
public class TradeMarkService {

    @Autowired
    private TradeMarkRepository tradeMarkRepository;

    public List<TradeMark> searchAll(){
        return tradeMarkRepository.findAll();
    }
}
