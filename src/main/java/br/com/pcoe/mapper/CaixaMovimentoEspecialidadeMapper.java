package br.com.pcoe.mapper;

import br.com.pcoe.dto.CaixaMovimentoEspecialidadeDTO;
import br.com.pcoe.model.CaixaMovimentoEspecialidade;

import java.util.List;

public interface CaixaMovimentoEspecialidadeMapper {

    CaixaMovimentoEspecialidadeDTO toDTO(CaixaMovimentoEspecialidade caixaMovimentoEspecialidade);
    List<CaixaMovimentoEspecialidadeDTO> toDTO(List<CaixaMovimentoEspecialidade> caixaMovimentoEspecialidades);
    CaixaMovimentoEspecialidade toEntity(CaixaMovimentoEspecialidadeDTO caixaMovimentoEspecialidadeDTO);
}
