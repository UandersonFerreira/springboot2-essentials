package estudo.java.springboot2.service;

import estudo.java.springboot2.domain.Anime;
import estudo.java.springboot2.repository.AnimeRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimeService {
   // private final AnimeRepository animeRepository;
    public List<Anime> listAll(){
        return List.of(
                new Anime(1L,"Naruto Shippuden"),
                new Anime(2L,"Overlord")
        );
    }

}

/*
- Class reponsavel pela impletação
da regra de negócio da aplicação!


 -O @Bean  serve para exportar uma classe(Criar uma Intância) para o Spring, para
  que ele consiga carregar essa classe e fazer injeção de
  dependência(utilizar methos e atributos) dela em outras classes.

Geralmente quando precisamos criar uma classe que o Spring
vai gerenciar, basta criar a classe e anota-la com alguma
anotação do Spring( @controller, @service, @Repository, etc.).

fonte:https://cursos.alura.com.br/forum/topico-duvida-sobre-o-bean-113349


 */