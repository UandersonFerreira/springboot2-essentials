package estudo.java.springboot2.config;

import estudo.java.springboot2.service.DevDojoUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;

@EnableWebSecurity
@Log4j2
@EnableMethodSecurity(prePostEnabled = true)//configuração que valida a configuração de acesso dos usuários ao method save definido no controller
@RequiredArgsConstructor
@SuppressWarnings("java:S5344")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final DevDojoUserDetailsService devDojoUserDetailsService;

    /**
     * BasicAuthenticationFilter
     * UsernamePasswordAuthenticationFilter
     * DefaultLoginPageGeneratingFilter
     * DefaultLogoutPageGeneratingFilter
     * FilterSecurityInterceptor
     * Authentication -> Authorization
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
//                csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .authorizeRequests()
                .antMatchers("/animes/admin/**").hasRole("ADMIN")//A Ordem de declaração é importante
                .antMatchers("/animes/**").hasRole("USER")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
        /*
                     PROTEÇÃO DE URL COM ANTMATCHER

        Se numa aplicação os endpoints(url's) possuem um padrão consistente
        é possível realizar o filtro de acesso por meio dos antMatchers.
        Ex:

        save - /animes
        delete - /animes/admin
        update - /animes
        find -  /animes

        OBSERVAÇÃO: A ORDEM DE DECLARAÇÃO É IMPORTANTE!!
         - A role, que for mais RESTRITIVA deve vim Primeiro.




        */
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("Password encoded {}", passwordEncoder.encode("academy"));

        //O spring vai seguir a Ordem de declaração/criação no código
        //1° tentará Autenticar com os Usuários em inMemoryAuthentication
        auth.inMemoryAuthentication()
                .withUser("uanderson2")
                .password(passwordEncoder.encode("academy"))
                .roles("USER", "ADMIN")
                .and()
                .withUser("devdojo2")
                .password(passwordEncoder.encode("academy"))
                .roles("USER");
        //2° -caso não encontre em inMemoryAuthentication tentará Com os usuários definidos no banco de dados
        //3° - Caso Não encontrar ninguém lançará um 403 - Forbidden
        auth.userDetailsService(devDojoUserDetailsService)
                .passwordEncoder(passwordEncoder);
        /*
        POR CAUSA DO POLIMORFISMO userDetailsService CONSEGUE LOCALIZAR O METHOD
        loadUserByUsername(), QUE ESTÁ VINDO DevDojoUserDetailsService, ATRAVÉS
        DA VÁRIAVEL DE REFERÊNCIA PASSADA.

        ONDE NO METHOD loadUserByUsername(), ATRAVÉS DA VARIAVEL DE REFERÊNCIA
        DO DevDojoRepository(que Estabelece com o DB), chama o method findByUsername()
        que realiza uma consulta e retorna um UserDetails ou um Optional/ou throws UsernameNotFoundException.

        ****************-------------------------------************************

        A parte de configuração do Spring suporta multipla autenticação, com diferentes
        tipos de providers. (Pense em Bancos de dados)

        multipla autenticação -> Mais de um ponto de entrada de dados.


         */
    }

}//class


/*

Documentação spring security atualizações "websecurityconfigureradapter":
- https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html

- https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter

- https://www.mballem.com/post/websecurityconfigureradapter-is-deprecated/

- https://www.baeldung.com/spring-security-5-default-password-encoder

CSRF TOKEN - Quando alguém executa uma ação em seu nome.

SITUAÇÃO: Uma pessoa X e outra Y, tem contas no mesmo banco.
A pessoa X, em sua conta começa a realizar os passos a passo
para executar uma transferência da conta Y para a dela(X),
no entanto, essa operação é vista como INVÁLIDA ou deveria ser vista.
Porém a pessoa X que está executando essa ações de laguma forma seleciona o
escorpo da requisição no extado somente de realizar o "Submit/Envio" para efetivação
via email/link para a pessoa Y, que ao clicar no link e redirencionada para
a sua conta ainda com uma sessão ativa válida é realiza a operação de Transferência  no
lugar da pessoa X (OPERAÇÃO QUE DEVERIA SER INVÁLIDA).




    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http)  throws Exception {
        http.csrf() .disable()
//      .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .authorizeHttpRequests((authz-> authz.anyRequest().authenticated()))
                .formLogin()//redireciona para uma page de login/autenticação padrão do spring
                .and()
                .httpBasic(Customizer.withDefaults());
        return http.build();

    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(AuthenticationManagerBuilder auth ) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("Password '{}'",passwordEncoder.encode("academy"));

        UserDetails user = User.withUsername("uanderson")
                .password(passwordEncoder.encode("academy"))
                .roles("USER","ADMIN")
                .build();

        UserDetails devdojo = User.withUsername("devdojo")
                .password(passwordEncoder.encode("academy"))
                .roles("USER")
                .build();

        Collection<UserDetails> users = new ArrayList<>();
        users.add(user);
        users.add(devdojo);

        return new InMemoryUserDetailsManager(users);
    }



 */