package ua.project.forit.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.project.forit.data.entities.models.Post;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>
{
    Optional<Post> getPostByEntityToken(String entityToken);

    @Transactional
    int removePostByAuthorEmailAndEntityToken(String email, String entityToken);

    boolean existsPostByEntityToken(String entityToken);
}
