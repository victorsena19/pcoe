package br.com.pcoe.mapper;

import br.com.pcoe.dto.CaixaMovimentoDTO;
import br.com.pcoe.model.CaixaMovimento;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CaixaMovimentoMapper {

    CaixaMovimentoDTO toDTO(CaixaMovimento caixaMovimento);
    List<CaixaMovimentoDTO> toDTO(List<CaixaMovimento> caixaMovimentos);
    CaixaMovimento toEntity(CaixaMovimentoDTO caixaMovimentoDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CaixaMovimentoDTO dto, @MappingTarget CaixaMovimento entity);
}
