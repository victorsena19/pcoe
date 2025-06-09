package br.com.pcoe.controller;

import br.com.pcoe.dto.CaixaDTO;
import br.com.pcoe.service.CaixaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public ResponseEntity<List<CaixaDTO>> listarCaixasParaUsuarioLogado(){
        List<CaixaDTO> registro = caixaService.listarCaixasParaUsuarioLogado();
        return ResponseEntity.ok().body(registro);
    }

    @GetMapping(path = "/data")
    public ResponseEntity<CaixaDTO> listarCaixasPorDataUsuarioLogado(@RequestParam LocalDate data){
        CaixaDTO registro = caixaService.listarCaixaPorDataParaUsarioLogado(data);
        return ResponseEntity.ok().body(registro);
    }

    @GetMapping(path = "/datas")
    public ResponseEntity<List<CaixaDTO>> listarCaixasPorPeriodoUsuarioLogado(@RequestParam LocalDate dataInicial,@RequestParam LocalDate dataFinal){
        List<CaixaDTO> registro = caixaService.listarCaixaPorPeriodoUsuarioLogado(dataInicial, dataFinal);
        return ResponseEntity.ok().body(registro);
    }

    @GetMapping(path = "/aberto")
    public ResponseEntity<List<CaixaDTO>> listarCaixasAbertoParaUsuarioLogado(){
        List<CaixaDTO> registro = caixaService.listarCaixaAbertoParaUsuarioLogado();
        return ResponseEntity.ok().body(registro);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CaixaDTO> listarCaixaId(@PathVariable UUID id){
        CaixaDTO registro = caixaService.listarCaixaId(id);
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
