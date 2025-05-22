package br.com.pcoe.controller;

import br.com.pcoe.dto.EspecialidadeDTO;
import br.com.pcoe.service.EspecialidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public List<EspecialidadeDTO> listarEspecialidades(){
        return especialidadeService.listarEspecialidades();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<EspecialidadeDTO> listarEspecialidadeId(@PathVariable UUID id){
        EspecialidadeDTO registro = especialidadeService.ListarEspecialidadeId(id);
        return ResponseEntity.ok().body(registro);
    }

    @PostMapping
    public ResponseEntity<EspecialidadeDTO> cadastrarEspecialidade(@RequestBody EspecialidadeDTO especialidade){
        EspecialidadeDTO novoEspecialidade = especialidadeService.cadastroEspecialidade(especialidade);
        return  ResponseEntity.status(HttpStatusCode.valueOf(201)).body(novoEspecialidade);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<EspecialidadeDTO> atualizarEspecialidade(@PathVariable UUID id, @RequestBody EspecialidadeDTO especialidade){
        EspecialidadeDTO especialidadeAtualizado = especialidadeService.atualizarEspecialidade(id,especialidade);
        return  ResponseEntity.ok().body(especialidadeAtualizado);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<EspecialidadeDTO> deletarEspecialidade(@PathVariable UUID id){
        especialidadeService.deletarEspecialidade(id);
        return ResponseEntity.noContent().build();
    }
}
