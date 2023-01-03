package estudo.java.springboot2.util;

import estudo.java.springboot2.requests.AnimePostRequestBody;
import estudo.java.springboot2.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {
    public static AnimePutRequestBody createdAnimePutRequestBody(){
        return AnimePutRequestBody.builder()
                .id(AnimeCreated.createValidUpdateAnime().getId())
                .name(AnimeCreated.createValidUpdateAnime().getName())
                .build();
    }

}
