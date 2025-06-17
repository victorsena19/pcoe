package br.com.pcoe.service;

import br.com.pcoe.dto.EspecialidadeDTO;
import br.com.pcoe.enums.MensagensErrosGenericas;
import br.com.pcoe.exceptions.MensagemException;
import br.com.pcoe.model.Especialidade;
import br.com.pcoe.mapper.EspecialidadeMapper;
import br.com.pcoe.repository.EspecialidadeRepository;
import br.com.pcoe.utils.UtilitariosGerais;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EspecialidadeService {
    private final EspecialidadeRepository especialidadeRepository;
    private final EspecialidadeMapper especialidadeMapper;
    private final UtilitariosGerais utilitariosGerais;

    @Autowired
    public EspecialidadeService(EspecialidadeRepository especialidadeRepository, EspecialidadeMapper especialidadeMapper, UtilitariosGerais utilitariosGerais) {
        this.especialidadeRepository = especialidadeRepository;
        this.especialidadeMapper = especialidadeMapper;
        this.utilitariosGerais = utilitariosGerais;
    }

    @Transactional(readOnly = true)
    public List<EspecialidadeDTO> listarEspecialidades() {
        List<Especialidade> especialidade = especialidadeRepository.findAll();
        return especialidadeMapper.toDTO(especialidade);
    }

    @Transactional(readOnly = true)
    public EspecialidadeDTO listarEspecialidadeId(UUID id) {
        Especialidade especialidade = utilitariosGerais
                .buscarEntidadeId(especialidadeRepository, id, "Especialidade");
        return especialidadeMapper.toDTO(especialidade);
    }

    @Transactional(readOnly = true)
    public EspecialidadeDTO listarEspecialidadePorNome(String nome) {
        Especialidade especialidade = especialidadeRepository.findByNomeIgnoreCase(nome);
        return especialidadeMapper.toDTO(especialidade);
    }

    @Transactional
    public EspecialidadeDTO cadastroEspecialidade(EspecialidadeDTO especialidadeDTO) {
        boolean especialidadeJaExiste  = especialidadeRepository.existsByNomeIgnoreCase(especialidadeDTO.getNome());
        if (especialidadeJaExiste ) {
            throw new MensagemException(MensagensErrosGenericas.ARGUMENTOJAEXISTENTE.format(especialidadeDTO.getNome()));
        }
        Especialidade novaEspecialidade = especialidadeRepository.save(especialidadeMapper.toEntity(especialidadeDTO));
        return especialidadeMapper.toDTO(novaEspecialidade);
    }

    @Transactional
    public EspecialidadeDTO atualizarEspecialidade(UUID id, EspecialidadeDTO especialidadeDTO){
        Especialidade especialidadeJaExiste  = utilitariosGerais
                .buscarEntidadeId(especialidadeRepository, id, "Especialidade");
        Especialidade especialidadeAtualizada = especialidadeMapper.toEntity(especialidadeDTO);
        especialidadeAtualizada.setId(especialidadeJaExiste.getId());
        Especialidade salvarEspecialidade = especialidadeRepository.save(especialidadeAtualizada);
        return especialidadeMapper.toDTO(salvarEspecialidade);
    }

    @Transactional
    public void deletarEspecialidade(UUID id){
        Especialidade especialidadeExiste = utilitariosGerais
                .buscarEntidadeId(especialidadeRepository, id, "Especialidade");
        especialidadeRepository.delete(especialidadeExiste );
    }
}
