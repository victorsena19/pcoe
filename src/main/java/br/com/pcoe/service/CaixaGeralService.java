package br.com.pcoe.service;

import br.com.pcoe.dto.CaixaDTO;
import br.com.pcoe.dto.CaixaGeralDTO;
import br.com.pcoe.model.Usuario;
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

    @Autowired
    public CaixaGeralService(CaixaService caixaService){
        this.caixaService = caixaService;
    }

/**
 * Monta uma estrutura de CaixaGeralDTO para evitar repetição de código
 */
    private CaixaGeralDTO montarCaixaGeral(List<CaixaDTO> caixas){
        if(caixas == null || caixas.isEmpty()) return new CaixaGeralDTO(Collections.emptyList(), BigDecimal.ZERO);
        BigDecimal valorTotal = caixas.stream()
                .map(CaixaDTO::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CaixaGeralDTO(caixas, valorTotal);
    }

    /**
     * somando o valor total de todos os caixas da lista
     */
    public CaixaGeralDTO listarTodosCaixas(){
        List<CaixaDTO> listaDosCaixa = caixaService.listarTodosCaixas();
        return montarCaixaGeral(listaDosCaixa);
    }

/**
 * Retorna um resumo geral de todos os caixas por usuario que criou o caixa,
 * somando o valor total de todos os caixas da lista
*/
    @Transactional(readOnly = true)
    public CaixaGeralDTO listarCaixaPorUsuario(Usuario usuario) {
        List<CaixaDTO> caixas = caixaService.listarCaixasPorUsuario(usuario);
        return montarCaixaGeral(caixas);
    }

    /**
     * Retorna um resumo geral dos caixas de uma data específica, somando o valor total de todos os caixas da lista
     */
    @Transactional(readOnly = true)
    public CaixaGeralDTO listarCaixaGeralData(LocalDate data) {
        List<CaixaDTO> caixaPorData = caixaService.listarCaixasPorData(data);
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
