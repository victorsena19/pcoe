package br.com.pcoe.controller;

import br.com.pcoe.dto.CaixaGeralDTO;
import br.com.pcoe.model.Usuario;
import br.com.pcoe.service.CaixaGeralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("caixa-geral")
public class CaixaGeralController {

    private final CaixaGeralService caixaGeralService;

    @Autowired
    public CaixaGeralController(CaixaGeralService caixaGeralService){
        this.caixaGeralService = caixaGeralService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CaixaGeralDTO> listarTodosCaixas(){
        CaixaGeralDTO listaCaixaPorUsuario = caixaGeralService.listarTodosCaixas();
        return ResponseEntity.ok().body(listaCaixaPorUsuario);
    }
    @GetMapping("/usuario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CaixaGeralDTO> listarCaixaGeralPorUsuario(@RequestBody Usuario usuario){
        CaixaGeralDTO listaCaixaPorUsuario = caixaGeralService.listarCaixaPorUsuario(usuario);
        return ResponseEntity.ok().body(listaCaixaPorUsuario);
    }

    @GetMapping(path = "/data")
    @PreAuthorize("hasRole('ADMIN')")
    public CaixaGeralDTO listarCaixaGeralData(@RequestParam LocalDate data){
        return caixaGeralService.listarCaixaGeralData(data);
    }

    @GetMapping(path = "/datas")
    @PreAuthorize("hasRole('ADMIN')")
    public CaixaGeralDTO listarCaixaGeralBetweenData(@RequestParam LocalDate dataInicial, @RequestParam LocalDate dataFinal){
        return caixaGeralService.listarCaixaGeralBetweenData(dataInicial, dataFinal);
    }
}
