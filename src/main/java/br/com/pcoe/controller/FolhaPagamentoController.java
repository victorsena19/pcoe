package br.com.pcoe.controller;

import br.com.pcoe.dto.FolhaPagamentoDTO;
import br.com.pcoe.gerador_pdf.FolhaPagamentoPDF;
import br.com.pcoe.service.FolhaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<FolhaPagamentoDTO>> listarFolhaPagamentos(){
        return ResponseEntity.ok().body(folhaPagamentoService.listarFolhaPagamentos());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<FolhaPagamentoDTO> listarFolhaPagamentoId(@PathVariable UUID id){
        FolhaPagamentoDTO registro = folhaPagamentoService.listarFolhaPagamentoId(id);
        return ResponseEntity.ok().body(registro);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<FolhaPagamentoDTO> cadastrarFolhaPagamento(@RequestBody FolhaPagamentoDTO folhaPagamento){
        FolhaPagamentoDTO novoFolhaPagamento = folhaPagamentoService.cadastroFolhaPagamento(folhaPagamento);
        return  ResponseEntity.status(HttpStatusCode.valueOf(201)).body(novoFolhaPagamento);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<FolhaPagamentoDTO> atualizarFolhaPagamento(@PathVariable UUID id, @RequestBody FolhaPagamentoDTO folhaPagamento){
        FolhaPagamentoDTO folhaPagamentoAtualizado = folhaPagamentoService.atualizarFolhaPagamento(id,folhaPagamento);
        return  ResponseEntity.ok().body(folhaPagamentoAtualizado);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<FolhaPagamentoDTO> deletarFolhaPagamento(@PathVariable UUID id){
        folhaPagamentoService.deletarFolhaPagamento(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
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
