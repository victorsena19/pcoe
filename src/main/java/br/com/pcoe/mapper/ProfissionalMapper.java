package br.com.pcoe.mapper;

import br.com.pcoe.dto.ProfissionalDTO;
import br.com.pcoe.model.Profissional;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfissionalMapper {

    ProfissionalDTO toDTO(Profissional profissional);
    List<ProfissionalDTO> toDTO(List<Profissional> profissionals);
    Profissional toEntity(ProfissionalDTO profissionalDTO);
}
