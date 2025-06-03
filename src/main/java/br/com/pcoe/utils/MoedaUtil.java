package br.com.pcoe.utils;

import com.ibm.icu.text.RuleBasedNumberFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

public class MoedaUtil {

    public String valorPorExtenso(BigDecimal valor) {
        if (valor == null) {
            return "";
        }

        BigDecimal inteiro = valor.setScale(0, RoundingMode.DOWN);
        BigDecimal centavos = valor.subtract(inteiro).movePointRight(2).setScale(0, RoundingMode.HALF_UP);

        RuleBasedNumberFormat formatter = new RuleBasedNumberFormat(new Locale("pt", "BR"), RuleBasedNumberFormat.SPELLOUT);

        StringBuilder valorExtenso = new StringBuilder();

        long parteInteira = inteiro.longValue();
        int parteCentavos = centavos.intValue();

        if (parteInteira > 0) {
            valorExtenso.append(formatter.format(parteInteira))
                    .append(parteInteira == 1 ? " real" : " reais");
        }

        if (parteCentavos > 0) {
            if (parteInteira > 0) {
                valorExtenso.append(" e ");
            }
            valorExtenso.append(formatter.format(parteCentavos))
                    .append(parteCentavos == 1 ? " centavo" : " centavos");
        }

        return valorExtenso.toString();
    }
}
