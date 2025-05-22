package br.com.pcoe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaixaGeralDTO {
       private UUID id;
        private List<CaixaDTO> caixa;
        private BigDecimal valorTotal;
        private BigDecimal valorTotalTaxa;

}
