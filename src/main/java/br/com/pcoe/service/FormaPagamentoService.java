package br.com.pcoe.service;

import br.com.pcoe.dto.FormaPagamentoDTO;
import br.com.pcoe.enums.MensagensErrosGenericas;
import br.com.pcoe.exceptions.MensagemException;
import br.com.pcoe.mapper.FormaPagamentoMapper;
import br.com.pcoe.model.FormaPagamento;
import br.com.pcoe.repository.FormaPagamentoRepository;
import br.com.pcoe.utils.UtilitariosGerais;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 *  Classe com CRUD de Forma de Pagamento com as devidas validações para cada função
*/
@Service
public class FormaPagamentoService {
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final FormaPagamentoMapper formaPagamentoMapper;
    private final UtilitariosGerais utilitariosGerais;

    @Autowired
    public FormaPagamentoService(FormaPagamentoRepository formaPagamentoRepository,
                                 FormaPagamentoMapper formaPagamentoMapper,
                                 UtilitariosGerais utilitariosGerais){
        this.formaPagamentoRepository = formaPagamentoRepository;
        this.formaPagamentoMapper = formaPagamentoMapper;
        this.utilitariosGerais = utilitariosGerais;
    }

    @Transactional(readOnly = true)
    public List<FormaPagamentoDTO> listarFormasPagamentos(){
        List<FormaPagamento> listaDeformaPagamento = formaPagamentoRepository.findAll();

        return formaPagamentoMapper.toDTO(listaDeformaPagamento);
    }

    @Transactional(readOnly = true)
    public FormaPagamentoDTO listarFormaPagamentoId(UUID id){
        FormaPagamento formaPagamentoId = utilitariosGerais
                .buscarEntidadeId(formaPagamentoRepository, id, "Forma de Pagamento");

        return formaPagamentoMapper.toDTO(formaPagamentoId);
    }

    @Transactional
    public FormaPagamentoDTO cadastroFormaPagamento(FormaPagamentoDTO formaPagamentoDTO){
        boolean nomeDeFormaPagamentoExistente = formaPagamentoRepository
                .existsByNomeIgnoreCase(formaPagamentoDTO.getNome());

        //Valida se já existe um uma forma de pagamento com o mesmo nome, antes de cadastrar
        if(nomeDeFormaPagamentoExistente){
            throw new MensagemException(MensagensErrosGenericas
                    .ARGUMENTOJAEXISTENTE.format(formaPagamentoDTO.getNome()));
        }

        FormaPagamento novaFormaPagamento = formaPagamentoRepository.save(formaPagamentoMapper
                .toEntity(formaPagamentoDTO));

        return formaPagamentoMapper.toDTO(novaFormaPagamento);
    }

    @Transactional
    public FormaPagamentoDTO atualizarFormaPagamento(UUID id, FormaPagamentoDTO formaPagamentoDTO){
        FormaPagamento formaPagamentoExistente = utilitariosGerais
                .buscarEntidadeId(formaPagamentoRepository, id, "Forma de Pagamento");

        boolean nomeDeFormaPagamentoExistente = formaPagamentoRepository
                .existsByNomeIgnoreCase(formaPagamentoDTO.getNome());

        //Valida se já existe um uma forma de pagamento com o mesmo nome, antes de atualizar
        if(nomeDeFormaPagamentoExistente){
            throw new MensagemException(MensagensErrosGenericas
                    .ARGUMENTOJAEXISTENTE.format(formaPagamentoDTO.getNome()));
        }

        FormaPagamento formaPagamentoAtualizada = formaPagamentoMapper.toEntity(formaPagamentoDTO);

        formaPagamentoAtualizada.setId(formaPagamentoExistente.getId());

        FormaPagamento salvarFormaPagamento = formaPagamentoRepository.save(formaPagamentoAtualizada);

        return formaPagamentoMapper.toDTO(salvarFormaPagamento);
    }

    @Transactional
    public void deletarFormaPagamento(UUID id){
        FormaPagamento formaPagamentoExistente = utilitariosGerais
                .buscarEntidadeId(formaPagamentoRepository, id, "Forma de Pagamento");

        formaPagamentoRepository.delete(formaPagamentoExistente);
    }
}
