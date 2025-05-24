package MarioOlaya.Projecte4.EntreBicis.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Configuració de seguretat per a la web i la API REST.
 */
@Configuration
public class SecurityConfig {

        @Autowired
        private UserValidator userValidator;

        /**
         * Configura la cadena de seguretat per a les rutes de l'API REST.
         *
         * @param http L'objecte {@link HttpSecurity} per configurar les rutes.
         * @return {@link SecurityFilterChain} per a les rutes de la API.
         * @throws Exception en cas d'error de configuració.
         */
        @Bean
        @Order(1)
        public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
                http
                                .securityMatcher("/api/**")
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint((request, response, authException) -> response
                                                                .sendError(HttpServletResponse.SC_UNAUTHORIZED)));
                return http.build();
        }

        /**
         * Configura la cadena de seguretat per a les rutes de la web.
         *
         * @param http L'objecte {@link HttpSecurity} per configurar les rutes.
         * @return {@link SecurityFilterChain} per a les rutes web.
         * @throws Exception en cas d'error de configuració.
         */
        @Bean
        @Order(2)
        public SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/images/**", "/login").permitAll()
                                                .requestMatchers("/").hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(login -> login
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/")
                                                .failureUrl("/login?error=true")
                                                .usernameParameter("email")
                                                .passwordParameter("password")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout=true")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll());
                return http.build();
        }

        /**
         * Defineix l'encodador de contrasenyes.
         *
         * @return {@link PasswordEncoder} basat en BCrypt.
         */
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        /**
         * Defineix el gestor d'autenticació amb el servei de validació d'usuaris
         * personalitzat.
         *
         * @param http L'objecte {@link HttpSecurity}.
         * @return {@link AuthenticationManager} configurat.
         * @throws Exception en cas d'error de configuració.
         */
        @SuppressWarnings("removal")
        @Bean
        public AuthenticationManager authManager(HttpSecurity http) throws Exception {
                return http.getSharedObject(AuthenticationManagerBuilder.class)
                                .userDetailsService(userValidator)
                                .passwordEncoder(passwordEncoder())
                                .and()
                                .build();
        }
}
