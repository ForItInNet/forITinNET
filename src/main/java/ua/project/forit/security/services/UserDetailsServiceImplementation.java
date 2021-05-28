package ua.project.forit.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.project.forit.data.services.UserService;
import ua.project.forit.exceptions.UserServiceException;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService
{
    private final UserService userService;

    @Autowired
    public UserDetailsServiceImplementation(UserService userService)
    {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        try
        {
            return userService.getUserByEmail(email);
        }
        catch (UserServiceException ex)
        {
            throw new UsernameNotFoundException("Невірний логін або пароль.");
        }
    }
}
