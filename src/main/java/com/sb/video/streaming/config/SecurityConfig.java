package com.sb.video.streaming.config;

import com.sb.video.streaming.filter.JwtFilter;
import com.sb.video.streaming.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // @Autowired
    // private CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    private JwtFilter jwtFilter;


    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public WebMvcConfigurer corsConfigurer() {
    //     return new WebMvcConfigurer() {
    //         @Override
    //         public void addCorsMappings(CorsRegistry registry) {
    //             registry.addMapping("/api/**")
    //                     .allowedOrigins("http://localhost:5173")     // 🔥 React Origin
    //                     .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
    //                     .allowedHeaders("*")
    //                     .allowCredentials(true);                     // needed for basic auth
    //         }
    //     };
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(customizer -> customizer.disable()).cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/health").permitAll()
                .requestMatchers("/info").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/login","/logout").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(login -> login.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
            // .formLogin(login -> login
            //     .loginPage("/login")
            //     .successHandler(successHandler)
            // )
            // .logout(logout -> logout
            //     .logoutUrl("/logout")
            //     .logoutSuccessUrl("/login")
            //     .permitAll()
            // );
        return http.build();
            
    }

    // @Bean
    // public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
    //     DaoAuthenticationProvider auth = new DaoAuthenticationProvider(userDetailsService);
    //     auth.setPasswordEncoder(passwordEncoder());
    //     return auth;
    // }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception{
        return auth.getAuthenticationManager();
    }
}
