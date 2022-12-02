package estudo.java.springboot2.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class Anime {
   private Long id;
    private String nome;

}
/*
Representa os dados que serão guardados na base de dados.
 */
