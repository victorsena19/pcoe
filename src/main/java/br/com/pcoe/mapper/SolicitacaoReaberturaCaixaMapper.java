package br.com.pcoe.mapper;

import br.com.pcoe.dto.RequisicaoSolicitacaoReaberturaDTO;
import br.com.pcoe.dto.RespostaSolicitacaoReaberturaDTO;
import br.com.pcoe.model.SolicitacaoReaberturaCaixa;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SolicitacaoReaberturaCaixaMapper {

    /**
     * Lida com os mapeamentos da RequisicaoSolicitacaoReaberturaDTO
     * */
    RequisicaoSolicitacaoReaberturaDTO toDTORequisicao(SolicitacaoReaberturaCaixa reaberturaCaixa);

    List<RequisicaoSolicitacaoReaberturaDTO> toDTORequisicao(List<SolicitacaoReaberturaCaixa> reaberturaCaixas);

    List<SolicitacaoReaberturaCaixa> toEntityRequisicao(List<RequisicaoSolicitacaoReaberturaDTO> reaberturaCaixas);

    SolicitacaoReaberturaCaixa toEntityRequisicao(RequisicaoSolicitacaoReaberturaDTO reaberturaCaixa);

    /**
     * Lida com os mapeamentos da RespostaSolicitacaoReaberturaDTO
     * */
    RespostaSolicitacaoReaberturaDTO toDTOResposta(SolicitacaoReaberturaCaixa reaberturaCaixa);

    List<RespostaSolicitacaoReaberturaDTO> toDTOResposta(List<SolicitacaoReaberturaCaixa> reaberturaCaixas);

    List<SolicitacaoReaberturaCaixa> toEntityResposta(List<RespostaSolicitacaoReaberturaDTO> reaberturaCaixas);

    SolicitacaoReaberturaCaixa toEntityResposta(RespostaSolicitacaoReaberturaDTO reaberturaCaixa);
}
