package ua.project.forit.data.services;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.project.forit.data.entities.models.User;
import ua.project.forit.data.repositories.UserRepository;
import ua.project.forit.exceptions.UserServiceException;

import java.util.List;

@Service
public class UserService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //private final AuthorityRepository authorityRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder)//,
                       //AuthorityRepository authorityRepository)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        //this.authorityRepository = authorityRepository;
    }

    public void createUser(@NonNull User user)
    {
        //Set<Authority> authorities = new HashSet<>();
        //authorities.add(new Authority(AuthoritiesContainer.Authorities.READER.name()));
        //user.setAuthorities(authorities);
        //user.setEntityToken(EntityTokenService.generateUserEntityToken());
        userRepository.saveAndFlush(user);
    }

    public User getUserByEmail(@NonNull String email) throws UserServiceException
    {
        return userRepository.getUserByEmail(email)
                .orElseThrow(() -> new UserServiceException("Користувача не знайдено"));
    }

    public User getUserByEntityToken(@NonNull String entityToken) throws UserServiceException
    {
        return userRepository.getUserByEntityToken(entityToken)
                .orElseThrow(() -> new UserServiceException("Користувача не знайдено"));
    }

    public User getUserByEmailAndPassword(@NonNull String email, @NonNull String password) throws UserServiceException
    {
        User user = getUserByEmail(email);

        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new UserServiceException("Користувача не знайдено");

        return user;
    }

    public void follow(@NonNull User userForFollowing, @NonNull User user)
    {
        List<User> followings = user.getFollowings();
        List<User> followers = userForFollowing.getFollowers();

        if(!followings.contains(userForFollowing))
        {
            followings.add(userForFollowing);
            followers.add(user);
        }
        else
        {
            followings.remove(userForFollowing);
            followers.remove(user);
        }

        user.setFollowings(followings);
        userForFollowing.setFollowers(followers);
        saveAndFlush(userForFollowing);
        saveAndFlush(user);
    }

    public void saveAndFlush(final User user)
    {
        userRepository.saveAndFlush(user);
    }

    public boolean userExistsByEmail(@NonNull String email)
    {
        return userRepository.existsUserByEmail(email);
    }
}
