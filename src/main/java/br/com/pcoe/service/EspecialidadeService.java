package br.com.pcoe.service;

import br.com.pcoe.dto.EspecialidadeDTO;
import br.com.pcoe.model.Especialidade;
import br.com.pcoe.mapper.EspecialidadeMapper;
import br.com.pcoe.repository.EspecialidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EspecialidadeService {
    private final EspecialidadeRepository especialidadeRepository;
    private final EspecialidadeMapper especialidadeMapper;

    @Autowired
    public EspecialidadeService(EspecialidadeRepository especialidadeRepository, EspecialidadeMapper especialidadeMapper) {
        this.especialidadeRepository = especialidadeRepository;
        this.especialidadeMapper = especialidadeMapper;
    }

    public List<EspecialidadeDTO> listarEspecialidades() {
        List<Especialidade> especialidade = especialidadeRepository.findAll();
        return especialidadeMapper.toDTO(especialidade);
    }

    public EspecialidadeDTO ListarEspecialidadeId(UUID id) {
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Não foi possivel encontrar um especialidade com esse ID: " + id));
        return especialidadeMapper.toDTO(especialidade);
    }

    public EspecialidadeDTO cadastroEspecialidade(EspecialidadeDTO especialidadeDTO) {
        boolean nome = especialidadeRepository.existsByNomeIgnoreCase(especialidadeDTO.getNome());
        if (nome) {
            throw new IllegalArgumentException("Esse especialidade já existe no sistema!");
        }
        Especialidade novaEspecialidade = especialidadeRepository.save(especialidadeMapper.toEntity(especialidadeDTO));
        return especialidadeMapper.toDTO(novaEspecialidade);
    }

    public EspecialidadeDTO atualizarEspecialidade(UUID id, EspecialidadeDTO especialidadeDTO){
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Não foi possivel encontrar um Especialidade com esse ID: " + id));
        Especialidade especialidadeAtualizada = especialidadeMapper.toEntity(especialidadeDTO);
        especialidadeAtualizada.setId(especialidade.getId());
        Especialidade salvarEspecialidade = especialidadeRepository.save(especialidadeAtualizada);
        return especialidadeMapper.toDTO(salvarEspecialidade);
    }

    public void deletarEspecialidade(UUID id){
        Especialidade especialidade = especialidadeRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Não foi possivel encontrar um Especialidade com esse ID: " + id));
        especialidadeRepository.delete(especialidade);
    }
}
