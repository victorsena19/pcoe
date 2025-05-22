package br.com.pcoe.service;

import br.com.pcoe.dto.ProfissionalDTO;
import br.com.pcoe.model.Profissional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.pcoe.mapper.ProfissionalMapper;
import br.com.pcoe.repository.ProfissionalRepository;

import java.util.List;
import java.util.UUID;

@Service
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;
    private final ProfissionalMapper profissionalMapper;

    @Autowired
    public ProfissionalService(ProfissionalRepository profissionalRepository, ProfissionalMapper profissionalMapper) {
        this.profissionalRepository = profissionalRepository;
        this.profissionalMapper = profissionalMapper;
    }

    public List<ProfissionalDTO> listarProfissionais() {
        List<Profissional> profissional = profissionalRepository.findAll();
        return profissionalMapper.toDTO(profissional);
    }

    public ProfissionalDTO ListarProfissionalId(UUID id) {
        Profissional profissional = profissionalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Não foi possivel encontrar um profissional com esse ID: " + id));
        return profissionalMapper.toDTO(profissional);
    }

    public ProfissionalDTO cadastroProfissional(ProfissionalDTO profissionalDTO) {
        boolean nome = profissionalRepository.existsByNomeIgnoreCase(profissionalDTO.getNome());
        if (nome) {
            throw new IllegalArgumentException("Esse profissional já existe no sistema!");
        }
        Profissional novaProfissional = profissionalRepository.save(profissionalMapper.toEntity(profissionalDTO));
        return profissionalMapper.toDTO(novaProfissional);
    }

    public ProfissionalDTO atualizarProfissional(UUID id, ProfissionalDTO profissionalDTO){
        Profissional profissional = profissionalRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Não foi possivel encontrar um Profissional com esse ID: " + id));
        Profissional profissionalAtualizada = profissionalMapper.toEntity(profissionalDTO);
        profissionalAtualizada.setId(profissional.getId());
        Profissional salvarProfissional = profissionalRepository.save(profissionalAtualizada);
        return profissionalMapper.toDTO(salvarProfissional);
    }

    public void deletarProfissional(UUID id){
        Profissional profissional = profissionalRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Não foi possivel encontrar um Profissional com esse ID: " + id));
        profissionalRepository.delete(profissional);
    }
}
