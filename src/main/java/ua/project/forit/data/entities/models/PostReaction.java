package ua.project.forit.data.entities.models;

import lombok.Data;
import ua.project.forit.data.entities.enums.WritingReaction;

import javax.persistence.*;

@Data
@Entity
public class PostReaction
{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    protected User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    protected Post post;

    protected WritingReaction writingReaction;

    {
        writingReaction = WritingReaction.VIEW;
    }

    public PostReaction() {}

    public PostReaction(Post post, User user)
    {
        this.post = post;
        this.user = user;
    }
}
