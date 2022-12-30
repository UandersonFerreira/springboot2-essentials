package estudo.java.springboot2.util;

import estudo.java.springboot2.domain.Anime;

public class AnimeCreated {

    public static Anime createdAnimeToBeSave(){
        return Anime.builder()
                .name("Hajime")
                .build();
    }

    public static Anime createValidAnime(){
        return Anime.builder()
                .id(22L)
                .name("Hajime")
                .build();
    }

    public static Anime createValidUpdateAnime(){
        return Anime.builder()
                .name("Hajime no Ippo")
                .id(22L)
                .build();
    }


}//class
