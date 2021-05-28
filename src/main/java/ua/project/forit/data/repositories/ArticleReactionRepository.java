package ua.project.forit.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.project.forit.data.entities.models.*;

@Repository
public interface ArticleReactionRepository extends JpaRepository<ArticleReaction, Long>
{
    ArticleReaction getByArticleAndUser(Article article, User user);

    boolean existsPostReactionByArticleAndUser(Article article, User user);
}
