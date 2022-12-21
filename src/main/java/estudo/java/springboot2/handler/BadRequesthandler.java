package estudo.java.springboot2.handler;

import estudo.java.springboot2.exception.BadRequestException;
import estudo.java.springboot2.exception.BadRequestExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class BadRequesthandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException bre){
        return new ResponseEntity<>(
                BadRequestExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad Request Exception, Checks the Documentation")
                        .details(bre.getMessage())
                        .developerMessage(bre.getClass().getName())
                        .build(), HttpStatus.BAD_REQUEST
        );
    }

}//class
/*
@ControllerAdvice: Essa anotação é do Spring Framework e é utilizada
para lidar com exceções lançadas em qualquer lugar da sua
aplicação, não só pelo controller.
@ControllerAdvice pode ser vista como uma barreira que
intercepta todas as exceções que acontecem dentro de um
método/classe anotado com @RequestMapping


O @ExceptionHandler é uma anotação usada para lidar com as exceções
específicas e o envio das respostas personalizadas ao cliente.




link:https://www.zup.com.br/blog/controller-advice
 */
