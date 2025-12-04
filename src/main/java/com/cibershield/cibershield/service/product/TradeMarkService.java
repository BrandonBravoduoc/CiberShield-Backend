package com.cibershield.cibershield.service.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibershield.cibershield.dto.productsDTO.TradeMarkDTO;
import com.cibershield.cibershield.model.product.TradeMark;
import com.cibershield.cibershield.repository.product.TradeMarkRepository;

import jakarta.transaction.Transactional;



@Service
@Transactional
public class TradeMarkService {

    @Autowired
    private TradeMarkRepository tradeMarkRepository;

    public List<TradeMarkDTO.Response> findAll() {
        return tradeMarkRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public TradeMarkDTO.Response findById(long id) {
        TradeMark tradeMark = tradeMarkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));
        return mapToResponse(tradeMark);
    }

    public TradeMarkDTO.Response create(TradeMarkDTO.Create dto) {
        if (tradeMarkRepository.existsByTradeMarkName(dto.tradeMarkName())) {
            throw new RuntimeException("La marca ya existe.");
        }

        TradeMark t = new TradeMark();
        t.setTradeMarkName(dto.tradeMarkName());

        t = tradeMarkRepository.save(t);
        return mapToResponse(t);
    }

    public void deleteTradeMark(Long id){
        if (!tradeMarkRepository.existsById(id)) {
            throw new RuntimeException("Marca comercial no encontrada");    
        }
        tradeMarkRepository.deleteById(id);
    }

    private TradeMarkDTO.Response mapToResponse(TradeMark t) {
        return new TradeMarkDTO.Response(
                t.getId(),
                t.getTradeMarkName());
    }

}
