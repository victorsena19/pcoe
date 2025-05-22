package br.com.pcoe.controller;

import br.com.pcoe.dto.FormaPagamentoDTO;
import br.com.pcoe.service.FormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("forma-pagamento")
public class FormaPagamentoController {

    private final FormaPagamentoService formaPagamentoService;

    @Autowired
    public FormaPagamentoController(FormaPagamentoService formaPagamentoService){
        this.formaPagamentoService = formaPagamentoService;
    }

    @GetMapping
    public List<FormaPagamentoDTO> listarFormaPagamentos(){
        return formaPagamentoService.listarFormasPagamentos();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<FormaPagamentoDTO> listarFormaPagamentoId(@PathVariable UUID id){
        FormaPagamentoDTO registro = formaPagamentoService.ListarFormaPagamentoId(id);
        return ResponseEntity.ok().body(registro);
    }

    @PostMapping
    public ResponseEntity<FormaPagamentoDTO> cadastrarFormaPagamento(@RequestBody FormaPagamentoDTO formaPagamento){
        FormaPagamentoDTO novoFormaPagamento = formaPagamentoService.cadastroFormaPagamento(formaPagamento);
        return  ResponseEntity.status(HttpStatusCode.valueOf(201)).body(novoFormaPagamento);
    }
    @PutMapping(path = "/{id}")
    public ResponseEntity<FormaPagamentoDTO> atualizarFormaPagamento(@PathVariable UUID id, @RequestBody FormaPagamentoDTO formaPagamento){
        FormaPagamentoDTO formaPagamentoAtualizado = formaPagamentoService.atualizarFormaPagamento(id,formaPagamento);
        return  ResponseEntity.ok().body(formaPagamentoAtualizado);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<FormaPagamentoDTO> deletarFormaPagamento(@PathVariable UUID id){
        formaPagamentoService.deletarFormaPagamento(id);
        return ResponseEntity.noContent().build();
    }
}
