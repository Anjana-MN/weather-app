    package com.project.weatherforecast.config;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationProvider;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.NoOpPasswordEncoder;
    import org.springframework.security.provisioning.InMemoryUserDetailsManager;
    import org.springframework.security.web.SecurityFilterChain;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        private final UserDetailsService userDetailsService;

        @Bean
        public AuthenticationProvider authenticationProvider(){
            DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
            daoAuthenticationProvider.setUserDetailsService(userDetailsService);
            daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
            return daoAuthenticationProvider;
        }

        public SecurityConfig(UserDetailsService userDetailsService) {
            this.userDetailsService = userDetailsService;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
                throws Exception {
            return httpSecurity
                    .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                    .authorizeRequests()
                    .requestMatchers("/api/user/register").permitAll()
                    .anyRequest().authenticated().and()
                    .httpBasic(Customizer.withDefaults())
                    .sessionManagement(session -> session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS))
                    .build();

        }

//        @Bean
//        public UserDetailsService userDetailsService(){
//
//            UserDetails user = User.withDefaultPasswordEncoder()
//                    .username("anjana").password("1234").roles("USER").build();
//            return new InMemoryUserDetailsManager(user);
//        }
    }
