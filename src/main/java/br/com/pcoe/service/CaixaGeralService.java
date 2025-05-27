package br.com.pcoe.service;

import br.com.pcoe.dto.CaixaDTO;
import br.com.pcoe.dto.CaixaGeralDTO;
import br.com.pcoe.mapper.CaixaMapper;
import br.com.pcoe.model.Caixa;
import br.com.pcoe.utils.UtilitariosParaCaixa;
import br.com.pcoe.utils.UtilitariosParaCaixa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class CaixaGeralService {
    private final CaixaService caixaService;
    private final CaixaMapper caixaMapper;
    private final UtilitariosParaCaixa utilitarios;

    @Autowired
    public CaixaGeralService(CaixaService caixaService, CaixaMapper caixaMapper, UtilitariosParaCaixa utilitarios){
        this.caixaService = caixaService;
        this.caixaMapper = caixaMapper;
        this.utilitarios = utilitarios;
    }

/**
 * Monta uma estrutura de CaixaGeralDTO para evitar repetição de código
 *
 */
    private CaixaGeralDTO montarCaixaGeral(List<CaixaDTO> caixas){
        if(caixas == null || caixas.isEmpty()) return new CaixaGeralDTO(Collections.emptyList(), BigDecimal.ZERO);
        List<Caixa> caixa = caixaMapper.toEntity(caixas);
        utilitarios.validarAcessoAdminParaCaixas(caixa);
        BigDecimal valorTotal = caixas.stream()
                .map(CaixaDTO::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CaixaGeralDTO(caixas, valorTotal);
    }

/**
 * Retorna um resumo geral de todos os caixas, somando o valor total de todos os caixas da lista
*/
    @Transactional(readOnly = true)
    public CaixaGeralDTO listarCaixaGerais() {
        List<CaixaDTO> caixas = caixaService.listarCaixas();
        return montarCaixaGeral(caixas);
    }

    /**
     * Retorna um resumo geral dos caixas de uma data específica, somando o valor total de todos os caixas da lista
     */
    @Transactional(readOnly = true)
    public CaixaGeralDTO listarCaixaGeralData(LocalDate data) {
        List<CaixaDTO> caixaPorData = caixaService.listarCaixasData(data);
        return montarCaixaGeral(caixaPorData);
    }

    /**
     * Retorna um resumo geral dos caixas, baseado em duas datas, somando o valor total de todos os caixas da lista
     */
    @Transactional(readOnly = true)
    public CaixaGeralDTO listarCaixaGeralBetweenData(LocalDate data1, LocalDate data2) {
        List<CaixaDTO> buscandoCaixasEntreDuasDatas = caixaService.listarCaixaEntreDatas(data1, data2);
        return montarCaixaGeral(buscandoCaixasEntreDuasDatas);
    }
}
