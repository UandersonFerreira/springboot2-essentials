package estudo.java.springboot2.configure;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

public class DevDojoWebMvnConfigure implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver pageHandler = new PageableHandlerMethodArgumentResolver();
        pageHandler.setFallbackPageable(PageRequest.of(0,5));
        resolvers.add(pageHandler);
    }
}
/*
-> WebMvcConfigure
Sobreescrevendo a Configuração do spring a respeito da
paginação!

-> Neste exemplo estamos sobreescrendo/definindo a página e quantidade de objetos
mostrados na tela ao se requisitado todos os animes.
 */
