package estudo.java.springboot2.controller;

import estudo.java.springboot2.domain.Anime;
import estudo.java.springboot2.requests.AnimePostRequestBody;
import estudo.java.springboot2.requests.AnimePutRequestBody;
import estudo.java.springboot2.service.AnimeService;
import estudo.java.springboot2.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("animes")
@Log4j2
//@AllArgsConstructor//cria um construtor com todos os atributos da class
@RequiredArgsConstructor//cria um construtor com todos os atributos final da class
public class AnimeController {
    private final DateUtil dateUtil;
    private final AnimeService animeService;

    @GetMapping()
    public ResponseEntity<List<Anime>> list(){
        log.info(dateUtil.formatLocalDateTimeToDateDabaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(animeService.listAll(), HttpStatus.OK);//ou ResponseEntity.ok(animeService.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id){
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }

    @PostMapping
    // @ResponseStatus(HttpStatus.CREATED) // Outra forma de retorna o CODIGO de um resposta
    public ResponseEntity<Anime> save(@RequestBody AnimePostRequestBody animePostRequestBody){
        return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody AnimePutRequestBody animePutRequestBody){//update=sub. o estado inteiro do object
        animeService.replace(animePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }





}//class
/*
Criação dos EndPoints da aplicação.
Recursos que serão acessados /anime/save, /anime/delete..


Boaa Práticas:
- Sempre retornar algo a mais do que só o esperado
ex:
    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id){
        return ResponseEntity.ok(animeService.findById(id));
    }

->@ResponseStatus(HttpStatus.CREATED)
 - Outra forma de retorna o CODIGO de um resposta
 ex:
    @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Anime> save(@RequestBody Anime anime){
        return ResponseEntity.ok(animeService.save(anime));
    }

OBS: o method delete, pode ser não-idepontente
ex; caso ele esteja alterando sempre a ultima posição de
uma lista ele estara alterando o estado do servidor.


- O "Hypertext Transfer Protocol (HTTP) Method Registry" foi
preenchido com os registros abaixo:

   +---------+------+------------+---------------+
   | Method  | Safe | Idempotent | Reference     |
   +---------+------+------------+---------------+
   | CONNECT | no   | no         | Section 4.3.6 |
   | DELETE  | no   | yes        | Section 4.3.5 |
   | GET     | yes  | yes        | Section 4.3.1 |
   | HEAD    | yes  | yes        | Section 4.3.2 |
   | OPTIONS | yes  | yes        | Section 4.3.7 |
   | POST    | no   | no         | Section 4.3.3 |
   | PUT     | no   | yes        | Section 4.3.4 |
   | TRACE   | yes  | yes        | Section 4.3.8 |
   +---------+------+------------+---------------+

 - CÓDIGOS DE RETORNO:

   +-------+-------------------------------+----------------+
   | Value | Description                   | Reference      |
   +-------+-------------------------------+----------------+
   | 100   | Continue                      | Section 6.2.1  |
   | 101   | Switching Protocols           | Section 6.2.2  |
   | 200   | OK                            | Section 6.3.1  |
   | 201   | Created                       | Section 6.3.2  |
   | 202   | Accepted                      | Section 6.3.3  |
   | 203   | Non-Authoritative Information | Section 6.3.4  |
   | 204   | No Content                    | Section 6.3.5  |
   | 205   | Reset Content                 | Section 6.3.6  |
   | 300   | Multiple Choices              | Section 6.4.1  |
   | 301   | Moved Permanently             | Section 6.4.2  |
   | 302   | Found                         | Section 6.4.3  |
   | 303   | See Other                     | Section 6.4.4  |
   | 305   | Use Proxy                     | Section 6.4.5  |
   | 306   | (Unused)                      | Section 6.4.6  |
   | 307   | Temporary Redirect            | Section 6.4.7  |
   | 400   | Bad Request                   | Section 6.5.1  |
   | 402   | Payment Required              | Section 6.5.2  |
   | 403   | Forbidden                     | Section 6.5.3  |
   | 404   | Not Found                     | Section 6.5.4  |
   | 405   | Method Not Allowed            | Section 6.5.5  |
   | 406   | Not Acceptable                | Section 6.5.6  |
   | 408   | Request Timeout               | Section 6.5.7  |
   | 409   | Conflict                      | Section 6.5.8  |
   | 410   | Gone                          | Section 6.5.9  |
   | 411   | Length Required               | Section 6.5.10 |
   | 413   | Payload Too Large             | Section 6.5.11 |
   | 414   | URI Too Long                  | Section 6.5.12 |
   | 415   | Unsupported Media Type        | Section 6.5.13 |
   | 417   | Expectation Failed            | Section 6.5.14 |
   | 426   | Upgrade Required              | Section 6.5.15 |
   | 500   | Internal Server Error         | Section 6.6.1  |
   | 501   | Not Implemented               | Section 6.6.2  |
   | 502   | Bad Gateway                   | Section 6.6.3  |
   | 503   | Service Unavailable           | Section 6.6.4  |
   | 504   | Gateway Timeout               | Section 6.6.5  |
   | 505   | HTTP Version Not Supported    | Section 6.6.6  |
   +-------+-------------------------------+----------------+

fonte:https://www.rfc-editor.org/rfc/rfc7231#page-24


 */