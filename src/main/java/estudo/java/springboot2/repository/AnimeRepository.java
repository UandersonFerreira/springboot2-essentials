package estudo.java.springboot2.repository;

import estudo.java.springboot2.domain.Anime;

import java.util.List;

public interface AnimeRepository {
    List<Anime> listAll();

}


/*
Interação/conexão com o Banco de dados.
Criação das Query's
 */