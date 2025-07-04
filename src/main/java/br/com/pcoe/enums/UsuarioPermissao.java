package br.com.pcoe.enums;

import lombok.Getter;

@Getter
public enum UsuarioPermissao {
    ADMIN("admin"),
    USER("user");

    private final String permissao;

    UsuarioPermissao(String permissao){
        this.permissao = permissao;
    }

}
