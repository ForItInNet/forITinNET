package ua.project.forit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ua.project.forit.data.repositories.UserRepository;
import ua.project.forit.data.services.AuthenticationTokenService;
import ua.project.forit.data.services.UserService;
import ua.project.forit.security.filters.AuthenticationFilter;
import ua.project.forit.security.services.UserDetailsServiceImplementation;

import java.util.Arrays;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationProvider authenticationProvider;


    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    @Bean
    @Override
    public UserDetailsService userDetailsService()
    {
        return new UserDetailsServiceImplementation(userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder(8);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth)
    {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        http.addFilterBefore(new AuthenticationFilter(authenticationTokenService),
                UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .mvcMatchers("/admin").hasAnyAuthority(AuthoritiesContainer.getArrayAuthoritiesFrom(AuthoritiesContainer.Authorities.ADMIN))
                .mvcMatchers("/admin/**").hasAnyAuthority(AuthoritiesContainer.getArrayAuthoritiesFrom(AuthoritiesContainer.Authorities.SUPERUSER))
                .mvcMatchers("/registration/**", "/login/**", "/logout", "/").permitAll()
                .anyRequest().authenticated();

        http.httpBasic().disable();
        http.sessionManagement().disable(); //sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.logout()
                .disable();

        http.cors()
            .and().csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList(
                "content-type",
                "x-xsrf-token",
                "authorization",
                "x-requested-with",
                "x-authentication-token",
                "withCredentials",
                "_csrf"
        ));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
