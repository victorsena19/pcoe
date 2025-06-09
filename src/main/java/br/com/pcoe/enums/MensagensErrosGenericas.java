package br.com.pcoe.enuns;

import lombok.Getter;

@Getter
public enum MensagensErrosGenericas {
    PERMISSAOUSUARIONEGADA("Não foi possível %s esse caixa pois voce não é Admin ou não foi você que lançou esse caixa"),
    IDNAOENCONTRADO("Não foi possível encontrar um(a) %s com esse ID: "),
    ARGUMENTOJAEXISTENTE("%s já encontra-se cadastrada no sistema!");
    private final String erro;

     MensagensErrosGenericas(String erro) {
        this.erro = erro;
    }

    public String format(Object... args){
         return String.format(erro, args);
    }


}
