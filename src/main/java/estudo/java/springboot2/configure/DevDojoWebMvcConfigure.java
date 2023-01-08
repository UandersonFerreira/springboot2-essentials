package estudo.java.springboot2.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class DevDojoWebMvcConfigure implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver pageHandler = new PageableHandlerMethodArgumentResolver();
        pageHandler.setFallbackPageable(PageRequest.of(0,20,Sort.by("id").descending()));
        resolvers.add(pageHandler);
    }


}//class

/*
ALTERAÇÕES NO NÍVEL DO BANCO DE DADOS, NÃO DA APLICAÇÃO

@Configuration – define uma classe como fonte de definições/configurações de beans e é uma das
anotações essenciais se você estiver usando a configuração baseada em Java.
Injenção de dependencia, flag que sinaliza que o spring têm que gerênciar as configurações.

-Sem a annotação as configurações não seram aplicadas, prevalecendo as padrões.

-> WebMvcConfigure
Sobreescrevendo a Configuração do spring a respeito da
paginação!

-> Neste exemplo estamos sobreescrendo/definindo a página e quantidade de objetos
mostrados na tela ao se requisitado todos os animes, ORDENANDO pelo id em ordem DECRESCENTE.
URL parametros( ?sort=id,desc)

 */
