package br.com.pcoe.service;

import br.com.pcoe.dto.ListarUsuarioDTO;
import br.com.pcoe.dto.UsuarioDTO;
import br.com.pcoe.mapper.UsuarioMapper;
import br.com.pcoe.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.pcoe.repository.UsuarioRepository;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService implements UserDetailsService {

        private final UsuarioRepository usuarioRepository;
        private final UsuarioMapper usuarioMapper;

        @Autowired
        public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper){
            this.usuarioRepository = usuarioRepository;
            this.usuarioMapper = usuarioMapper;
        }

        public UserDetails listarEmail(String email){
            return usuarioRepository.findByEmail(email);
        }

        public List<ListarUsuarioDTO> listarUsuarios(){
            List<Usuario> usuario = usuarioRepository.findAll();
            return usuarioMapper.listaUsuarioToDTO(usuario);
        }

        public ListarUsuarioDTO ListarUsuarioId(UUID id){
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(()-> new IllegalArgumentException("Não foi possivel encontrar um usuario com esse ID: " + id));
            return usuarioMapper.listaUsuarioIdToDTO(usuario);
        }

        public UsuarioDTO cadastroUsuario(UsuarioDTO usuarioDTO){
            boolean email = usuarioRepository.existsByEmailIgnoreCase(usuarioDTO.getEmail());
            if(email){
                throw new IllegalArgumentException("Esse email já existe no sistema : " + usuarioDTO.getEmail());
            }
            String encryptedPassword = new BCryptPasswordEncoder().encode(usuarioDTO.getPassword());
            usuarioDTO.setPassword(encryptedPassword);
            Usuario novaUsuario = usuarioRepository.save(usuarioMapper.toEntity(usuarioDTO));
            return usuarioMapper.toDTO(novaUsuario);
        }

    public UsuarioDTO atualizarUsuario(UUID id, UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Não foi possivel encontrar um Usuario com esse ID: " + id));
        Usuario usuarioAtualizada = usuarioMapper.toEntity(usuarioDTO);
        usuarioAtualizada.setId(usuario.getId());
        Usuario salvarUsuario = usuarioRepository.save(usuarioAtualizada);
        return usuarioMapper.toDTO(salvarUsuario);
    }

    public void deletarUsuario(UUID id){
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Não foi possivel encontrar um Usuario com esse ID: " + id));
        usuarioRepository.delete(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username);
    }
}
