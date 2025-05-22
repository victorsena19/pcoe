package br.com.pcoe.mapper;

import br.com.pcoe.dto.FormaPagamentoDTO;
import br.com.pcoe.model.FormaPagamento;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FormaPagamentoMapper {

    FormaPagamentoDTO toDTO(FormaPagamento formaPagamento);
    List<FormaPagamentoDTO> toDTO(List<FormaPagamento> formaPagamentos);
    FormaPagamento toEntity(FormaPagamentoDTO formaPagamentoDTO);
}
