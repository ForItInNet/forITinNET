package ua.project.forit.security.filters;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import ua.project.forit.data.entities.models.AuthenticationToken;
import ua.project.forit.data.entities.models.User;
import ua.project.forit.data.services.AuthenticationTokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthenticationFilter extends GenericFilterBean
{
    private final AuthenticationTokenService authenticationTokenService;

    public AuthenticationFilter(AuthenticationTokenService authenticationTokenService)
    {
        this.authenticationTokenService = authenticationTokenService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        try
        {
            String requestAuthenticationToken = ((HttpServletRequest) servletRequest).getHeader(AuthenticationTokenService.TOKEN_NAME);
            AuthenticationToken token = authenticationTokenService.getTokenByTokenAndIP(requestAuthenticationToken, servletRequest.getRemoteAddr());
            User user = token.getUser();
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch (Throwable ex)
        {
            SecurityContextHolder.getContext().setAuthentication(null);
        }


        filterChain.doFilter(servletRequest, servletResponse);
    }
}
