package br.com.pcoe.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErroPadronizado {

    private LocalDateTime timestamp;
    private int status;
    private String mensagem;
    private String caminho;
}
