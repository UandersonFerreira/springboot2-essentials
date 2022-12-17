package estudo.java.springboot2.repository;

import estudo.java.springboot2.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime, Long> {

}


/*
Interação/conexão com o Banco de dados.
Criação das Query's
 */