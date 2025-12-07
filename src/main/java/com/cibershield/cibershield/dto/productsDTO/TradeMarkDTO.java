package com.cibershield.cibershield.dto.productsDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class TradeMarkDTO {
        @Schema(name = "TradeMarkCreate")
        public record Create(
                        @NotBlank(message = "El nombre de la marca es obligatorio") String tradeMarkName) {
        }

        @Schema(name = "TradeMarkResponse")
        public record Response(
                        Long id,
                        String tradeMarkName) {
        }

        @Schema(name = "TradeMarkUpdate")
        public record Update(
                        String tradeMarkName) {
        }

        @Schema(name = "TradeMarkCombo")
        public record Combo(
                        Long id,
                        String tradeMarkName) {
        }
}