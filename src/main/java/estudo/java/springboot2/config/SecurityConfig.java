package estudo.java.springboot2.config;

import jakarta.servlet.Filter;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EnableWebSecurity
@Configuration
@Log4j2
public class SecurityConfig{

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http)  throws Exception {
//        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
//                .authorizeHttpRequests()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic();

        http.csrf().disable()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .authorizeHttpRequests((authz-> authz.anyRequest().authenticated()))
                .httpBasic();

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("Password '{}'",passwordEncoder.encode("test"));

        UserDetails user = User.withUsername("Uanderson")
                .password(passwordEncoder.encode("academy"))
                .roles("USER","ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }



}//class

/*

Documentação spring security atualizações "websecurityconfigureradapter":
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




 */