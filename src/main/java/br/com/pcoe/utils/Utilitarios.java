package br.com.pcoe.utils;

import br.com.pcoe.model.Caixa;
import br.com.pcoe.model.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


/**
 * Classe utilitária para acesso ao usuário logado e permissões.
**/
@Component
public class Utilitarios {

    public Usuario getUsuarioLogado(){
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public boolean isAdmin(Usuario usuario){
        return usuario.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean naoEhCriadorNemAdmin(Caixa caixa){
        Usuario usuarioLogado = getUsuarioLogado();
        return !usuarioLogado.getId().equals(caixa.getUsuario().getId()) && !isAdmin(usuarioLogado);
    }

}


