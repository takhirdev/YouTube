package you_tube_own.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import you_tube_own.util.MD5Util;

@EnableWebSecurity
@Component
@Configuration
public class SecurityConfig {
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private JwtTokenFilter jwtTokeFilter;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() { //
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return MD5Util.getMd5(rawPassword.toString()).equals(encodedPassword);
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // authorization
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/profile/verification/*").permitAll()
                    .requestMatchers("/profile/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/attach/**").permitAll()
                    .requestMatchers("/chanel/**").permitAll()
                    .requestMatchers("/video/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/category/**").permitAll()
                    .requestMatchers("/playlist/**").hasAnyRole("USER", "ADMIN")

                    // swagger
                    .requestMatchers("/v2/api-docs").permitAll()
                    .requestMatchers("/v3/api-docs").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers("/swagger-resources").permitAll()
                    .requestMatchers("/swagger-resources/**").permitAll()
                    .requestMatchers("/configuration/ui").permitAll()
                    .requestMatchers("/configuration/security").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/webjars/**").permitAll()
                    .requestMatchers("/swagger-ui.html").permitAll()
                    .anyRequest()
                    .authenticated();
        });
        http.addFilterBefore(jwtTokeFilter, UsernamePasswordAuthenticationFilter.class);
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
