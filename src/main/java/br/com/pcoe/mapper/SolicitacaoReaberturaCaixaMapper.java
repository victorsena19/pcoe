package br.com.pcoe.mapper;

import br.com.pcoe.dto.RequisicaoSolicitacaoReaberturaDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SolicitacaoReaberturaCaixa {
    RequisicaoSolicitacaoReaberturaDTO toDTO(SolicitacaoReaberturaCaixa reaberturaCaixa);

    List<RequisicaoSolicitacaoReaberturaDTO> toDTO(List<SolicitacaoReaberturaCaixa> reaberturaCaixas);

    List<SolicitacaoReaberturaCaixa> toEntity(List<RequisicaoSolicitacaoReaberturaDTO> reaberturaCaixas);

    SolicitacaoReaberturaCaixa toEntity(RequisicaoSolicitacaoReaberturaDTO reaberturaCaixa);
}
