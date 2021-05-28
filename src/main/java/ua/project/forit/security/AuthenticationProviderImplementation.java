package ua.project.forit.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProviderImplementation implements AuthenticationProvider
{
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationProviderImplementation(UserDetailsService userDetailsService,
                                                PasswordEncoder passwordEncoder)
    {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails user = userDetailsService.loadUserByUsername(email);

        if(passwordEncoder.matches(password, user.getPassword()))
            return new UsernamePasswordAuthenticationToken(email, password, user.getAuthorities());

        throw new AuthenticationCredentialsNotFoundException("Невірний логін або пароль.");
    }

    @Override
    public boolean supports(Class<?> authenticationType)
    {
        return authenticationType.equals(UsernamePasswordAuthenticationToken.class);
    }
}
