package br.com.pcoe.dto;

import br.com.pcoe.enums.StatusSolicitacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequisicaoSolicitacaoReaberturaDTO {

    private UUID id;
    private String motivo;
    private StatusSolicitacao statusReabertura;
    private CaixaDTO caixa;
    private UsuarioDTO solicitante;
}
