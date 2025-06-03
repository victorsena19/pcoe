package br.com.pcoe.controller;

import br.com.pcoe.dto.FolhaPagamentoDTO;
import br.com.pcoe.gerador_pdf.FolhaPagamentoPDF;
import br.com.pcoe.service.FolhaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("folha-pagamento")
public class FolhaPagamentoController {

    private final FolhaPagamentoService folhaPagamentoService;
    private final FolhaPagamentoPDF folhaPagamentoPDF;

    @Autowired
    public FolhaPagamentoController(FolhaPagamentoService folhaPagamentoService, FolhaPagamentoPDF folhaPagamentoPDF){
        this.folhaPagamentoService = folhaPagamentoService;
        this.folhaPagamentoPDF = folhaPagamentoPDF;
    }

    @GetMapping
    public List<FolhaPagamentoDTO> listarFolhaPagamentos(){
        return folhaPagamentoService.listarFolhaPagamentos();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<FolhaPagamentoDTO> listarFolhaPagamentoId(@PathVariable UUID id){
        FolhaPagamentoDTO registro = folhaPagamentoService.listarFolhaPagamentoId(id);
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

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> gerarPdfFolha(@PathVariable UUID id) {
        byte[] pdfBytes = folhaPagamentoPDF.gerarReciboFolhaPagamentoPDF(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition
                .inline()
                .filename("recibo_folha_pagamento.pdf")
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
