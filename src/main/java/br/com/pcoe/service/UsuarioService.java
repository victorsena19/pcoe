package br.com.pcoe.service;

import br.com.pcoe.dto.ListarUsuarioDTO;
import br.com.pcoe.dto.UsuarioDTO;
import br.com.pcoe.exceptions.MensagemException;
import br.com.pcoe.mapper.UsuarioMapper;
import br.com.pcoe.model.Usuario;
import br.com.pcoe.repository.CaixaRepository;
import br.com.pcoe.utils.UtilitariosGerais;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.pcoe.repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 *  Classe com CRUD de Usuário com as devidas validações para cada função
 */
@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final CaixaRepository caixaRepository;
    private final UsuarioMapper usuarioMapper;
    private final UtilitariosGerais utilitariosGerais;


    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, CaixaRepository caixaRepository, UsuarioMapper usuarioMapper, UtilitariosGerais utilitariosGerais){
        this.usuarioRepository = usuarioRepository;
        this.caixaRepository = caixaRepository;
        this.usuarioMapper = usuarioMapper;
        this.utilitariosGerais = utilitariosGerais;
    }

    @Transactional(readOnly = true)
    public UserDetails listarEmail(String email){
        return usuarioRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<ListarUsuarioDTO> listarUsuarios(){
        List<Usuario> usuario = usuarioRepository.findAll();
        return usuarioMapper.listaUsuarioToDTO(usuario);
    }

    @Transactional(readOnly = true)
    public ListarUsuarioDTO listarUsuarioId(UUID id){
        Usuario usuario = utilitariosGerais
                .buscarEntidadeId(usuarioRepository, id, "Usuario");
        return usuarioMapper.listaUsuarioIdToDTO(usuario);
    }

    @Transactional
    public UsuarioDTO cadastroUsuario(UsuarioDTO usuarioDTO){
        boolean email = usuarioRepository.existsByEmailIgnoreCase(usuarioDTO.getEmail());
        if(email){
            throw new MensagemException("Esse email já existe no sistema : " + usuarioDTO.getEmail());
        }
        //Encriptografa a senha antes de salvar
        String encryptedPassword = new BCryptPasswordEncoder().encode(usuarioDTO.getPassword());
        usuarioDTO.setPassword(encryptedPassword);
        Usuario novaUsuario = usuarioRepository.save(usuarioMapper.toEntity(usuarioDTO));
        return usuarioMapper.toDTO(novaUsuario);
    }

    @Transactional
    public UsuarioDTO atualizarUsuario(UUID id, UsuarioDTO usuarioDTO){
        Usuario usuario = utilitariosGerais
                .buscarEntidadeId(usuarioRepository, id, "Usuário");
        Usuario usuarioAtualizada = usuarioMapper.toEntity(usuarioDTO);
        usuarioAtualizada.setId(usuario.getId());
        Usuario salvarUsuario = usuarioRepository.save(usuarioAtualizada);
        return usuarioMapper.toDTO(salvarUsuario);
    }

    @Transactional
    public void deletarUsuario(UUID id){
        Usuario usuario = utilitariosGerais
                .buscarEntidadeId(usuarioRepository, id, "Usuário");
        boolean possuiCaixa = caixaRepository.existsByUsuarioId(usuario.getId());
        if (possuiCaixa){
            throw new MensagemException("Não é possível apagar um usuário com caixa registrado, apenas deixe-o inativo!");
        }
        usuarioRepository.delete(usuario);
    }

    //Sobrescrita de método do UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username);
    }
}
