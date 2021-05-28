package ua.project.forit.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.project.forit.data.entities.models.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>
{}
