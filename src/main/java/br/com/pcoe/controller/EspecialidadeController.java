package br.com.pcoe.controller;

import br.com.pcoe.dto.EspecialidadeDTO;
import br.com.pcoe.service.EspecialidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("especialidade")
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;

    @Autowired
    public EspecialidadeController(EspecialidadeService especialidadeService){
        this.especialidadeService = especialidadeService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<EspecialidadeDTO>> listarEspecialidades(){
        return ResponseEntity.ok().body(especialidadeService.listarEspecialidades());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<EspecialidadeDTO> listarEspecialidadeId(@PathVariable UUID id){
        EspecialidadeDTO registro = especialidadeService.listarEspecialidadeId(id);
        return ResponseEntity.ok().body(registro);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/nome")
    public ResponseEntity<EspecialidadeDTO> listarEspecialidadeId(@RequestParam String nome){
        EspecialidadeDTO registro = especialidadeService.listarEspecialidadePorNome(nome);
        return ResponseEntity.ok().body(registro);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EspecialidadeDTO> cadastrarEspecialidade(@RequestBody EspecialidadeDTO especialidade){
        EspecialidadeDTO novoEspecialidade = especialidadeService.cadastroEspecialidade(especialidade);
        return  ResponseEntity.status(HttpStatusCode.valueOf(201)).body(novoEspecialidade);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<EspecialidadeDTO> atualizarEspecialidade(@PathVariable UUID id, @RequestBody EspecialidadeDTO especialidade){
        EspecialidadeDTO especialidadeAtualizado = especialidadeService.atualizarEspecialidade(id,especialidade);
        return  ResponseEntity.ok().body(especialidadeAtualizado);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<EspecialidadeDTO> deletarEspecialidade(@PathVariable UUID id){
        especialidadeService.deletarEspecialidade(id);
        return ResponseEntity.noContent().build();
    }
}
