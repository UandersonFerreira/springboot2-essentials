package estudo.java.springboot2.controller;

import estudo.java.springboot2.domain.Anime;
import estudo.java.springboot2.requests.AnimePostRequestBody;
import estudo.java.springboot2.requests.AnimePutRequestBody;
import estudo.java.springboot2.service.AnimeService;
import estudo.java.springboot2.util.AnimeCreated;
import estudo.java.springboot2.util.AnimePostRequestBodyCreator;
import estudo.java.springboot2.util.AnimePutRequestBodyCreator;
import estudo.java.springboot2.util.DateUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)//Informa que queremos utilizar o Junit com Spring
class AnimeControllerTest {
    @InjectMocks
    private AnimeController animeController;
    @Mock
    private AnimeService animeServiceMock;
    @Mock
    private DateUtil dateUtilMock;

    @BeforeEach
    void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreated.createValidAnime()));
        BDDMockito.when(animeServiceMock.listAll(any()))//quando animeservicemock listar todos, não importanto o argumento passado
                .thenReturn(animePage);//então deve-se retornar o animePage.

        BDDMockito.when(animeServiceMock.listAllNoPageable())
                .thenReturn(List.of(AnimeCreated.createValidAnime()));

        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))//Qualquer argumento do tipo Long
                .thenReturn(AnimeCreated.createValidAnime());

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))//Qualquer argumento do tipo String
                .thenReturn(List.of(AnimeCreated.createValidAnime()));

        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))//Forcando a aceitar qualquer objeto, desde que seja um AnimePostRequestBody
                .thenReturn(AnimeCreated.createValidAnime());

        BDDMockito.doNothing().when(animeServiceMock)//doNothing-não faça nada/Por estar chamando um method sem retorno
                .replace(ArgumentMatchers.any(AnimePutRequestBody.class));//Forcando a substituir por qualquer objeto, desde que seja um AnimePutRequestBody


        BDDMockito.doNothing().when(animeServiceMock)//doNothing-não faça nada/Por estar chamando um method sem retorno
                .delete(ArgumentMatchers.anyLong());

    }//setUp

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimeInsidePageObject_WhenSuccessful(){
        String expectedName = AnimeCreated.createValidAnime().getName();

        Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }


    @Test
    @DisplayName("ListAll returns list of anime when successful")
    void listAll_ReturnsListOfAnime_WhenSuccessful(){
        String expectedName = AnimeCreated.createValidAnime().getName();

        List<Anime> animeList = animeController.listAll().getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful(){
        Long expectedId = AnimeCreated.createValidAnime().getId();

        Anime anime = animeController.findById(1).getBody();

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }


    @Test
    @DisplayName("findByName returns list of anime when successful")
    void findByname_ReturnsListOfAnime_WhenSuccessful(){
        String expectedName = AnimeCreated.createValidAnime().getName();

        List<Anime> animeList = animeController.findByName("Overlod").getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns empty list of anime when anime is not found")
    void findByname_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound(){
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());//retorna uma lista vazia

        List<Anime> animeList = animeController.findByName("Overlod").getBody();

        Assertions.assertThat(animeList)
                .isNotNull()//compara senão é nula
                .isEmpty();//e se está realmente vazia!
    }

    @Test
    @DisplayName("save returns AnimePostRequestBody when successful")
    void save_ReturnsAnime_WhenSuccessful(){
        Anime anime = animeController
                .save(AnimePostRequestBodyCreator.createdAnimePostRequestBody())
                .getBody();

        Assertions.assertThat(anime)
                .isNotNull()
                .isEqualTo(AnimeCreated.createValidAnime());
    }

    @Test
    @DisplayName("replace update AnimePutRequestBody when successful")
    void replace_UpdateAnime_WhenSuccessful(){
        Assertions.assertThatCode(() -> animeController
                .replace(AnimePutRequestBodyCreator.createdAnimePutRequestBody()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.replace(AnimePutRequestBodyCreator.createdAnimePutRequestBody());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){
        Assertions.assertThatCode(() -> animeController.delete(1))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.delete(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }


}//classs
/*
@SpringBootTest:
- contexto do spring irá ser inicializado| tenta inicializar a
aplicação  para realizar os testes.

@InjectMocks - Utiliza-se quando queremos testar a classe em si.
@Mock - Utiliza-se para todas as classes que estão dentro da
classe que queremos testar (Que de alguma forma está sendo necessário sua utilização).

Ex:
  Queremos testar a class AnimeController e dentro dela estamos utilizando uma
  referência das classes:
     - DateUtil dateUtil;
     - AnimeService animeService;

E dentro do AnimeService estamos utilizando uma referência da class(Interface):
   - AnimeRepository animeRepository


then - então
when - quando
that - isso/esse
assertThat - afirmar isso

 */