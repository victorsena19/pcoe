package br.com.pcoe.service;

import br.com.pcoe.dto.RequisicaoSolicitacaoReaberturaDTO;
import br.com.pcoe.dto.RespostaSolicitacaoReaberturaDTO;
import br.com.pcoe.enums.StatusSolicitacao;
import br.com.pcoe.exceptions.MensagemException;
import br.com.pcoe.mapper.SolicitacaoReaberturaCaixaMapper;
import br.com.pcoe.model.Caixa;
import br.com.pcoe.model.SolicitacaoReaberturaCaixa;
import br.com.pcoe.model.Usuario;
import br.com.pcoe.repository.CaixaRepository;
import br.com.pcoe.repository.SolicitacaoReaberturaCaixaRepository;
import br.com.pcoe.utils.UtilitariosGerais;
import br.com.pcoe.utils.UtilitariosParaCaixa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class SolicitacaoReaberturaCaixaService {

    private final SolicitacaoReaberturaCaixaRepository solicitacaoReaberturaCaixaRepository;
    private final SolicitacaoReaberturaCaixaMapper solicitacaoReaberturaCaixaMapper;
    private final UtilitariosParaCaixa utilitariosParaCaixa;
    private final UtilitariosGerais utilitariosGerais;
    private final CaixaRepository caixaRepository;
    private final CaixaService caixaService;

    @Autowired
    public SolicitacaoReaberturaCaixaService(SolicitacaoReaberturaCaixaRepository solicitacaoReaberturaCaixaRepository,
                                             SolicitacaoReaberturaCaixaMapper solicitacaoReaberturaCaixaMapper,
                                             UtilitariosParaCaixa utilitariosParaCaixa, UtilitariosGerais utilitariosGerais,
                                             CaixaRepository caixaRepository, CaixaService caixaService){
        this.solicitacaoReaberturaCaixaRepository = solicitacaoReaberturaCaixaRepository;
        this.solicitacaoReaberturaCaixaMapper = solicitacaoReaberturaCaixaMapper;
        this.utilitariosParaCaixa = utilitariosParaCaixa;
        this.utilitariosGerais = utilitariosGerais;
        this.caixaRepository = caixaRepository;
        this.caixaService = caixaService;
    }

    @Transactional(readOnly = true)
    public List<RespostaSolicitacaoReaberturaDTO> listarStatusReaberturaParaSolicitante(StatusSolicitacao statusReabertura){
        Usuario usuarioLogado = utilitariosParaCaixa.getUsuarioLogado();
        List<SolicitacaoReaberturaCaixa>  listaCaixaPorStatus = solicitacaoReaberturaCaixaRepository
                .findByStatusReaberturaAndSolicitanteId(statusReabertura, usuarioLogado.getId());
        return solicitacaoReaberturaCaixaMapper.toDTOResposta(listaCaixaPorStatus);
    }

    @Transactional(readOnly = true)
    public List<RequisicaoSolicitacaoReaberturaDTO> listarRequisicaoReaberturaStatus(StatusSolicitacao statusReabertura){
        List<SolicitacaoReaberturaCaixa>  listaCaixaPorStatus = solicitacaoReaberturaCaixaRepository
                .findByStatusReabertura(statusReabertura);
        return solicitacaoReaberturaCaixaMapper.toDTORequisicao(listaCaixaPorStatus);
    }

    @Transactional
    private void solicitarReaberturaCaixa(
            RequisicaoSolicitacaoReaberturaDTO solicitacaoReaberturaDTO){

        Caixa caixa = utilitariosGerais.buscarEntidadeId(caixaRepository,
                solicitacaoReaberturaDTO.getCaixa().getId(),
                "Caixa" );

        SolicitacaoReaberturaCaixa solicitacaoReaberturaCaixa = solicitacaoReaberturaCaixaMapper
                .toEntityRequisicao(solicitacaoReaberturaDTO);

            solicitacaoReaberturaCaixa.setStatusReabertura(StatusSolicitacao.PENDENTE);
            solicitacaoReaberturaCaixa.setSolicitante(utilitariosParaCaixa.getUsuarioLogado());
            solicitacaoReaberturaCaixa.setDataSolicitacao(LocalDate.now());
            solicitacaoReaberturaCaixa.setCaixa(caixa);
            SolicitacaoReaberturaCaixa novaSolicitacaoReaberturaCaixa = solicitacaoReaberturaCaixaRepository
                    .save(solicitacaoReaberturaCaixa);

        solicitacaoReaberturaCaixaMapper.toDTORequisicao(novaSolicitacaoReaberturaCaixa);

    }

    /**
     * Valida */
    @Transactional
    public void processaReaberturaCaixas(RequisicaoSolicitacaoReaberturaDTO dto){
        Caixa caixaExistente = utilitariosGerais.buscarEntidadeId(caixaRepository,
                dto.getCaixa().getId(),
                "Caixa");
        Usuario usuario = utilitariosParaCaixa.getUsuarioLogado();

        //Verificar se é mesmo usuario que criou o caixa
        boolean mesmoUsuario = usuario.getId().equals(caixaExistente.getUsuario().getId());

        //Verificar se é mesmo dia
        boolean mesmoDia = caixaExistente.getData().equals(LocalDate.now());

        boolean podeReabrir = utilitariosParaCaixa.isAdmin(usuario) || (mesmoUsuario && mesmoDia);

        //Verifica se usuario é Admin ou é mesmo usuario e mesmo dia
        if (podeReabrir){
            caixaService.reabrirCaixa(caixaExistente.getId());
        }
        else{
            solicitarReaberturaCaixa(dto);
        }

    }

    @Transactional
    public RespostaSolicitacaoReaberturaDTO atualizarRespostaReaberturaCaixa(UUID id,
                                            RespostaSolicitacaoReaberturaDTO respostaSolicitacaoReaberturaDTO){
       SolicitacaoReaberturaCaixa solicitacaoReabertura = utilitariosGerais
               .buscarEntidadeId(solicitacaoReaberturaCaixaRepository,id,"Solicitacao Abertura Caixa");

       if (solicitacaoReabertura.getStatusReabertura().equals(StatusSolicitacao.APROVADO) ||
           solicitacaoReabertura.getStatusReabertura().equals(StatusSolicitacao.NEGADO)){
           throw new MensagemException("Essa solicitação já foi respondida");
       }

        solicitacaoReabertura.setStatusReabertura(respostaSolicitacaoReaberturaDTO.getStatusReabertura());
        solicitacaoReabertura.setDataResposta(LocalDate.now());
        solicitacaoReabertura.setAdministrador(utilitariosParaCaixa.getUsuarioLogado());

       SolicitacaoReaberturaCaixa novaSolicitacaoReaberturaCaixa = solicitacaoReaberturaCaixaRepository
               .save(solicitacaoReabertura);

       if (respostaSolicitacaoReaberturaDTO.getStatusReabertura() == StatusSolicitacao.APROVADO){
           caixaService.reabrirCaixa(novaSolicitacaoReaberturaCaixa.getCaixa().getId());
        }

       return solicitacaoReaberturaCaixaMapper.toDTOResposta(novaSolicitacaoReaberturaCaixa);

    }

}
