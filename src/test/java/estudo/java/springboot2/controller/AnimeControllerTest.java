package estudo.java.springboot2.controller;

import estudo.java.springboot2.domain.Anime;
import estudo.java.springboot2.service.AnimeService;
import estudo.java.springboot2.util.AnimeCreated;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))//quando animeservicemock listar todos, não importanto o argumento passado
                .thenReturn(animePage);//então deve-se retornar o animePage.

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