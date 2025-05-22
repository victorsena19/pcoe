package br.com.pcoe.service;

import br.com.pcoe.dto.CaixaDTO;
import br.com.pcoe.mapper.CaixaMapper;
import br.com.pcoe.model.*;
import br.com.pcoe.repository.CaixaRepository;
import br.com.pcoe.repository.EspecialidadeRepository;
import br.com.pcoe.repository.FormaPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CaixaService {
    private final CaixaRepository caixaRepository;
    private final CaixaMapper caixaMapper;
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final EspecialidadeRepository especialidadeRepository;

    @Autowired
    public CaixaService(CaixaRepository caixaRepository, CaixaMapper caixaMapper,
                        FormaPagamentoRepository formaPagamentoRepository,
                        EspecialidadeRepository especialidadeRepository){
        this.caixaRepository = caixaRepository;
        this.caixaMapper = caixaMapper;
        this.formaPagamentoRepository = formaPagamentoRepository;
        this.especialidadeRepository = especialidadeRepository;
    }

    public List<CaixaDTO> listarTodosCaixas() {
            return caixaMapper.toDTO(caixaRepository.findAll());
    }

    public List<CaixaDTO> listarCaixasData(LocalDate data){
        return caixaMapper.toDTO(caixaRepository.findByData(data));
    }

    public List<CaixaDTO> listarCaixaEntreDatas(LocalDate data1, LocalDate data2) {
        return caixaMapper.toDTO(caixaRepository.findByBetweenData(data1, data2));
    }

    public List<CaixaDTO> listarCaixas() {
        // Obtém o usuário autenticado
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = usuario.getId();

        // Verifica a role do usuário
        boolean isAdmin = usuario.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        // Filtra os caixas com base na role
        List<Caixa> caixas;
        if (isAdmin) {
            caixas = caixaRepository.findAll();
        } else {
            caixas = caixaRepository.findByUsuariorId(userId);
        }

        // Converte para DTO e retorna
        return caixaMapper.toDTO(caixas);
    }

    public CaixaDTO ListarCaixaId(UUID id){
        Caixa caixaId = caixaRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Não foi possivel encontrar um caixa com esse ID: " + id));
        if(caixaId.getUsuario().getId().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal()) ||
                SecurityContextHolder
                        .getContext()
                        .getAuthentication().getAuthorities()
                        .stream().toList().get(0)
                        .getAuthority().equals("ROLE_ADMIN")){
            return caixaMapper.toDTO(caixaId);
        }
        throw new RuntimeException("Não foi possivel ver esse caixa pois voce não é Admin ou não foi você que lançou esse caixa");
    }

    public CaixaDTO abrirCaixa() {
        CaixaDTO caixaDTO = new CaixaDTO();
        caixaDTO.setData(LocalDate.now());
        caixaDTO.setValorTotal(BigDecimal.ZERO);
        caixaDTO.setValorQuebra(BigDecimal.ZERO);
        caixaDTO.setValorRetirada(BigDecimal.ZERO);
        caixaDTO.setAberto(true);
        caixaDTO.setCaixaMovimento(List.of());
        Caixa novaCaixa = caixaMapper.toEntity(caixaDTO);
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        novaCaixa.setUsuario(usuario);
        caixaRepository.save(novaCaixa);
        return caixaMapper.toDTO(novaCaixa);
    }

    public CaixaDTO movimentoCaixa(UUID id, CaixaDTO caixaDTO){
        Caixa caixa = caixaRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Não foi possivel encontrar um caixa com esse ID: " + id));
        Caixa novoCaixa = caixaMapper.toEntity(caixaDTO);

        List<CaixaMovimento> listaMovimento = novoCaixa.getCaixaMovimento().stream().map((movimento) -> {
            FormaPagamento formaPagamento = formaPagamentoRepository.findById(movimento.getFormaPagamento().getId())
                        .orElseThrow(()-> new IllegalArgumentException("Não foi possivel encontrar uma forma de pagamento com esse ID: " + movimento.getFormaPagamento().getId()));
            CaixaMovimento caixaMovimento = new CaixaMovimento();
            caixaMovimento.setFormaPagamento(formaPagamento);
            caixaMovimento.setValorMovimento(movimento.getValorMovimento());
            return caixaMovimento;
        }).toList();

        List<CaixaMovimentoEspecialidade> listaMovimentoEspecialidade = novoCaixa.getCaixaMovimentoEspecialidade().stream().map((movimentoEspecialidade) -> {
            Especialidade especialidade = especialidadeRepository.findById(movimentoEspecialidade.getEspecialidade().getId())
                    .orElseThrow(()-> new IllegalArgumentException("Não foi possivel encontrar uma especialidade com esse ID: " + movimentoEspecialidade.getEspecialidade().getId()));
            CaixaMovimentoEspecialidade caixaMovimentoEspecialidade = new CaixaMovimentoEspecialidade();
            caixaMovimentoEspecialidade.setEspecialidade(especialidade);
            caixaMovimentoEspecialidade.setValorMovimento(movimentoEspecialidade.getValorMovimento());
            return caixaMovimentoEspecialidade;
        }).toList();

        if(caixa.getUsuario().getId().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal()) ||
                !SecurityContextHolder
                        .getContext()
                        .getAuthentication().getAuthorities()
                        .stream().toList().get(0)
                        .getAuthority().equals("ROLE_ADMIN")){
            throw new RuntimeException("Não foi possivel movimentar esse caixa pois voce não é Admin ou não foi você que lançou esse caixa");
        }

        novoCaixa.setCaixaMovimento(listaMovimento);
        novoCaixa.setId(caixa.getId());

        caixaRepository.save(novoCaixa);
        return caixaMapper.toDTO(novoCaixa);
    }

    public CaixaDTO fecharCaixa(UUID id, CaixaDTO caixaDTO){
        Caixa caixa = caixaRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Não foi possivel encontrar um caixa com esse ID: " + id));
        if(caixa.getUsuario().getId().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal()) ||
                !SecurityContextHolder
                        .getContext()
                        .getAuthentication().getAuthorities()
                        .stream().toList().get(0)
                        .getAuthority().equals("ROLE_ADMIN")){
            throw new RuntimeException("Não foi possivel fechar esse caixa pois voce não é Admin ou não foi você que lançou esse caixa");
        }
        caixaDTO.setAberto(false);
        Caixa caixaAtualizada = caixaMapper.toEntity(caixaDTO);
        caixaAtualizada.setId(caixa.getId());
        Caixa salvarCaixa = caixaRepository.save(caixaAtualizada);
        return caixaMapper.toDTO(salvarCaixa);
    }

    public void deletarCaixa(UUID id){
        Caixa caixa = caixaRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Não foi possivel encontrar um caixa com esse ID: " + id));
        if(!caixa.getUsuario().getId().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal()) ||
                !SecurityContextHolder
                        .getContext()
                        .getAuthentication().getAuthorities()
                        .stream().toList().get(0)
                        .getAuthority().equals("ROLE_ADMIN")){
                throw new RuntimeException("Não foi possivel ver esse caixa pois voce não é Admin ou não foi você que lançou esse caixa");
        }
        caixaRepository.delete(caixa);
    }
}
