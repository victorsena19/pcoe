package br.com.pcoe.mapper;

import br.com.pcoe.dto.CaixaDTO;
import br.com.pcoe.model.Caixa;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CaixaMapper {
    CaixaDTO toDTO(Caixa caixa);
    List<CaixaDTO> toDTO(List<Caixa> caixas);
    Caixa toEntity(CaixaDTO caixaDTO);
}
