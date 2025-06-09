package br.com.pcoe.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaixaDTO {
        private UUID id;
        private UsuarioDTO usuario;
        private List<CaixaMovimentoDTO> caixaMovimento;
        private List<CaixaMovimentoEspecialidadeDTO> caixaMovimentoEspecialidade;
        private LocalDate data;
        private boolean aberto;
        private BigDecimal valorTotal;
        private BigDecimal valorRetirada;
        private BigDecimal valorQuebra;
        private String observacao;

}
