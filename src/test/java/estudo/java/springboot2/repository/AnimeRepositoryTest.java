package estudo.java.springboot2.repository;

import estudo.java.springboot2.domain.Anime;
import estudo.java.springboot2.util.AnimeCreated;
import javax.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Teste for Anime Repository")
class AnimeRepositoryTest {
    @Autowired
    private AnimeRepository animeRepository;


    @Test
    @DisplayName("Save persists anime when Successful")
    void save_PersistAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreated.createdAnimeToBeSave();
        Anime animeSave = this.animeRepository.save(animeToBeSaved);
        //está sendo utilizado a importação do pacotes: import org.assertj.core.api.Assertions;| não do Junit5 padrão
        Assertions.assertThat(animeSave).isNotNull();//se é != null
        Assertions.assertThat(animeSave.getId()).isNotNull();//se id != null
        Assertions.assertThat(animeSave.getName()).isEqualTo(animeToBeSaved.getName());// se o nome do objeto a ser salvo é o mesmo que definimos para ser criado

    }//teste

    @Test
    @DisplayName("Save update anime when Successful")
    void update_UpdatesAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreated.createdAnimeToBeSave();
        Anime animeSave = this.animeRepository.save(animeToBeSaved);

        animeSave.setName("Overlord");

        Anime animeUpdate = this.animeRepository.save(animeSave);

        Assertions.assertThat(animeUpdate).isNotNull();//se é != null
        Assertions.assertThat(animeUpdate.getId()).isNotNull();//se id != null
        Assertions.assertThat(animeUpdate.getName()).isEqualTo(animeSave.getName());// se o nome do objeto a ser atualizado é o mesmo que definimos para para o objeto

    }//teste

    @Test
    @DisplayName("Delete update anime when Successful")
    void delete_RemovesAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreated.createdAnimeToBeSave();
        Anime animeSave = this.animeRepository.save(animeToBeSaved);

        this.animeRepository.delete(animeSave);

        final Optional<Anime> animeOptional = this.animeRepository.findById(animeSave.getId());

        Assertions.assertThat(animeOptional).isEmpty();

    }//teste

    @Test
    @DisplayName("Find by Name returns list of anime when Successful")
    void findByname_ReturnsListOfAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreated.createdAnimeToBeSave();
        Anime animeSave = this.animeRepository.save(animeToBeSaved);

        String name = animeSave.getName();

        List<Anime> animeList = this.animeRepository.findByName(name);

        Assertions.assertThat(animeList)
                .isNotEmpty() // se não é vazio
                .contains(animeSave);// e se ele está contido na lista de animes retornada.

    }//teste

    @Test
    @DisplayName("Find by Name returns empty list when no anime is found")
    void findByname_ReturnsEmptyList_WhenAnimeNotFound(){

        List<Anime> animeList = this.animeRepository.findByName("Cuca");
        Assertions.assertThat(animeList).isEmpty();
        //OBS: empty/vazio é diferente de null/nulo
    }//teste

    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    void save_ThrowsConstraintViolationException_WhenNameIsEmpty(){
        Anime anime = new Anime();
//        Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
//                .isInstanceOf(ConstraintViolationException.class);

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.animeRepository.save(anime))
                .withMessageContaining("The anime name cannot be empty");

    }//teste
    

}//class

/*
Sintaxe/conversão sugerida Para os nomes dos testes:
  ->  nome da ação + o que deveria fazer | separado por handerline é seguindo a sintaxe de nomes compostos
  do java.
 */