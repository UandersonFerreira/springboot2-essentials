package estudo.java.springboot2.client;

import estudo.java.springboot2.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
public class SpringClient {
    //REST TEMPLATE
    public static void main(String[] args) {
        String url = "http://localhost:8080/animes/{id}";
        //retorna um objeto dentro de um wrapper que é o ResponseEntity
        ResponseEntity<Anime> entity = new RestTemplate().getForEntity(url, Anime.class,2);
        Anime anime = new RestTemplate().getForEntity(url, Anime.class,2).getBody();

        log.info("getForEntity: '{}' ",entity);
        log.info("getForEntity + getBody:  '{}' ",anime);

        //Retorna um objeto diretamente
        Anime object = new RestTemplate().getForObject(url, Anime.class,3);
        log.info("getForObject: '{}'",object);

        Anime[] animesAll = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class);
        log.info("AnimesAll[] + getForObject: '{}'",animesAll);

        //SUPER TYPE TOKENS
        //@formatter:off - desabilita a formatação nesta parte
        final ResponseEntity<List<Anime>> exchange = new RestTemplate()
                .exchange("http://localhost:8080/animes/all",
                        HttpMethod.GET, //Necessita especificar o method HTTP que será utilizado na requisição
                        null,//RequestEntity, podendo passar um objeto caso utiliza-se um method HTTP diferente do Get que aceite
                        new ParameterizedTypeReference<List<Anime>>() {}//Pega o tipo do Objeto passado na lista e
                        // converte para uma Lista do mesmo tipo.
                );
        //@formatter:on
        log.info("animesAllWithList + exchange: '{}'",exchange.getBody());

/*         Anime kingdom = Anime.builder().name("Kingdom").build();
         Anime kingdomSaved = new RestTemplate().postForObject("http://localhost:8080/animes", kingdom, Anime.class);
        log.info("Save anime: {}",kingdomSaved);
*/
    // COM POST
        Anime samuraiChamploo = Anime.builder().name("Samurai champloo").build();
        ResponseEntity<Anime> samuraiChamplooSaved = new RestTemplate().exchange("http://localhost:8080/animes",
                HttpMethod.POST,
                new HttpEntity<>(samuraiChamploo, createJsonHeader()),//podemos passar headers http dentro do HttpEntity, junto o objeto passado
                Anime.class);

        log.info("Save anime: {}",samuraiChamplooSaved);
        // COM PUT
         Anime animeToBeUpdate = samuraiChamplooSaved.getBody();
        animeToBeUpdate.setName("Samurai champloo 2");
        ResponseEntity<Void> samuraiChamplooUpdate= new RestTemplate().exchange("http://localhost:8080/animes",
                HttpMethod.PUT,
                new HttpEntity<>(animeToBeUpdate,createJsonHeader()),//podemos passar headers http dentro do HttpEntity, junto o objeto passado
                Void.class);

        log.info("Update anime: {}",samuraiChamplooUpdate);

        // COM DELETE
        ResponseEntity<Void> samuraiChamplooDelete = new RestTemplate().exchange(url,
                HttpMethod.DELETE,
               null,//podemos passar headers http dentro do HttpEntity, junto o objeto passado
                Void.class,
                animeToBeUpdate.getId());

        log.info("Delete anime: {}",samuraiChamplooDelete);

    }//main
    private static HttpHeaders createJsonHeader(){
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}//class
