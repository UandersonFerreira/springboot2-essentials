package estudo.java.springboot2.integration;

import estudo.java.springboot2.domain.Anime;
import estudo.java.springboot2.domain.DevDojoUser;
import estudo.java.springboot2.repository.AnimeRepository;
import estudo.java.springboot2.repository.DevDojoUserRepository;
import estudo.java.springboot2.requests.AnimePostRequestBody;
import estudo.java.springboot2.util.AnimeCreated;
import estudo.java.springboot2.util.AnimePostRequestBodyCreator;
import estudo.java.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
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
    @Qualifier(value = "testRestTemplateRoleUser")//Quando for realizar a injeção de dependência, utilize o Bean de nome "testRestTemplateRoleUser"
    private TestRestTemplate testRestTemplateRoleUser;
    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;

//    @LocalServerPort//forma de pegar a porta gerada
//    private  int portaGerada;
    @Autowired
    private AnimeRepository animeRepository;
    @Autowired
    private DevDojoUserRepository devDojoUserRepository;
    private static final DevDojoUser USER = DevDojoUser.builder()
            .name("DevDojo Academy")
            .password("{bcrypt}$2a$10$eW1OqlFmyAplHkHHo8x1H.dXoSlTq20lSwnmCGwYfBOxRHRkvGy5O")
            .username("devdojo")
            .authorities("ROLE_USER")
            .build();

    private static final DevDojoUser ADMIN = DevDojoUser.builder()
            .name("Uanderson Oliveira")
            .password("{bcrypt}$2a$10$eW1OqlFmyAplHkHHo8x1H.dXoSlTq20lSwnmCGwYfBOxRHRkvGy5O")
            .username("uanderson")
            .authorities("ROLE_USER,ROLE_ADMIN")
            .build();

    @TestConfiguration//pq é um configuração que iremos fazer na switch/coleção de Testes
    @Lazy //Flag para dizer que a class deve ter uma inicialização mais "lenta"/"preguiçosa"
    static class Config{
        //criar um bean que será utilizado na hora do @Autowired do  private TestRestTemplate testRestTemplate;

        @Bean(name = "testRestTemplateRoleUser")//Nomeando um bean específico
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port){
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+ port)
                    .basicAuthentication("devdojo", "academy");

            return new TestRestTemplate(restTemplateBuilder);

        }

        @Bean(name = "testRestTemplateRoleAdmin")//Nomeando um bean específico
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port){
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+ port)
                    .basicAuthentication("uanderson", "academy");

            return new TestRestTemplate(restTemplateBuilder);

        }

        /*
        A nomeação é comum, quando temos mais de um tipo de bean, que pode
        ser injetado automaticamente.

        .rootUri("http:localhost:"+ port) - Para evitar:
            - java.lang.IllegalArgumentException: URI is not absolute


         @Lazy - Neste caso estamos utilizando a flag
         para fazer com a classe tenha uma inicialização mais lenta,
         fazendo que seja carregada somente, depois da configuração da porta,
         pois o atributo da porta gerado não é estático para pode utilizar diretamento
         no method testRestTemplateRoleUserCreator().

         */
    }



    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimeInsidePageObject_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreated.createdAnimeToBeSave());//salva um objeto anime no banco de teste em memória

        devDojoUserRepository.save(USER);

        String expectedName = savedAnime.getName();//pegamos o nome salvo do objeto anime salvo

        PageableResponse<Anime> animePage = testRestTemplateRoleUser.exchange("/animes",//executa uma requisição para o controller
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
        devDojoUserRepository.save(USER);

        String expectedName = savedAnime.getName();

        List<Anime> animes = testRestTemplateRoleUser.exchange("/animes/all",
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
        devDojoUserRepository.save(USER);

        Long expectedId = savedAnime.getId();

        Anime anime = testRestTemplateRoleUser.getForObject("/animes/{id}",
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
        devDojoUserRepository.save(USER);

        String expectedName = savedAnime.getName();
        String url = String.format("/animes/find?name=%s", expectedName);

        List<Anime> animes = testRestTemplateRoleUser.exchange(url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns empty list of anime when anime is not found")
    void findByname_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound(){
        devDojoUserRepository.save(USER);

        List<Anime> animes = testRestTemplateRoleUser.exchange("/animes/find?name=dbz",
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
        devDojoUserRepository.save(USER);

        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createdAnimePostRequestBody();
        ResponseEntity<Anime> animeResponseEntity = testRestTemplateRoleUser.postForEntity("/animes", animePostRequestBody, Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();

    }

    @Test
    @DisplayName("replace update AnimePutRequestBody when successful")
    void replace_UpdateAnime_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeCreated.createdAnimeToBeSave());
        devDojoUserRepository.save(USER);

        savedAnime.setName("New name");

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange("/animes",
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
        devDojoUserRepository.save(ADMIN);

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleAdmin.exchange("/animes/admin/{id}",
                HttpMethod.DELETE,
               null,
                Void.class,
                savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    @Test
    @DisplayName("delete returns 403 when user is not Admin")
    void delete_Returns403_WhenUserIsNotAdmin(){
        Anime savedAnime = animeRepository.save(AnimeCreated.createdAnimeToBeSave());
        devDojoUserRepository.save(USER);

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange("/animes/admin/{id}",
                HttpMethod.DELETE,
               null,
                Void.class,
                savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
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

DICA: TENTAR MANTER SEMPRE UM PADRÃO NA COSNTRUÇÃO DE UM CÓDIGO.
 */