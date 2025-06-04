package br.com.pcoe.controller;

import br.com.pcoe.dto.ListarUsuarioDTO;
import br.com.pcoe.dto.UsuarioDTO;
import br.com.pcoe.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ListarUsuarioDTO>> listarUsuarios(){
        return ResponseEntity.ok().body(usuarioService.listarUsuarios());
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<ListarUsuarioDTO> listarUsuarioId(@PathVariable UUID id){
        ListarUsuarioDTO registro = usuarioService.listarUsuarioId(id);
        return ResponseEntity.ok().body(registro);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@RequestBody UsuarioDTO usuario){
        UsuarioDTO novoUsuario = usuarioService.cadastroUsuario(usuario);
        return  ResponseEntity.status(HttpStatusCode.valueOf(201)).body(novoUsuario);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable UUID id, @RequestBody UsuarioDTO usuario){
        UsuarioDTO usuarioAtualizado = usuarioService.atualizarUsuario(id,usuario);
        return  ResponseEntity.ok().body(usuarioAtualizado);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<UsuarioDTO> deletarUsuario(@PathVariable UUID id){
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
