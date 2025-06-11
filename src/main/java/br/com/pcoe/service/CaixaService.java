package br.com.pcoe.service;

import br.com.pcoe.dto.CaixaDTO;
import br.com.pcoe.dto.CaixaMovimentoDTO;
import br.com.pcoe.dto.CaixaMovimentoEspecialidadeDTO;
import br.com.pcoe.enums.MensagensErrosGenericas;
import br.com.pcoe.exceptions.MensagemException;
import br.com.pcoe.mapper.CaixaMapper;
import br.com.pcoe.model.*;
import br.com.pcoe.repository.CaixaRepository;
import br.com.pcoe.repository.EspecialidadeRepository;
import br.com.pcoe.repository.FormaPagamentoRepository;
import br.com.pcoe.utils.UtilitariosGerais;
import br.com.pcoe.utils.UtilitariosParaCaixa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final UtilitariosParaCaixa utilitarios;
    private final UtilitariosGerais utilitariosGerais;

    @Autowired
    public CaixaService(CaixaRepository caixaRepository, CaixaMapper caixaMapper,
                        FormaPagamentoRepository formaPagamentoRepository,
                        EspecialidadeRepository especialidadeRepository,
                        UtilitariosParaCaixa utilitarios, UtilitariosGerais utilitariosGerais){
        this.caixaRepository = caixaRepository;
        this.caixaMapper = caixaMapper;
        this.formaPagamentoRepository = formaPagamentoRepository;
        this.especialidadeRepository = especialidadeRepository;
        this.utilitarios = utilitarios;
        this.utilitariosGerais = utilitariosGerais;
    }

    public List<CaixaDTO> listarTodosCaixas(){
        return caixaMapper.toDTO(caixaRepository.findAll());
    }

    //Lista caixas por uma data específica
    @Transactional(readOnly = true)
    public List<CaixaDTO> listarCaixasPorData(LocalDate data){
        return caixaMapper.toDTO(caixaRepository.findByData(data));
    }


    @Transactional(readOnly = true)
    public List<CaixaDTO> listarCaixaEntreDatas(LocalDate dataInicial, LocalDate dataFinal) {
        return caixaMapper.toDTO(caixaRepository.findByEntreData(dataInicial, dataFinal));
    }


    @Transactional(readOnly = true)
    public List<CaixaDTO> listarCaixaPorPeriodoUsuarioLogado(LocalDate dataInicial, LocalDate dataFinal) {
        Usuario usuario = utilitarios.getUsuarioLogado();
        return caixaMapper.toDTO(caixaRepository.
                findByUsuarioIdAndDataBetween(usuario.getId(), dataInicial, dataFinal));
    }

    @Transactional(readOnly = true)
    public CaixaDTO listarCaixaPorDataParaUsarioLogado(LocalDate data){
        Usuario usuario = utilitarios.getUsuarioLogado();
        Caixa caixas = caixaRepository.findByUsuarioIdAndData(usuario.getId(), data);

        return caixaMapper.toDTO(caixas);
    }

    //Lista todos os caixas criados, sendo que só pode ser visto pelo usuario que criou os caixas
    @Transactional(readOnly = true)
    public List<CaixaDTO> listarCaixasParaUsuarioLogado() {
        Usuario usuario = utilitarios.getUsuarioLogado();
        List<Caixa> caixas = caixaRepository.findByUsuarioId(usuario.getId());
        return caixaMapper.toDTO(caixas);
    }

    @Transactional(readOnly = true)
    public List<CaixaDTO> listarCaixasPorUsuario(Usuario usuario) {
        List<Caixa> caixas = caixaRepository.findByUsuarioId(usuario.getId());
        return caixaMapper.toDTO(caixas);
    }

    //Lista caixa por ID com base em quem criou ou se for Admin mostra qualquer caixa com base no ID
    @Transactional(readOnly = true)
    public CaixaDTO listarCaixaId(UUID id){
        //Verifica se existe um caixa com esse ID, se não existir lança exceção
        Caixa caixaExistente = utilitariosGerais.buscarEntidadeId(caixaRepository, id, "Caixa");

        //Verificar se é o mesmo usuário que criou o caixa ou é Admin, se não for lança exceção
        utilitarios.validarPermissaoOuLancarErro(caixaExistente,
                MensagensErrosGenericas.PERMISSAOUSUARIONEGADA.format("visualizar"));
        return caixaMapper.toDTO(caixaExistente);
    }


     //Listar Caixa aberto baseado no usuário logado
    @Transactional(readOnly = true)
    public List<CaixaDTO> listarCaixaAbertoParaUsuarioLogado(){
        List<CaixaDTO> listaDosCaixa = listarCaixasParaUsuarioLogado();
       return listaDosCaixa.stream()
               .filter(CaixaDTO::isAberto).collect(Collectors.toList());
    }

    @Transactional
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

    //Calcula o valor total do caixa
    private void calcularValorTotalCaixa(CaixaDTO caixaDTO){
        caixaDTO.getCaixaMovimentoEspecialidade().forEach(valor ->
                caixaDTO.setValorTotal(caixaDTO.getValorTotal().add(valor.getValorMovimento())));
    }

    private void validaValorTotalCaixa(CaixaDTO caixaDTO){

        BigDecimal valoresEspecialidade = caixaDTO.getCaixaMovimentoEspecialidade().stream()
                .map(CaixaMovimentoEspecialidadeDTO::getValorMovimento).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal valoresFormaPagamento = caixaDTO.getCaixaMovimento().stream()
                .map(CaixaMovimentoDTO::getValorMovimento).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (valoresEspecialidade.compareTo(valoresFormaPagamento) != 0) {
            throw new MensagemException("Valores de movimentos das especialidades e formas de pagamento não coincidem.");
        }
    }

    @Transactional
    public CaixaDTO movimentoCaixa(UUID id, CaixaDTO caixaDTO){
        //Acha o caixa com base no ID, caso não ache lança uma exceção
        Caixa caixaExistente = utilitariosGerais.buscarEntidadeId(caixaRepository, id, "Caixa");

        //Usuario que Criou o caixa
        Usuario usuario = caixaExistente.getUsuario();

        //Se não for o usuario que criou o caixa e não é Admin lança exceção
        utilitarios.validarPermissaoOuLancarErro(caixaExistente,
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

        //Verificar se os valores de valorMovimento são iguais nas duas listas
        validaValorTotalCaixa(caixaDTO);

        //Calcula o valor total do caixa
        calcularValorTotalCaixa(caixaDTO);

        caixaExistente.setValorTotal(caixaDTO.getValorTotal());
        caixaExistente.setValorRetirada(caixaDTO.getValorRetirada());
        caixaExistente.setValorQuebra(caixaDTO.getValorQuebra());
        caixaExistente.setObservacao(caixaDTO.getObservacao());

        Caixa caixaAtualizado = caixaRepository.save(caixaExistente);

        return caixaMapper.toDTO(caixaAtualizado);
    }

    @Transactional
    public void reabrirCaixa(UUID id) {
        //Verifica se existe um caixa com esse ID
        Caixa caixaExistente = utilitariosGerais.buscarEntidadeId(caixaRepository, id, "Caixa");
        if (caixaExistente.isAberto()){
            throw new MensagemException("Não pode reabrir um caixa que já está aberto!");
        }
        caixaExistente.setAberto(true);

        Caixa caixaSalvo = caixaRepository.save(caixaExistente);

        caixaMapper.toDTO(caixaSalvo);
    }

    @Transactional
    public CaixaDTO fecharCaixa(UUID id){
        //Verifica se existe um caixa com esse ID, se não existir lança exceção
        Caixa caixaExistente = utilitariosGerais.buscarEntidadeId(caixaRepository, id, "Caixa");

        utilitarios.validarPermissaoOuLancarErro(caixaExistente, MensagensErrosGenericas.PERMISSAOUSUARIONEGADA.format("fechar"));


        List<CaixaMovimento> caixaMovimento = caixaExistente.getCaixaMovimento();
        List<CaixaMovimentoEspecialidade> caixaMovimentoEspecialidade = caixaExistente.getCaixaMovimentoEspecialidade();

        if ((caixaMovimento == null || caixaMovimento.isEmpty()) &&
                (caixaMovimentoEspecialidade == null || caixaMovimentoEspecialidade.isEmpty())){
            throw new IllegalArgumentException("Não é possivel fechar um caixa sem movimentação, tente exclui-lo");
        }

        caixaExistente.setAberto(false);

        Caixa caixaSalvo = caixaRepository.save(caixaExistente);

        return caixaMapper.toDTO(caixaSalvo);
    }

    @Transactional
    public void deletarCaixa(UUID id){
        //Verifica se existe um caixa com esse ID
        Caixa caixaExistente = utilitariosGerais.buscarEntidadeId(caixaRepository, id, "Caixa");
        //Verificar se é o mesmo usuario que criou o caixa ou é Admin
        utilitarios.validarPermissaoOuLancarErro(caixaExistente, MensagensErrosGenericas.PERMISSAOUSUARIONEGADA.format("apagar"));

        List<CaixaMovimento> caixaMovimento = caixaExistente.getCaixaMovimento();
        List<CaixaMovimentoEspecialidade> caixaMovimentoEspecialidade = caixaExistente.getCaixaMovimentoEspecialidade();

        if((caixaMovimento != null && !caixaMovimento.isEmpty()) ||
                (caixaMovimentoEspecialidade != null && !caixaMovimentoEspecialidade.isEmpty())){
            throw new IllegalArgumentException("Não é possivel excluir um caixa que já teve uma movimentação");
        }
        caixaRepository.delete(caixaExistente);
    }
}
