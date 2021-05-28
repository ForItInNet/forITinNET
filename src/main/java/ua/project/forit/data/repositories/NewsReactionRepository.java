package ua.project.forit.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.project.forit.data.entities.models.*;

@Repository
public interface NewsReactionRepository extends JpaRepository<NewsReaction, Long>
{
    NewsReaction getByNewsAndUser(News news, User user);

    boolean existsPostReactionByNewsAndUser(News news, User user);
}
