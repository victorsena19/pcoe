package br.com.pcoe.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErroValidacao {
    private LocalDateTime timestamp;
    private int status;
    private String mensagem;
    private String caminho;
    private Map<String, String> erros;
}
