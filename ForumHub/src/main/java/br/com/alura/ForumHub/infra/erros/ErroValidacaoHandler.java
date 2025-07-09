package br.com.alura.ForumHub.infra.erros;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ErroValidacaoHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErroDTO>> handleValidacao(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors().stream()
                .map(erro -> new ErroDTO("Campo '" + erro.getField() + "' inválido: " + erro.getDefaultMessage()))
                .toList();

        return ResponseEntity.badRequest().body(erros);
    }
}

