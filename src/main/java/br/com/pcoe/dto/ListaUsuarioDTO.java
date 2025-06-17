package br.com.pcoe.dto;

import br.com.pcoe.enums.UsuarioPermissao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioSemSenhaDTO {
    private UUID id;
    private String nome;
    private String email;
    private boolean ativo;
    private UsuarioPermissao permissao;

}
