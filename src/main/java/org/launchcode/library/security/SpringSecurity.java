package org.launchcode.library.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/register/**").permitAll()
                                .requestMatchers("/index").permitAll()
                                .requestMatchers("/library").permitAll()
                                //.requestMatchers("/landing").hasRole("ADMIN")
                                //for all books related URLs
                                .requestMatchers("/books/**").hasRole("ADMIN")
                                .requestMatchers("/books/search/**").hasRole("ADMIN")
                                .requestMatchers("/books/detail/**").hasRole("ADMIN")
                                .requestMatchers("/books/add/**").hasRole("ADMIN")
                                .requestMatchers("/books/update/**").hasRole("ADMIN")
                                .requestMatchers("/books/delete/**").hasRole("ADMIN")
                                .requestMatchers("/books/checkout/**").hasRole("ADMIN")
                                .requestMatchers("/books/hold/**").hasRole("ADMIN")
                                .requestMatchers("/books/checkin/**").hasRole("ADMIN")
                                //.requestMatchers("/books/view/**").hasRole("ADMIN")
                                // for all student related URLs
                                /*.requestMatchers("/students/**").hasRole("ADMIN")
                                .requestMatchers("/students/search/**").hasRole("ADMIN")
                                .requestMatchers("/students/detail/**").hasRole("ADMIN")
                                .requestMatchers("/students/add/**").hasRole("ADMIN")
                                .requestMatchers("/students/delete/**").hasRole("ADMIN")
                                .requestMatchers("/students/update/**").hasRole("ADMIN")*/
                                .requestMatchers("/landing").permitAll()
                                .requestMatchers("/students/**").permitAll()
                                .requestMatchers("/students/detail/**").permitAll()
                                .requestMatchers("/students/search/**").permitAll()
                                .requestMatchers("/students/detail/**").permitAll()
                                .requestMatchers("/students/add/**").permitAll()
                                .requestMatchers("/students/delete/**").permitAll()
                                .requestMatchers("/students/update/**").permitAll()
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/landing")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();
    }

    // setup UserDetailService, password encoder to build memory by AuthenticationManager.

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}