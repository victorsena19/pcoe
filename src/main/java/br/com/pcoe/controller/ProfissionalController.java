package br.com.pcoe.controller;

import br.com.pcoe.dto.ProfissionalDTO;
import br.com.pcoe.service.ProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("profissional")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @Autowired
    public ProfissionalController(ProfissionalService profissionalService){
        this.profissionalService = profissionalService;
    }

    @GetMapping
    public List<ProfissionalDTO> listarProfissionais(){
        return profissionalService.listarProfissionais();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProfissionalDTO> listarProfissionalId(@PathVariable UUID id){
        ProfissionalDTO registro = profissionalService.listarProfissionalId(id);
        return ResponseEntity.ok().body(registro);
    }

    @PostMapping
    public ResponseEntity<ProfissionalDTO> cadastrarProfissional(@RequestBody ProfissionalDTO profissional){
        ProfissionalDTO novoProfissional = profissionalService.cadastroProfissional(profissional);
        return  ResponseEntity.status(HttpStatusCode.valueOf(201)).body(novoProfissional);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ProfissionalDTO> atualizarProfissional(@PathVariable UUID id, @RequestBody ProfissionalDTO profissional){
        ProfissionalDTO profissionalAtualizado = profissionalService.atualizarProfissional(id,profissional);
        return  ResponseEntity.ok().body(profissionalAtualizado);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ProfissionalDTO> deletarProfissional(@PathVariable UUID id){
        profissionalService.deletarProfissional(id);
        return ResponseEntity.noContent().build();
    }
}
