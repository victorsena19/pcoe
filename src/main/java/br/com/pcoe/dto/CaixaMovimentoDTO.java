package br.com.pcoe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaixaMovimentoDTO {
        private UUID id;
        private FormaPagamentoDTO formaPagamento;
        private BigDecimal valorMovimento;
}
