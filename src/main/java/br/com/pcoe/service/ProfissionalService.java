package br.com.pcoe.service;

import br.com.pcoe.dto.ProfissionalDTO;
import br.com.pcoe.exceptions.MensagemException;
import br.com.pcoe.model.Profissional;
import br.com.pcoe.repository.FolhaPagamentoRepository;
import br.com.pcoe.utils.UtilitariosGerais;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.pcoe.mapper.ProfissionalMapper;
import br.com.pcoe.repository.ProfissionalRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 *  Classe com CRUD de Profissional com as devidas validações para cada função
 */
@Service
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;
    private final FolhaPagamentoRepository folhaPagamentoRepository;
    private final ProfissionalMapper profissionalMapper;
    private final UtilitariosGerais utilitariosGerais;


    @Autowired
    public ProfissionalService(ProfissionalRepository profissionalRepository, FolhaPagamentoRepository folhaPagamentoRepository,
                               ProfissionalMapper profissionalMapper,
                               UtilitariosGerais utilitariosGerais) {
        this.profissionalRepository = profissionalRepository;
        this.folhaPagamentoRepository = folhaPagamentoRepository;
        this.profissionalMapper = profissionalMapper;
        this.utilitariosGerais = utilitariosGerais;
    }

    @Transactional(readOnly = true)
    public List<ProfissionalDTO> listarProfissionais() {
        List<Profissional> profissional = profissionalRepository.findAll();
        return profissionalMapper.toDTO(profissional);
    }

    @Transactional(readOnly = true)
    public ProfissionalDTO listarProfissionalId(UUID id) {
        Profissional profissional = utilitariosGerais
                .buscarEntidadeId(profissionalRepository, id, "Profissional");
        return profissionalMapper.toDTO(profissional);
    }

    @Transactional
    public ProfissionalDTO cadastroProfissional(ProfissionalDTO profissionalDTO) {
        Profissional novaProfissional = profissionalRepository.save(profissionalMapper.toEntity(profissionalDTO));
        return profissionalMapper.toDTO(novaProfissional);
    }

    @Transactional
    public ProfissionalDTO atualizarProfissional(UUID id, ProfissionalDTO profissionalDTO){
        Profissional profissional = utilitariosGerais
                .buscarEntidadeId(profissionalRepository, id, "Profissional");
        Profissional profissionalAtualizada = profissionalMapper.toEntity(profissionalDTO);
        profissionalAtualizada.setId(profissional.getId());
        Profissional salvarProfissional = profissionalRepository.save(profissionalAtualizada);
        return profissionalMapper.toDTO(salvarProfissional);
    }

    @Transactional
    public void deletarProfissional(UUID id){
        Profissional profissional = utilitariosGerais
                .buscarEntidadeId(profissionalRepository, id, "Profissional");

        boolean possuiFolhaPagamento = folhaPagamentoRepository.existsByProfissionalId(profissional.getId());
        if (possuiFolhaPagamento) {
            throw new MensagemException("Não é possivel apagar um profissional com folha de pagamento registrada, apenas deixe-o inativo!");
        }
        profissionalRepository.delete(profissional);
    }
}
