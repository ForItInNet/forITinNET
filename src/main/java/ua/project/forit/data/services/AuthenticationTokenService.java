package ua.project.forit.data.services;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.project.forit.data.entities.models.AuthenticationToken;
import ua.project.forit.data.entities.models.User;
import ua.project.forit.data.repositories.AuthenticationTokenRepository;
import ua.project.forit.exceptions.AuthenticationTokenException;

import java.util.Date;

@Service
public class AuthenticationTokenService
{
    public static final String TOKEN_NAME;

    static
    {
        TOKEN_NAME = "X-Authentication-Token";
    }

    protected final AuthenticationTokenRepository authenticationTokenRepository;

    @Autowired
    public AuthenticationTokenService(AuthenticationTokenRepository authenticationTokenRepository)
    {
        this.authenticationTokenRepository = authenticationTokenRepository;
    }


    public boolean tokenPeriodNotExpired(@NonNull AuthenticationToken token)
    {
        Date tokenDateExpiry = token.getDateExpiry();
        if(tokenDateExpiry == null)
            return true;

        return tokenDateExpiry.after(new Date());
    }

    public AuthenticationToken getToken(String token) throws AuthenticationTokenException
    {
        return authenticationTokenRepository.getAuthenticationTokenByToken(token)
                .orElseThrow(() -> new AuthenticationTokenException("Не знайдено токен авторизації"));

    }

    public AuthenticationToken getTokenByTokenAndIP(@NonNull String token, @NonNull String IP)
    {
        AuthenticationToken authenticationToken = authenticationTokenRepository.getAuthenticationTokenByTokenAndTokenRegisterIP(token, IP);

        if(authenticationToken == null)
            return null;
        else if(!tokenPeriodNotExpired(authenticationToken))
        {
            authenticationTokenRepository.removeAuthenticationTokenByToken(authenticationToken.getToken());
            return null;
        }
        return authenticationToken;
    }

    public AuthenticationToken generateToken(@NonNull User user, @NonNull String IP, @NonNull String rememberMe)
    {
        AuthenticationToken authenticationToken = new AuthenticationToken(user, IP, !rememberMe.equals("true"));

        authenticationTokenRepository.removeAuthenticationTokenByToken(authenticationToken.getToken());
        authenticationTokenRepository.save(authenticationToken);

        return authenticationToken;
    }

    public void removeToken(@NonNull String token)
    {
        authenticationTokenRepository.removeAuthenticationTokenByToken(token);
    }
}
