package estudo.java.springboot2.service;

import estudo.java.springboot2.domain.Anime;
import estudo.java.springboot2.mapper.AnimeMapper;
import estudo.java.springboot2.repository.AnimeRepository;
import estudo.java.springboot2.requests.AnimePostRequestBody;
import estudo.java.springboot2.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository animeRepository;
    public List<Anime> listAll(){
        return animeRepository.findAll();
    }
    public Anime findByIdOrThrowBadRequestException(long id){
        return animeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found"));
    }

    public Anime save(AnimePostRequestBody animePostRequestBody) {
        final Anime anime = AnimeMapper.INSTANCE.toAnime(animePostRequestBody);
        return animeRepository.save(anime);
    }

    public void delete(long id) {
        animeRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        final Anime savedAnime = findByIdOrThrowBadRequestException(animePutRequestBody.getId());
        final Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
        anime.setId(savedAnime.getId());

        animeRepository.save(anime);
    }

}//class

/*
- Class reponsavel pela impletação
da regra de negócio da aplicação!

OBS:
- Não é recomendado mostrar toda a stack de erro no front, portanto basta configurar
o arquivo application.properties/yml do spring para que não seja exibido.
ex:
server:
  error:
    include-stacktrace: on_param


 -O @Bean  serve para exportar uma classe(Criar uma Intância) para o Spring, para
  que ele consiga carregar essa classe e fazer injeção de
  dependência(utilizar methos e atributos) dela em outras classes.

Geralmente quando precisamos criar uma classe que o Spring
vai gerenciar, basta criar a classe e anota-la com alguma
anotação do Spring( @controller, @service, @Repository, etc.).

fonte:https://cursos.alura.com.br/forum/topico-duvida-sobre-o-bean-113349


 */