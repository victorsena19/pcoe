package br.com.pcoe.controller;

import br.com.pcoe.dto.CaixaDTO;
import br.com.pcoe.service.CaixaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("caixa")
public class CaixaController {
    private final CaixaService caixaService;

    @Autowired
    public CaixaController(CaixaService caixaService){
        this.caixaService = caixaService;
    }

    @GetMapping
    public List<CaixaDTO> listarCaixas(){
        return caixaService.listarCaixas();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CaixaDTO> listarCaixaId(@PathVariable UUID id){
        CaixaDTO registro = caixaService.ListarCaixaId(id);
        return ResponseEntity.ok().body(registro);
    }

    @PostMapping(path = "/abrir")
    public ResponseEntity<CaixaDTO> abrirCaixa(){
        CaixaDTO novoCaixa = caixaService.abrirCaixa();
        return  ResponseEntity.status(HttpStatusCode.valueOf(201)).body(novoCaixa);
    }

    @PostMapping(path = "/movimento/{id}")
    public ResponseEntity<CaixaDTO> movimentoCaixa(@PathVariable UUID id, @RequestBody CaixaDTO caixa){
        CaixaDTO movimentoCaixa = caixaService.movimentoCaixa(id,caixa);
        return  ResponseEntity.status(HttpStatusCode.valueOf(200)).body(movimentoCaixa);
    }

    @PostMapping(path = "/fechar/{id}")
    public ResponseEntity<CaixaDTO> fecharCaixa(@PathVariable UUID id){
        CaixaDTO caixaFechado = caixaService.fecharCaixa(id);
        return  ResponseEntity.ok().body(caixaFechado);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CaixaDTO> deletarCaixa(@PathVariable UUID id){
        caixaService.deletarCaixa(id);
        return ResponseEntity.noContent().build();
    }


}
