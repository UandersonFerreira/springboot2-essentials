package estudo.java.springboot2.handler;

import estudo.java.springboot2.exception.BadRequestException;
import estudo.java.springboot2.exception.BadRequestExceptionDetails;
import estudo.java.springboot2.exception.ExceptionDetails;
import estudo.java.springboot2.exception.ValidationExceptionDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class BadRequesthandler extends ResponseEntityExceptionHandler {
    //PADRONIZAÇÃO DE EXCEÇÕES
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handleBadRequestException(BadRequestException bre){
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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        final List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        final String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        final String fieldsMessages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                ValidationExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad Request Exception, Invalid Fields")
                        .details(exception.getMessage())//poderia ser uma mensagem do "Check the field(s) errors"
                        .developerMessage(exception.getClass().getName())
                        .fields(fields)
                        .fieldsMessage(fieldsMessages)
                        .build(), HttpStatus.BAD_REQUEST
        );

               /*
        Sobreescrevendo um tratamento de erro padrão da class ResponseEntityExceptionHandler
        do spring para um personalizada.
         */
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
       Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

         ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(statusCode.value())
                .title(ex.getCause().getMessage())
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .build();
        return createResponseEntity(exceptionDetails, headers, statusCode, request);
        /*
        Sobreescrevendo um tratamento de erro padrão da class ResponseEntityExceptionHandler
        do spring para um personalizada.
         */
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

CLASSES QUE ESTÃO CONTIDAS NO(ExceptionHandler) TRATAMENTO DE ERRO DO SPRING:

	@ExceptionHandler({
			HttpRequestMethodNotSupportedException.class,
			HttpMediaTypeNotSupportedException.class,
			HttpMediaTypeNotAcceptableException.class,
			MissingPathVariableException.class,
			MissingServletRequestParameterException.class,
			MissingServletRequestPartException.class,
			ServletRequestBindingException.class,
			MethodArgumentNotValidException.class,
			NoHandlerFoundException.class,
			AsyncRequestTimeoutException.class,
			ErrorResponseException.class,
			ConversionNotSupportedException.class,
			TypeMismatchException.class,
			HttpMessageNotReadableException.class,
			HttpMessageNotWritableException.class,
			BindException.class
		})
 */
