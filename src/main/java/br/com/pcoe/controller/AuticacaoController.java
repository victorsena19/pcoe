package br.com.pcoe.controller;

import br.com.pcoe.dto.AutenticacaoDTO;
import br.com.pcoe.dto.LoginRespostaDTO;
import br.com.pcoe.model.Usuario;
import br.com.pcoe.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuticacaoController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    public AuticacaoController(AuthenticationManager authenticationManager,
                               TokenService tokenService){
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AutenticacaoDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.gerarTokenJwt((Usuario) auth.getPrincipal());
        var permissao = auth.getAuthorities().stream().toList().get(0).getAuthority();
        return ResponseEntity.ok(new LoginRespostaDTO(token, permissao));
    }

}
