package br.com.pcoe.service;

import br.com.pcoe.dto.FolhaPagamentoDTO;
import br.com.pcoe.mapper.FolhaPagamentoMapper;
import br.com.pcoe.model.FolhaPagamento;
import br.com.pcoe.repository.FolhaPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FolhaPagamentoService {
    private final FolhaPagamentoRepository folhaPagamentoRepository;
    private final FolhaPagamentoMapper folhaPagamentoMapper;

    @Autowired
    public FolhaPagamentoService(FolhaPagamentoRepository folhaPagamentoRepository, FolhaPagamentoMapper folhaPagamentoMapper) {
        this.folhaPagamentoRepository = folhaPagamentoRepository;
        this.folhaPagamentoMapper = folhaPagamentoMapper;
    }

    public List<FolhaPagamentoDTO> listarFolhaPagamentos() {
        List<FolhaPagamento> folhaPagamento = folhaPagamentoRepository.findAll();
        return folhaPagamentoMapper.toDTO(folhaPagamento);
    }

    public FolhaPagamentoDTO ListarFolhaPagamentoId(UUID id) {
        FolhaPagamento folhaPagamento = folhaPagamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Não foi possivel encontrar uma Folha Pagamento com esse ID: " + id));
        return folhaPagamentoMapper.toDTO(folhaPagamento);
    }

    public FolhaPagamentoDTO cadastroFolhaPagamento(FolhaPagamentoDTO folhaPagamentoDTO) {
        boolean profissional = folhaPagamentoRepository.existsByProfissionalId(folhaPagamentoDTO.getProfissional().getId());
        if (profissional) {
            throw new IllegalArgumentException("Esse profissional não existe no sistema!");
        }
        FolhaPagamento novaFolhaPagamento = folhaPagamentoRepository.save(folhaPagamentoMapper.toEntity(folhaPagamentoDTO));
        return folhaPagamentoMapper.toDTO(novaFolhaPagamento);
    }

    public FolhaPagamentoDTO atualizarFolhaPagamento(UUID id, FolhaPagamentoDTO folhaPagamentoDTO){
        FolhaPagamento folhaPagamento = folhaPagamentoRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Não foi possivel encontrar uma Folha Pagamento com esse ID: " + id));
        FolhaPagamento folhaPagamentoAtualizada = folhaPagamentoMapper.toEntity(folhaPagamentoDTO);
        folhaPagamentoAtualizada.setId(folhaPagamento.getId());
        FolhaPagamento salvarFolhaPagamento = folhaPagamentoRepository.save(folhaPagamentoAtualizada);
        return folhaPagamentoMapper.toDTO(salvarFolhaPagamento);
    }

    public void deletarFolhaPagamento(UUID id){
        FolhaPagamento folhaPagamento = folhaPagamentoRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Não foi possivel encontrar um FolhaPagamento com esse ID: " + id));
        folhaPagamentoRepository.delete(folhaPagamento);
    }
}
