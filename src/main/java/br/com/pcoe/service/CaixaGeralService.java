package br.com.pcoe.service;

import br.com.pcoe.dto.CaixaDTO;
import br.com.pcoe.dto.CaixaGeralDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CaixaGeralService {
    private final CaixaService caixaService;

    @Autowired
    public CaixaGeralService(CaixaService caixaService){
        this.caixaService = caixaService;
    }

    public CaixaGeralDTO listarCaixaGerais() {
        List<CaixaDTO> caixas = caixaService.listarTodosCaixas();

        BigDecimal totalValor = caixas.stream()
                .map(CaixaDTO::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CaixaGeralDTO caixaGeral = new CaixaGeralDTO();
        caixaGeral.setCaixa(caixas);
        caixaGeral.setValorTotal(totalValor);

        return caixaGeral;
    }


    public CaixaGeralDTO ListarCaixaGeralData(LocalDate data) {
        List<CaixaDTO> caixaData = caixaService.listarCaixasData(data);

        BigDecimal totalValor = caixaData.stream()
                .map(CaixaDTO::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CaixaGeralDTO caixaGeral = new CaixaGeralDTO();
        caixaGeral.setCaixa(caixaData);
        caixaGeral.setValorTotal(totalValor);

        return caixaGeral;
    }

    public CaixaGeralDTO ListarCaixaGeralBetweenData(LocalDate data1, LocalDate data2) {
        List<CaixaDTO> caixas = caixaService.listarCaixaEntreDatas(data1, data2);

        BigDecimal totalValor = caixas.stream()
                .map(CaixaDTO::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CaixaGeralDTO caixaGeral = new CaixaGeralDTO();
        caixaGeral.setCaixa(caixas);
        caixaGeral.setValorTotal(totalValor);
        return caixaGeral;
    }
}
