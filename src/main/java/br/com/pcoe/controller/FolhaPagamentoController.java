package br.com.pcoe.controller;

import br.com.pcoe.dto.FolhaPagamentoDTO;
import br.com.pcoe.service.FolhaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("folha-pagamento")
public class FolhaPagamentoController {

    private final FolhaPagamentoService folhaPagamentoService;

    @Autowired
    public FolhaPagamentoController(FolhaPagamentoService folhaPagamentoService){
        this.folhaPagamentoService = folhaPagamentoService;
    }

    @GetMapping
    public List<FolhaPagamentoDTO> listarFolhaPagamentos(){
        return folhaPagamentoService.listarFolhaPagamentos();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<FolhaPagamentoDTO> listarFolhaPagamentoId(@PathVariable UUID id){
        FolhaPagamentoDTO registro = folhaPagamentoService.ListarFolhaPagamentoId(id);
        return ResponseEntity.ok().body(registro);
    }

    @PostMapping
    public ResponseEntity<FolhaPagamentoDTO> cadastrarFolhaPagamento(@RequestBody FolhaPagamentoDTO folhaPagamento){
        FolhaPagamentoDTO novoFolhaPagamento = folhaPagamentoService.cadastroFolhaPagamento(folhaPagamento);
        return  ResponseEntity.status(HttpStatusCode.valueOf(201)).body(novoFolhaPagamento);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<FolhaPagamentoDTO> atualizarFolhaPagamento(@PathVariable UUID id, @RequestBody FolhaPagamentoDTO folhaPagamento){
        FolhaPagamentoDTO folhaPagamentoAtualizado = folhaPagamentoService.atualizarFolhaPagamento(id,folhaPagamento);
        return  ResponseEntity.ok().body(folhaPagamentoAtualizado);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<FolhaPagamentoDTO> deletarFolhaPagamento(@PathVariable UUID id){
        folhaPagamentoService.deletarFolhaPagamento(id);
        return ResponseEntity.noContent().build();
    }
}
