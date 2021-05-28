package ua.project.forit.security.services;

import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import ua.project.forit.data.entities.models.User;
import ua.project.forit.data.services.AuthenticationTokenService;
import ua.project.forit.data.services.UserService;
import ua.project.forit.exceptions.UserServiceException;

import static com.mysql.cj.conf.PropertyKey.logger;

@Service
public class AuthorizationService
{
    protected final UserService userService;
    protected final AuthenticationTokenService authenticationTokenService;

    public AuthorizationService(UserService userService,
                                AuthenticationTokenService authenticationTokenService)
    {
        this.userService = userService;
        this.authenticationTokenService = authenticationTokenService;
    }

    public String authenticateUser(String email, String password, String rememberMe, String IP) throws AuthorizationServiceException
    {
        if(email == null || password == null || IP == null || rememberMe == null)
            throw new AuthorizationServiceException("Некоректно введені дані");


        try
        {
            User user = userService.getUserByEmailAndPassword(email, password);
            return authenticationTokenService.generateToken(user, IP, rememberMe).toString();
        }
        catch (UserServiceException ex)
        {
            throw new AuthorizationServiceException("Невіргий логін або пароль");
        }
    }

    public void logout(String token)
    {
        if(token != null)
            authenticationTokenService.removeToken(token);
    }
}
