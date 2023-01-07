package estudo.java.springboot2.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class DevDojoUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "The anime name cannot be empty")
    private String name;
    private String username;
    private String password;
    private String authorities;//ROLES_ADMIN, ROLES_USER


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(authorities.split(","))//separação por vírgula
                .map(SimpleGrantedAuthority::new)//tranforma o array de Strings em um SimpleGrantedAuthority
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
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

