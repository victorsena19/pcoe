package br.com.pcoe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfissionalDTO {
        private UUID id;
        private String nome;
        private String cpf;
        private List<EspecialidadeDTO> especialidade;
        private boolean ativo;
}
