package estudo.java.springboot2.service;

import estudo.java.springboot2.domain.Anime;
import estudo.java.springboot2.exception.BadRequestException;
import estudo.java.springboot2.mapper.AnimeMapper;
import estudo.java.springboot2.repository.AnimeRepository;
import estudo.java.springboot2.requests.AnimePostRequestBody;
import estudo.java.springboot2.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
//- Class reponsavel pela impletação da regra de negócio da aplicação!
@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository animeRepository;
    public Page<Anime> listAll(Pageable pageable){
        return animeRepository.findAll(pageable);//Por animeRepository extender de JpaRepository, herda-se seus methods
    }
    public List<Anime>  listAllNoPageable() {
        return animeRepository.findAll();
    }
    public List<Anime> findByName(String name){
        return animeRepository.findByName(name);
    }
    public Anime findByIdOrThrowBadRequestException(long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime not found"));//exception customizada
    }
//        public Anime findByIdOrThrowBadRequestException(long id){
//        return animeRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found"));

//    }

    @Transactional
    public Anime save(AnimePostRequestBody animePostRequestBody) {
         Anime anime = AnimeMapper.INSTANCE.toAnime(animePostRequestBody);
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
-Uma transação garante que todo processo deva ser executado com êxito, é “tudo ou nada” (princípio da atomicidade).

@Transactional: Garante que se ocorre um erro durante a execução de uma
transaçã a mesma será cancelada, totalmente, não persistindo no banco de dados.

@Transactional(rollbackFor = Exception.class): Anotação para captar
exceções checadas

É recomenda-se sempre usar a anotação, quando temos methods que fazer alguma persistência
no banco de dados, afim de, garantir o principio da atomicidade.

 - begin(): Inicia uma transação;
 - commit(): Finaliza uma transação;
 - rollback(): Cancela uma transação.

 Link: https://www.devmedia.com.br/conheca-o-spring-transactional-annotations/32472

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