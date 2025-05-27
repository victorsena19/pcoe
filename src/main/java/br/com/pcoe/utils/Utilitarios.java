package br.com.pcoe.utils;

import br.com.pcoe.exceptions.MensagemException;
import br.com.pcoe.model.Caixa;
import br.com.pcoe.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Classe utilitária para acesso ao usuário logado e permissões.
**/
@Component
public final class Utilitarios {

    public Usuario getUsuarioLogado(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Usuario)){
            throw new IllegalStateException("Usuário não autenticado");
        }
        return (Usuario) auth.getPrincipal();
    }

    public boolean isAdmin(Usuario usuario){
        return usuario.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean naoEhCriadorNemAdmin(Caixa caixa){
        Usuario usuarioLogado = getUsuarioLogado();
        return !usuarioLogado.getId().equals(caixa.getUsuario().getId()) && !isAdmin(usuarioLogado);
    }

    public void validarPermissaoOuLancar(Caixa caixa, String mensagemErro){
        if(naoEhCriadorNemAdmin(caixa)) {
            throw new MensagemException(mensagemErro);
        }
    }
    public void validarSomenteAdmin(Caixa caixa, Usuario usuario){
        if (!isAdmin(usuario)){
            validarPermissaoOuLancar(caixa, "Somente Admin pode acessar esse recurso.");
        }
    }

    public void validarAcessoAdminParaCaixas(List<Caixa> caixas){
        if(caixas == null || caixas.isEmpty())return;

        caixas.forEach(cx -> validarSomenteAdmin(cx, cx.getUsuario()));
    }

}


