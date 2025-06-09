package br.com.pcoe.dto;

import br.com.pcoe.enums.StatusSolicitacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespostaSolicitacaoReaberturaDTO {

    private String motivo;
    private StatusSolicitacao statusReabertura;
    private CaixaDTO caixa;
    private UsuarioDTO administrador;
}
