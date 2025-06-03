package br.com.pcoe.service;

import br.com.pcoe.dto.FolhaPagamentoDTO;
import br.com.pcoe.enuns.MensagensErrosGenericas;
import br.com.pcoe.exceptions.MensagemException;
import br.com.pcoe.mapper.FolhaPagamentoMapper;
import br.com.pcoe.model.FolhaPagamento;
import br.com.pcoe.repository.FolhaPagamentoRepository;
import br.com.pcoe.repository.ProfissionalRepository;
import br.com.pcoe.utils.UtilitariosGerais;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class FolhaPagamentoService {
    private final FolhaPagamentoRepository folhaPagamentoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final FolhaPagamentoMapper folhaPagamentoMapper;
    private final UtilitariosGerais utilitariosGerais;

    @Autowired
    public FolhaPagamentoService(FolhaPagamentoRepository folhaPagamentoRepository, ProfissionalRepository profissionalRepository, FolhaPagamentoMapper folhaPagamentoMapper, UtilitariosGerais utilitariosGerais) {
        this.folhaPagamentoRepository = folhaPagamentoRepository;
        this.profissionalRepository = profissionalRepository;
        this.folhaPagamentoMapper = folhaPagamentoMapper;
        this.utilitariosGerais = utilitariosGerais;
    }

    @Transactional(readOnly = true)
    public List<FolhaPagamentoDTO> listarFolhaPagamentos() {
        List<FolhaPagamento> folhaPagamento = folhaPagamentoRepository.findAll();
        return folhaPagamentoMapper.toDTO(folhaPagamento);
    }

    @Transactional(readOnly = true)
    public FolhaPagamentoDTO listarFolhaPagamentoId(UUID id) {
        FolhaPagamento folhaPagamento = utilitariosGerais.buscarEntidadeId(folhaPagamentoRepository, id, "Folha de Pagamento");
        return folhaPagamentoMapper.toDTO(folhaPagamento);
    }

    @Transactional
    public FolhaPagamentoDTO cadastroFolhaPagamento(FolhaPagamentoDTO folhaPagamentoDTO) {
        boolean profissional = profissionalRepository.existsById(folhaPagamentoDTO.getProfissional().getId());
        if (!profissional) {
            throw new MensagemException(MensagensErrosGenericas.IDNAOENCONTRADO.format("Profissional"));
        }

        folhaPagamentoDTO.setDataLancamentoFolhaPagamento(LocalDate.now());
        FolhaPagamento novaFolhaPagamento = folhaPagamentoRepository.save(folhaPagamentoMapper.toEntity(folhaPagamentoDTO));
        return folhaPagamentoMapper.toDTO(novaFolhaPagamento);
    }

    @Transactional
    public FolhaPagamentoDTO atualizarFolhaPagamento(UUID id, FolhaPagamentoDTO folhaPagamentoDTO){
        FolhaPagamento folhaPagamento = utilitariosGerais
                .buscarEntidadeId(folhaPagamentoRepository, id, "Folha de Pagamento");
        FolhaPagamento folhaPagamentoAtualizada = folhaPagamentoMapper.toEntity(folhaPagamentoDTO);
        folhaPagamentoAtualizada.setId(folhaPagamento.getId());
        FolhaPagamento salvarFolhaPagamento = folhaPagamentoRepository.save(folhaPagamentoAtualizada);
        return folhaPagamentoMapper.toDTO(salvarFolhaPagamento);
    }

    @Transactional
    public void deletarFolhaPagamento(UUID id){
        FolhaPagamento folhaPagamento = utilitariosGerais
                .buscarEntidadeId(folhaPagamentoRepository, id, "Folha de Pagamento");
        folhaPagamentoRepository.delete(folhaPagamento);
    }
}
