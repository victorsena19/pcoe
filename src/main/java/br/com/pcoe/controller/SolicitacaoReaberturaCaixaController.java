package br.com.pcoe.controller;

import br.com.pcoe.dto.RequisicaoSolicitacaoReaberturaDTO;
import br.com.pcoe.dto.RespostaSolicitacaoReaberturaDTO;
import br.com.pcoe.enums.StatusSolicitacao;
import br.com.pcoe.service.SolicitacaoReaberturaCaixaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("reabertura-caixa")
public class SolicitacaoReaberturaCaixaController {
    private final SolicitacaoReaberturaCaixaService solicitacaoReaberturaCaixaService;

    public SolicitacaoReaberturaCaixaController(
            SolicitacaoReaberturaCaixaService solicitacaoReaberturaCaixaService) {
        this.solicitacaoReaberturaCaixaService = solicitacaoReaberturaCaixaService;
    }
    @GetMapping("/solicitacao")
    public ResponseEntity<List<RespostaSolicitacaoReaberturaDTO>> listarRequisicaoReaberturaCaixa(
            @RequestParam StatusSolicitacao statusReabertura){
        return ResponseEntity.ok().body(solicitacaoReaberturaCaixaService
                .listarStatusReaberturaParaSolicitante(statusReabertura));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/resposta")
    public ResponseEntity<List<RequisicaoSolicitacaoReaberturaDTO>> listarRespostaReaberturaCaixa(
            @RequestParam StatusSolicitacao statusReabertura) {
        return ResponseEntity.ok().body(solicitacaoReaberturaCaixaService
                .listarRequisicaoReaberturaStatus(statusReabertura));
    }

    @PostMapping
    public ResponseEntity<RequisicaoSolicitacaoReaberturaDTO> solicitarReaberturaCaixa(
            @RequestBody RequisicaoSolicitacaoReaberturaDTO solicitacaoReaberturaDTO){
        solicitacaoReaberturaCaixaService.processaReaberturaCaixas(solicitacaoReaberturaDTO);
        return ResponseEntity.ok().build();

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}")
    public ResponseEntity<RespostaSolicitacaoReaberturaDTO> atualizarReaberturaCaixa(@PathVariable UUID id,
                                                            @RequestBody RespostaSolicitacaoReaberturaDTO solicitacaoReaberturaDTO){
        return ResponseEntity.ok()
                .body(solicitacaoReaberturaCaixaService.atualizarRespostaReaberturaCaixa(id, solicitacaoReaberturaDTO));

    }


}
