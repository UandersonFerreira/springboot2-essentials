package estudo.java.springboot2.service;

import estudo.java.springboot2.domain.Anime;
import estudo.java.springboot2.exception.BadRequestException;
import estudo.java.springboot2.repository.AnimeRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
@ExtendWith(SpringExtension.class)//Informa que queremos utilizar o Junit com Spring
class AnimeServiceTest {
    @InjectMocks
    private AnimeService animeService;
    @Mock
    private AnimeRepository animeRepositoryMock;
    @Mock
    private DateUtil dateUtilMock;

    @BeforeEach
    void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreated.createValidAnime()));
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);//então deve-se retornar o animePage.

        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(List.of(AnimeCreated.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))//Qualquer argumento do tipo Long
                .thenReturn(Optional.of(AnimeCreated.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))//Qualquer argumento do tipo String
                .thenReturn(List.of(AnimeCreated.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))//Forcando a aceitar qualquer objeto, desde que seja um Anime
                .thenReturn(AnimeCreated.createValidAnime());

        BDDMockito.doNothing().when(animeRepositoryMock)//doNothing-não faça nada/Por estar chamando um method sem retorno
                .delete(ArgumentMatchers.any(Anime.class));

    }//setUp

    @Test
    @DisplayName("ListAll returns list of anime inside page object when successful")
    void listAll_ReturnsListOfAnimeInsidePageObject_WhenSuccessful(){
        String expectedName = AnimeCreated.createValidAnime().getName();

        Page<Anime> animePage = animeService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }


    @Test
    @DisplayName("listAllNoPageable returns list of anime when successful")
    void listAllNoPageable_ReturnsListOfAnime_WhenSuccessful(){
        String expectedName = AnimeCreated.createValidAnime().getName();

        List<Anime> animeList = animeService.listAllNoPageable();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns anime when successful")
    void findByIdOrThrowBadRequestException_ReturnsAnime_WhenSuccessful(){
        Long expectedId = AnimeCreated.createValidAnime().getId();

        Anime anime = animeService.findByIdOrThrowBadRequestException(1);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when Anime is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenAnimeisNotFound(){
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> animeService.findByIdOrThrowBadRequestException(1));
    }


    @Test
    @DisplayName("findByName returns list of anime when successful")
    void findByname_ReturnsListOfAnime_WhenSuccessful(){
        String expectedName = AnimeCreated.createValidAnime().getName();

        List<Anime> animeList = animeService.findByName("Overlod");

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns empty list of anime when anime is not found")
    void findByname_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound(){
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());//retorna uma lista vazia

        List<Anime> animeList = animeService.findByName("Overlod");

        Assertions.assertThat(animeList)
                .isNotNull()//compara senão é nula
                .isEmpty();//e se está realmente vazia!
    }

    @Test
    @DisplayName("save returns AnimePostRequestBody when successful")
    void save_ReturnsAnime_WhenSuccessful(){
        Anime anime = animeService
                .save(AnimePostRequestBodyCreator.createdAnimePostRequestBody());

        Assertions.assertThat(anime)
                .isNotNull()
                .isEqualTo(AnimeCreated.createValidAnime());
    }

    @Test
    @DisplayName("replace update AnimePutRequestBody when successful")
    void replace_UpdateAnime_WhenSuccessful(){
        Assertions.assertThatCode(() -> animeService
                        .replace(AnimePutRequestBodyCreator.createdAnimePutRequestBody()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){
        Assertions.assertThatCode(() -> animeService.delete(1))
                .doesNotThrowAnyException();
    }

}