package br.com.pcoe.gerador_pdf;

import br.com.pcoe.model.FolhaPagamento;
import br.com.pcoe.repository.FolhaPagamentoRepository;
import br.com.pcoe.utils.MoedaUtil;
import br.com.pcoe.utils.UtilitariosGerais;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.UUID;

/**
 * Implementar a geração de PDF da folha de pagamento
 */
@Component
public class FolhaPagamentoPDF {
    private final UtilitariosGerais utilitariosGerais;
    private final FolhaPagamentoRepository folhaPagamentoRepository;

    @Autowired
    public FolhaPagamentoPDF(UtilitariosGerais utilitariosGerais,
                             FolhaPagamentoRepository folhaPagamentoRepository) {
        this.utilitariosGerais = utilitariosGerais;
        this.folhaPagamentoRepository = folhaPagamentoRepository;
    }
    public byte[] gerarReciboFolhaPagamentoPDF(UUID id){
        FolhaPagamento folha = utilitariosGerais.
                buscarEntidadeId(folhaPagamentoRepository,id, "Folha de Pagamento");
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            String nomeProfissional = folha.getProfissional().getNome();
            String cpfProfissional = folha.getProfissional().getCpf();
            String nomeEmpresa = "Odonto Excellence Espinheiros";
            String cnpjEmpresa = "00.000.000/0001-00";
            BigDecimal valor = folha.getValorLiquido();
            LocalDate dataPagamento = folha.getDataFinalPrestacaoSevico();
            Locale locale = new Locale("pt", "BR");
            String mes = dataPagamento.getMonth().getDisplayName(TextStyle.FULL, locale);
            String valorExtenso = new MoedaUtil().valorPorExtenso(valor);

            String texto = String.format(
                    "DECLARAÇÃO DE PAGAMENTO AO SERVIÇOS PRESTADOS\n\n" +
                            "Eu %s inscrito (a) no CPF nº %s declaro que recebi da empresa %s de CNPJ %s a quantia de R$ %.2f (%s) " +
                            "referente aos serviços odontológicos prestados para a unidade %s no mês de %s.\n\n" +
                            "Itajaí, %d de %s de %d\n\n" +
                            "____________________________________\n%s\nCirurgião (a) Dentista\nPrestador (a) de Serviço Odontológico",
                    nomeProfissional, cpfProfissional, nomeEmpresa, cnpjEmpresa,
                    valor, valorExtenso, nomeEmpresa, mes,
                    dataPagamento.getDayOfMonth(), mes, dataPagamento.getYear(),
                    nomeProfissional
            );

            document.add(new Paragraph(texto).setTextAlignment(TextAlignment.JUSTIFIED));
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar declaração de pagamento em PDF", e);
        }
    }
}
