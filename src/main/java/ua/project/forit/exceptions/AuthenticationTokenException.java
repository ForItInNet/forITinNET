package ua.project.forit.exceptions;

import javax.naming.AuthenticationException;

public class AuthenticationTokenException extends AuthenticationException
{
    public AuthenticationTokenException(String message)
    {
        super(message);
    }
}
