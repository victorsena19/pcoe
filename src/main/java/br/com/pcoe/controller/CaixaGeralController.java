package br.com.pcoe.controller;

import br.com.pcoe.dto.CaixaGeralDTO;
import br.com.pcoe.service.CaixaGeralService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CaixaGeralDTO listarCaixaGerals(){
        return caixaGeralService.listarCaixaGerais();
    }
    @GetMapping(path = "/data")
    @PreAuthorize("hasRole('ADMIN')")
    public CaixaGeralDTO listarCaixaGeralData(@RequestParam LocalDate data){
        return caixaGeralService.ListarCaixaGeralData(data);
    }

    @GetMapping(path = "/datas")
    @PreAuthorize("hasRole('ADMIN')")
    public CaixaGeralDTO listarCaixaGeralBetweenData(@RequestParam LocalDate data1, @RequestParam LocalDate data2){
        return caixaGeralService.ListarCaixaGeralBetweenData(data1, data2);
    }
}
