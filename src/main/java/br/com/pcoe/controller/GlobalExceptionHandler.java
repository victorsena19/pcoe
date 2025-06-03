package br.com.pcoe.controller;

import br.com.pcoe.exceptions.ErroPadronizado;
import br.com.pcoe.exceptions.ErroValidacao;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroPadronizado> lidarComArgumentoErrado(IllegalArgumentException ex,
                                                                   WebRequest request) {
        ErroPadronizado erroPadronizado = new ErroPadronizado(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(erroPadronizado, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroPadronizado> lidarComEntidadeNaoEncontrada(EntityNotFoundException ex,
                                                                         WebRequest request) {
        ErroPadronizado erroPadronizado = new ErroPadronizado(
          LocalDateTime.now(),
          HttpStatus.NOT_FOUND.value(),
          ex.getMessage(),
          request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroPadronizado);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroPadronizado> lidarComAcessoNegado(AccessDeniedException ex,
                                                              WebRequest request) {
        ErroPadronizado erro = new ErroPadronizado(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Acesso negado. Você não tem permissão para essa operação.",
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroValidacao> lidarComValidacaoDeCampos(MethodArgumentNotValidException ex,
                                                          WebRequest request) {
        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((erro) -> {
            String campo = ((FieldError) erro).getField();
            String mensagem = erro.getDefaultMessage();
            erros.put(campo, mensagem);
        });

        ErroValidacao erro = new ErroValidacao(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação nos campos",
                request.getDescription(false),
                erros
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroPadronizado> lidarComErroGenerico(Exception ex, WebRequest request) {
        ex.printStackTrace();
        ErroPadronizado erro = new ErroPadronizado(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno inesperado. Contate o administrador do sistema.",
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

}
