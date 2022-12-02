package estudo.java.springboot2.controller;

import estudo.java.springboot2.domain.Anime;
import estudo.java.springboot2.service.AnimeService;
import estudo.java.springboot2.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<Anime> list(){
        log.info(dateUtil.formatLocalDateTimeToDateDabaseStyle(LocalDateTime.now()));
        return animeService.listAll();
    }
}
/*
Criação dos EndPoints da aplicação.
Recursos que serão acessados /anime/save, /anime/delete..
 */