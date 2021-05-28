package ua.project.forit.data.entities.models;

import lombok.Data;
import ua.project.forit.data.entities.enums.WritingReaction;

import javax.persistence.*;

@Data
@Entity
@Table(name = "article_reaction")
public class ArticleReaction
{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    protected User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    protected Article article;

    protected WritingReaction writingReaction;

    {
        writingReaction = WritingReaction.VIEW;
    }

    public ArticleReaction() {}

    public ArticleReaction(Article article, User user)
    {
        this.article = article;
        this.user = user;
    }
}
