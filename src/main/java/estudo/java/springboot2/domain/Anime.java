package estudo.java.springboot2.domain;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Anime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

}
/*
Representa os dados que serão guardados na base de dados.

Jackson
-realiza o mapeado dos atributos
- Se encontrar o JSON como o mesmo nome dos atributos declarados na classe
irá fazer automáticamnete o mapeamento, caso contrário é necessário
utilizar a anotaçõa:
 @JsonProperty("nome do atributo JSON") em cima do atributo da classe
- Leitura: o atributo JSON é {name}, mas eu quero que voçê mapei para  dentro de nameAnime.
 */

