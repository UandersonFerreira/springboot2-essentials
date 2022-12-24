package estudo.java.springboot2.client;

import estudo.java.springboot2.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
@Log4j2
public class SpringClient {
    //RESTTEMPLATE
    public static void main(String[] args) {
        String url = "http://localhost:8080/animes/{id}";
        ResponseEntity<Anime> entity = new RestTemplate().getForEntity(url, Anime.class,2);
        Anime anime = new RestTemplate().getForEntity(url, Anime.class,2).getBody();

        log.info("getForEntity: '{}' ",entity);
        log.info("getForEntity + getBody:  '{}' ",anime);

        Anime object = new RestTemplate().getForObject(url, Anime.class,3);
        log.info("getForObject: '{}'",object);

    }//main
}//class
