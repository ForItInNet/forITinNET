package ua.project.forit.data.entities.models;

import lombok.Data;
import ua.project.forit.data.entities.enums.WritingReaction;

import javax.persistence.*;

@Data
@Entity
public class NewsReaction
{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    protected User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    protected News news;

    protected WritingReaction writingReaction;

    {
        writingReaction = WritingReaction.VIEW;
    }

    public NewsReaction() {}

    public NewsReaction(News news, User user)
    {
        this.news = news;
        this.user = user;
    }
}
