package estudo.java.springboot2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)//sempre retornará o status d eum Bad Request
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}

/*

RELEMBRANDO:
Exception do tipo RuntimeException: Não verificadas é não exige o tratamento e
ocorrem por erro de programação/lógica, podendo quebrar o código.

Exception do tipo Exception: Verificadas, é exige o seu tratamento.

 */