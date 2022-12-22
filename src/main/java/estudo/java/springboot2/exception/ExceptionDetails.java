package estudo.java.springboot2.exception;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class ExceptionDetails {
    protected String title;
    protected int status;
    protected String details;
    protected String developerMessage;
    protected LocalDateTime timestamp;

}
/*
handler Global -  Manipulação/Tratamento global

OBS: E mais recomendando manter um padrão ao realizar o tramento de exception
ou seja manter uma consistência.

 */
