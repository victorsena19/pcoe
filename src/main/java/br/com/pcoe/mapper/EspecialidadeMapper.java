package br.com.pcoe.mapper;

import br.com.pcoe.dto.EspecialidadeDTO;
import br.com.pcoe.model.Especialidade;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EspecialidadeMapper {

    EspecialidadeDTO toDTO(Especialidade especialidade);
    List<EspecialidadeDTO> toDTO(List<Especialidade> especialidades);
    Especialidade toEntity(EspecialidadeDTO especialidadeDTO);
}
