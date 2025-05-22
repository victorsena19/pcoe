package br.com.pcoe.mapper;

import br.com.pcoe.dto.FolhaPagamentoDTO;
import br.com.pcoe.model.FolhaPagamento;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FolhaPagamentoMapper {

    FolhaPagamentoDTO toDTO(FolhaPagamento folhaPagamento);
    List<FolhaPagamentoDTO> toDTO(List<FolhaPagamento> folhaPagamentos);
    FolhaPagamento toEntity(FolhaPagamentoDTO folhaPagamentoDTO);
}
