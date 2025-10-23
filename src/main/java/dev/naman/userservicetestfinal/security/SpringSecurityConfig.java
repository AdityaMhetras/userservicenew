package dev.naman.userservicetestfinal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SpringSecurityConfig {

    /*@Bean
//    @Order(1)
    public SecurityFilterChain filteringCriteria(HttpSecurity http) throws Exception {
*//*        http.cors().disable();
        http.csrf().disable();
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
//        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
//        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/auth/*").authenticated());
        return http.build();*//*

        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                                .anyRequest().permitAll()
                        // .requestMatchers("/auth/*").authenticated() // Uncomment to restrict
                )
                .build();

*//*        .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/auth/**").authenticated()
                .anyRequest().permitAll()
        );*//*

    }*/

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
