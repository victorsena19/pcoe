package br.com.pcoe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolhaPagamentoDTO {
        private UUID id;
        private LocalDate dataLancamentoFolhaPagamento;
        private LocalDate dataInicialPrestacaoSevico;
        private LocalDate dataFinalPrestacaoSevico;
        private ProfissionalDTO profissional;
        private BigDecimal valorBase;
        private BigDecimal valorAcrescimo;
        private BigDecimal valorDesconto;
        private BigDecimal valorBonus;
        private BigDecimal valorLiquido;
}
