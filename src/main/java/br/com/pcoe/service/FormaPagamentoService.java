package br.com.pcoe.service;

import br.com.pcoe.dto.FormaPagamentoDTO;
import br.com.pcoe.mapper.FormaPagamentoMapper;
import br.com.pcoe.model.FormaPagamento;
import br.com.pcoe.repository.FormaPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FormaPagamentoService {
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final FormaPagamentoMapper formaPagamentoMapper;

    @Autowired
    public FormaPagamentoService(FormaPagamentoRepository formaPagamentoRepository,
                                 FormaPagamentoMapper formaPagamentoMapper){
        this.formaPagamentoRepository = formaPagamentoRepository;
        this.formaPagamentoMapper = formaPagamentoMapper;
    }

    public List<FormaPagamentoDTO> listarFormasPagamentos(){
        List<FormaPagamento> formaPagamento = formaPagamentoRepository.findAll();
        return formaPagamentoMapper.toDTO(formaPagamento);
    }

    public FormaPagamentoDTO ListarFormaPagamentoId(UUID id){
        FormaPagamento formaPagamento = formaPagamentoRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Não foi possivel encontrar uma Forma Pagamento com esse ID: " + id));
        return formaPagamentoMapper.toDTO(formaPagamento);
    }

    public FormaPagamentoDTO cadastroFormaPagamento(FormaPagamentoDTO formaPagamentoDTO){
        boolean nome = formaPagamentoRepository.existsByNomeIgnoreCase(formaPagamentoDTO.getNome());
        if(nome){
            throw new IllegalArgumentException("Já esxiste uma Forma Pagamento com esse nome : " + formaPagamentoDTO.getNome());
        }
        System.out.println("Aqui está a forma de pagamento: ------>" + formaPagamentoDTO);
        FormaPagamento novaFormaPagamento = formaPagamentoRepository.save(formaPagamentoMapper.toEntity(formaPagamentoDTO));
        return formaPagamentoMapper.toDTO(novaFormaPagamento);
    }

    public FormaPagamentoDTO atualizarFormaPagamento(UUID id, FormaPagamentoDTO formaPagamentoDTO){
        FormaPagamento formaPagamento = formaPagamentoRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Não foi possivel encontrar uma Forma Pagamento com esse ID: " + id));
        FormaPagamento formaPagamentoAtualizada = formaPagamentoMapper.toEntity(formaPagamentoDTO);
        formaPagamentoAtualizada.setId(formaPagamento.getId());
        FormaPagamento salvarFormaPagamento = formaPagamentoRepository.save(formaPagamentoAtualizada);
        return formaPagamentoMapper.toDTO(salvarFormaPagamento);
    }

    public void deletarFormaPagamento(UUID id){
        FormaPagamento formaPagamento = formaPagamentoRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Não foi possivel encontrar uma Forma Pagamento com esse ID: " + id));
        formaPagamentoRepository.delete(formaPagamento);
    }
}
