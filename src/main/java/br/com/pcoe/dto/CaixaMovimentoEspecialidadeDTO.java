package br.com.pcoe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CaixaMovimentoEspecialidadeDTO {

    private UUID id;
    private EspecialidadeDTO especialidade;
    private BigDecimal valorMovimento;
}
