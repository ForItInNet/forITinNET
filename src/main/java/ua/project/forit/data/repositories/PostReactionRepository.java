package ua.project.forit.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.project.forit.data.entities.models.Post;
import ua.project.forit.data.entities.models.PostReaction;
import ua.project.forit.data.entities.models.User;

import java.util.Optional;

@Repository
public interface PostReactionRepository extends JpaRepository<PostReaction, Long>
{
    Optional<PostReaction> getByPostAndUser(Post post, User user);

    boolean existsPostReactionByPostAndUser(Post post, User user);
}
