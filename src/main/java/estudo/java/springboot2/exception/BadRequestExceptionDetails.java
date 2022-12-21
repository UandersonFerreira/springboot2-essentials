package estudo.java.springboot2.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BadRequestExceptionDetails {
    private String title;
    private int status;
    private String details;
    private String developerMessage;
    private LocalDateTime timestamp;
}
/*
handler Global -  Manipulação/Tratamento global

OBS: E mais recomendando manter um padrão ao realizar o tramento de exception
ou seja manter uma consistência.

 */