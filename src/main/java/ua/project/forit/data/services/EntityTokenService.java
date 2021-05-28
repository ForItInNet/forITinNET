package ua.project.forit.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.project.forit.data.repositories.PostRepository;
import ua.project.forit.data.repositories.UserRepository;

import java.util.UUID;

@Service
public abstract class EntityTokenService
{
    @Autowired
    protected static UserRepository userRepository;

    @Autowired
    protected static PostRepository postRepository;

    public static String generateUserEntityToken()
    {
        return UUID.randomUUID().toString();
    }

    public static String generatePostEntityToken()
    {
        return UUID.randomUUID().toString();
    }
}
