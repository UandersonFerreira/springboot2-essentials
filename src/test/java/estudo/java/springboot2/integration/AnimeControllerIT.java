package estudo.java.springboot2.integration;

import estudo.java.springboot2.domain.Anime;
import estudo.java.springboot2.repository.AnimeRepository;
import estudo.java.springboot2.requests.AnimePostRequestBody;
import estudo.java.springboot2.util.AnimeCreated;
import estudo.java.springboot2.util.AnimePostRequestBodyCreator;
import estudo.java.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)//geração de portas aleatórias
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AnimeControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort//forma de pegar a porta gerada
    private  int portaGerada;
    @Autowired
    private AnimeRepository animeRepository;


    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimeInsidePageObject_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreated.createdAnimeToBeSave());//salva um objeto anime no banco de teste em memória

        String expectedName = savedAnime.getName();//pegamos o nome salvo do objeto anime salvo

        PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes",//executa uma requisição para o controller
                HttpMethod.GET,//Do tipo Get
                null,//Sem passar objetor/paramentros nenhum
                new ParameterizedTypeReference<PageableResponse<Anime>>() {//Cria um PageableResponse do tipo Anime
                }).getBody();//E retorna os atributos/objeto PageableResponse<Anime>

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("ListAll returns list of anime when successful")
    void listAll_ReturnsListOfAnime_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeCreated.createdAnimeToBeSave());//salva um objeto anime no banco de teste em memória

        String expectedName = savedAnime.getName();

        List<Anime> animes = testRestTemplate.exchange("/animes/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeCreated.createdAnimeToBeSave());//salva um objeto anime no banco de teste em memória

        Long expectedId = savedAnime.getId();

        Anime anime = testRestTemplate.getForObject("/animes/{id}",
                Anime.class, expectedId);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }


    @Test
    @DisplayName("findByName returns list of anime when successful")
    void findByname_ReturnsListOfAnime_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeCreated.createdAnimeToBeSave());//salva um objeto anime no banco de teste em memória

        String expectedName = savedAnime.getName();
        String url = String.format("/animes/find?name=%s", expectedName);

        List<Anime> animes = testRestTemplate.exchange(url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns empty list of anime when anime is not found")
    void findByname_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound(){

        List<Anime> animes = testRestTemplate.exchange("/animes/find?name=dbz",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save returns AnimePostRequestBody when successful")
    void save_ReturnsAnime_WhenSuccessful(){
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createdAnimePostRequestBody();
        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity("/animes", animePostRequestBody, Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();

    }

    @Test
    @DisplayName("replace update AnimePutRequestBody when successful")
    void replace_UpdateAnime_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeCreated.createdAnimeToBeSave());

        savedAnime.setName("New name");

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes",
                HttpMethod.PUT,
                new HttpEntity<>(savedAnime),
                Void.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeCreated.createdAnimeToBeSave());

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/{id}",
                HttpMethod.DELETE,
               null,
                Void.class,
                savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

}//class


/*
Por padrão o  testRestTemplate.exchange(), já reconhece o localhost e a porta.

Teste de integração inicia totalmente o servidor da aplicação e no nosso caso,
toda vez que for iniciado será numa porta diferente.

MockMvc -> Quando queremos testar o lado do servidor e evitar
requisições reais sendo feitas.

TestRestTemplate -> Quando queremos testar incluindo o deploy e as
requisições http.

 */