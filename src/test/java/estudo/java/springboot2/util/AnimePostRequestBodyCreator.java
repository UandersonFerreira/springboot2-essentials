package estudo.java.springboot2.util;

import estudo.java.springboot2.domain.Anime;
import estudo.java.springboot2.requests.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {
    public static AnimePostRequestBody createdAnimePostRequestBody(){
        return AnimePostRequestBody.builder()
                .name(AnimeCreated.createdAnimeToBeSave().getName())
                .build();
    }

}
