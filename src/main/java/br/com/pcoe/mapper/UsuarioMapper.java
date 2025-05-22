package br.com.pcoe.mapper;

import br.com.pcoe.dto.UsuarioDTO;
import br.com.pcoe.model.Usuario;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioDTO toDTO(Usuario usuario);
    List<UsuarioDTO> toDTO(List<Usuario> usuarios);
    Usuario toEntity(UsuarioDTO usuarioDTO);
}
