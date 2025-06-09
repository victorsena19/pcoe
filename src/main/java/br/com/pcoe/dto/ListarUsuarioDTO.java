package br.com.pcoe.dto;

import br.com.pcoe.enums.UsuarioPermissao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListarUsuarioDTO {
    private UUID id;
    private String nome;
    private String email;
    private UsuarioPermissao permissao;

}
