package ua.project.forit.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationSuccessHandlerImplementation implements AuthenticationSuccessHandler
{


    @Autowired
    public AuthenticationSuccessHandlerImplementation()
    {

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException
    {
        //httpServletResponse.addCookie(new Cookie("username", authentication.getName()));
        //httpServletResponse.addCookie(new Cookie("password", cookiePasswordService.generateAndSaveCookiesPassword(userService.findByEmail(authentication.getName()), httpServletRequest.getRemoteAddr())));
    }
}
