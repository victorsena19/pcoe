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
public class EspecialidadeDTO {
        private UUID id;
        private String nome;
        private List<ProfissionalDTO> profissional;
        private BigDecimal valorPercentual;
}
