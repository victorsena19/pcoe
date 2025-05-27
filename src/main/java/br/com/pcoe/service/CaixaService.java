package br.com.pcoe.service;

import br.com.pcoe.dto.CaixaDTO;
import br.com.pcoe.enuns.MensagensErrosGenericas;
import br.com.pcoe.mapper.CaixaMapper;
import br.com.pcoe.model.*;
import br.com.pcoe.repository.CaixaRepository;
import br.com.pcoe.repository.EspecialidadeRepository;
import br.com.pcoe.repository.FormaPagamentoRepository;
import br.com.pcoe.utils.Utilitarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CaixaService {
    private final CaixaRepository caixaRepository;
    private final CaixaMapper caixaMapper;
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final Utilitarios utilitarios;

    @Autowired
    public CaixaService(CaixaRepository caixaRepository, CaixaMapper caixaMapper,
                        FormaPagamentoRepository formaPagamentoRepository,
                        EspecialidadeRepository especialidadeRepository,
                        Utilitarios utilitarios){
        this.caixaRepository = caixaRepository;
        this.caixaMapper = caixaMapper;
        this.formaPagamentoRepository = formaPagamentoRepository;
        this.especialidadeRepository = especialidadeRepository;
        this.utilitarios = utilitarios;
    }

   ;

    //Lista caixas por uma data específica
    public List<CaixaDTO> listarCaixasData(LocalDate data){
        return caixaMapper.toDTO(caixaRepository.findByData(data));
    }

    //Lista caixas entre duas datas específicas
    public List<CaixaDTO> listarCaixaEntreDatas(LocalDate data1, LocalDate data2) {
        return caixaMapper.toDTO(caixaRepository.findByBetweenData(data1, data2));
    }

    //Lista todos os caixas criados, sendo que só pode ser visto pelo usuario que criou os caixas ou Admin que pode ver todos
    public List<CaixaDTO> listarCaixas() {
        Usuario usuario = utilitarios.getUsuarioLogado();

        //Lista os caixas com base nas permisssoes
        List<Caixa> caixas;
        if (utilitarios.isAdmin(usuario)) {
            caixas = caixaRepository.findAll();
        } else {
            caixas = caixaRepository.findByUsuariorId(usuario.getId());
        }

        // Converte para DTO e retorna
        return caixaMapper.toDTO(caixas);
    }

    //Lista caixa por ID com base em quem criou ou se for Admin mostra qualquer caixa com base no ID
    public CaixaDTO listarCaixaId(UUID id){
        //Verifica se existe um caixa com esse ID, se não existir lança exceção
        Caixa caixa = caixaRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException(MensagensErrosGenericas.IDNAOENCONTRADO.getErro() + id));

        //Verificar se é o mesmo usuario que criou o caixa ou é Admin, se não for lança exceção
        utilitarios.validarPermissaoOuLancar(caixa,
                MensagensErrosGenericas.PERMISSAOUSUARIONEGADA.format("visualizar"));
        return caixaMapper.toDTO(caixa);
    }

    //Abre um caixa com base na data atual e valores iniciais zerados
    public CaixaDTO abrirCaixa() {
        Usuario usuario = utilitarios.getUsuarioLogado();

        List<Caixa> caixasDoDia = caixaRepository.findByData((LocalDate.now()));

        //Ve se  usario logado tem um caixa aberto na data atual
        boolean usuarioComCaixaCriado = caixasDoDia.stream()
                .anyMatch(user -> user.getUsuario().getId().equals(usuario.getId()));

        // Se for verdade lança exceção
        if (usuarioComCaixaCriado){
            throw new IllegalArgumentException("Esse usuário já tem um caixa criado na data atual!");
        }

        //Valores iniciais do caixa
        CaixaDTO caixaDTO = new CaixaDTO();
        caixaDTO.setData(LocalDate.now());
        caixaDTO.setValorTotal(BigDecimal.ZERO);
        caixaDTO.setValorQuebra(BigDecimal.ZERO);
        caixaDTO.setValorRetirada(BigDecimal.ZERO);
        caixaDTO.setAberto(true);
        caixaDTO.setCaixaMovimento(new ArrayList<>());
        caixaDTO.setCaixaMovimentoEspecialidade(new ArrayList<>());
        Caixa novaCaixa = caixaMapper.toEntity(caixaDTO);
        novaCaixa.setUsuario(usuario);

        caixaRepository.save(novaCaixa);

        return caixaMapper.toDTO(novaCaixa);
    }

    //Movimentação de um caixa depois de aberto
    public CaixaDTO movimentoCaixa(UUID id, CaixaDTO caixaDTO){
        //Acha o caixa com base no ID
        Caixa caixaExistente = caixaRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException(MensagensErrosGenericas.IDNAOENCONTRADO.format("caixa") + id));

        //Usuario que Criou o caixa
        Usuario usuario = caixaExistente.getUsuario();

        //Se não for o usuario que criou o caixa e não é Admin lança exceção
        utilitarios.validarPermissaoOuLancar(caixaExistente,
                MensagensErrosGenericas.PERMISSAOUSUARIONEGADA.format("movimentar"));

        //Percorre a Lista de Movimento com o intuito de saber as formas de pagamentos e os valores
        List<CaixaMovimento> listaMovimento = caixaDTO.getCaixaMovimento().stream().map((movimento) -> {
            FormaPagamento formaPagamento = formaPagamentoRepository.findById(movimento.getFormaPagamento().getId())
                        .orElseThrow(()->
                                new IllegalArgumentException(MensagensErrosGenericas.IDNAOENCONTRADO.format("forma de pagamento")
                                        + movimento.getFormaPagamento().getId()));
            CaixaMovimento caixaMovimento = new CaixaMovimento();
            caixaMovimento.setFormaPagamento(formaPagamento);
            caixaMovimento.setValorMovimento(movimento.getValorMovimento());
            caixaMovimento.setCaixa(caixaExistente);
            return caixaMovimento;
        }).collect(Collectors.toList());;

        //Seta os valores da Movimentação no caixa
        caixaExistente.setCaixaMovimento(listaMovimento);

        //Percorre a Lista de Movimento Especialidade com o intuito de saber as Especialidades e os valores
        List<CaixaMovimentoEspecialidade> listaMovimentoEspecialidade = caixaDTO.getCaixaMovimentoEspecialidade().stream().map((movimentoEspecialidade) -> {
            Especialidade especialidade = especialidadeRepository.findById(movimentoEspecialidade.getEspecialidade().getId())
                    .orElseThrow(()-> new IllegalArgumentException(MensagensErrosGenericas.IDNAOENCONTRADO.format("especialidade") + movimentoEspecialidade.getEspecialidade().getId()));
            CaixaMovimentoEspecialidade caixaMovimentoEspecialidade = new CaixaMovimentoEspecialidade();
            caixaMovimentoEspecialidade.setEspecialidade(especialidade);
            caixaMovimentoEspecialidade.setValorMovimento(movimentoEspecialidade.getValorMovimento());
            caixaMovimentoEspecialidade.setCaixa(caixaExistente);
            return caixaMovimentoEspecialidade;
        }).collect(Collectors.toList());

        //Seta os valores da Movimentação Especialidade no caixa
        caixaExistente.setCaixaMovimentoEspecialidade(listaMovimentoEspecialidade);

        //Seta os dados no caixa
        caixaExistente.setValorTotal(caixaDTO.getValorTotal());
        caixaExistente.setValorRetirada(caixaDTO.getValorRetirada());
        caixaExistente.setValorQuebra(caixaDTO.getValorQuebra());
        caixaExistente.setObservacao(caixaDTO.getObservacao());

        Caixa caixaAtualizado = caixaRepository.save(caixaExistente);

        return caixaMapper.toDTO(caixaAtualizado);
    }

    //Reabre o caixa se estiver fechado passando o ID do caixa
    public CaixaDTO reabrirCaixa(UUID id) {
        //Verifica se existe um caixa com esse ID
        Caixa caixa = caixaRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException(MensagensErrosGenericas.IDNAOENCONTRADO.format("caixa") + id));
        if (caixa.isAberto()){
            throw new IllegalArgumentException("Não pode reabrir um caixa que já está aberto!");
        }
        //Pega Usuario logado
        Usuario usuario = utilitarios.getUsuarioLogado();

        //Verificar se é mesmo usuario que criou o caixa
        boolean isMesmoUsuario = usuario.getId().equals(caixa.getUsuario().getId());

        //Verificar se é mesmo dia
        boolean isMesmoDia = caixa.getData().equals(LocalDate.now());

        //Verifica se usuario é Admin ou é mesmo usuario e mesmo dia
        if (!(utilitarios.isAdmin(usuario) || (isMesmoUsuario && isMesmoDia))){
            throw new RuntimeException(MensagensErrosGenericas.PERMISSAOUSUARIONEGADA.format("reabrir"));
        }

        caixa.setAberto(true);

        Caixa caixaSalvo = caixaRepository.save(caixa);

        return caixaMapper.toDTO(caixaSalvo);
    }

    public CaixaDTO fecharCaixa(UUID id){
        //Verifica se existe um caixa com esse ID, se não existir lança exceção
        Caixa caixa = caixaRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException(MensagensErrosGenericas.IDNAOENCONTRADO.format("caixa") + id));

        utilitarios.validarPermissaoOuLancar(caixa, MensagensErrosGenericas.PERMISSAOUSUARIONEGADA.format("fechar"));


        List<CaixaMovimento> caixaMovimento = caixa.getCaixaMovimento();
        List<CaixaMovimentoEspecialidade> caixaMovimentoEspecialidade = caixa.getCaixaMovimentoEspecialidade();

        if ((caixaMovimento == null || caixaMovimento.isEmpty()) &&
                (caixaMovimentoEspecialidade == null || caixaMovimentoEspecialidade.isEmpty())){
            throw new IllegalArgumentException("Não é possivel fechar um caixa sem movimentação, tente exclui-lo");
        }

        caixa.setAberto(false);

        Caixa caixaSalvo = caixaRepository.save(caixa);

        return caixaMapper.toDTO(caixaSalvo);
    }

    public void deletarCaixa(UUID id){
        //Verifica se existe um caixa com esse ID
        Caixa caixa = caixaRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException(MensagensErrosGenericas.IDNAOENCONTRADO.format("caixa") + id));
        //Verificar se é o mesmo usuario que criou o caixa ou é Admin
        utilitarios.validarPermissaoOuLancar(caixa, MensagensErrosGenericas.PERMISSAOUSUARIONEGADA.format("apagar"));

        List<CaixaMovimento> caixaMovimento = caixa.getCaixaMovimento();
        List<CaixaMovimentoEspecialidade> caixaMovimentoEspecialidade = caixa.getCaixaMovimentoEspecialidade();

        if((caixaMovimento != null && !caixaMovimento.isEmpty()) ||
                (caixaMovimentoEspecialidade != null && !caixaMovimentoEspecialidade.isEmpty())){
            throw new IllegalArgumentException("Não é possivel excluir um caixa que já teve uma movimentação");
        }
        caixaRepository.delete(caixa);
    }
}
