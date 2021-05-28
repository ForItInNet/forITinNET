package ua.project.forit.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.project.forit.data.entities.models.Article;
import ua.project.forit.data.entities.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByEntityToken(String entityToken);


    boolean existsUserByEmail(String email);
    boolean existsUserByEntityToken(String entityToken);
}
