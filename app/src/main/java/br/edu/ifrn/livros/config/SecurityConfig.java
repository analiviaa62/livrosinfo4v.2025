package br.edu.ifrn.livros.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll() // Permite acesso ao banco H2
                .anyRequest().authenticated() // Bloqueia todo o resto
            )
            .formLogin((form) -> form
                .loginPage("/login") // Nossa página personalizada
                .defaultSuccessUrl("/livros", true) // Redireciona para livros ao entrar
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            // Liberações para o H2 Console funcionar
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // CONFIGURAÇÃO DO BIBLIOTECÁRIO
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("admin")   // LOGIN
            .password("123456")  // SENHA
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user);
    }
}