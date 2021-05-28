package ua.project.forit.data.entities.models;

import lombok.NonNull;
import ua.project.forit.data.entities.interfaces.Comment;

import javax.persistence.*;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "post_coment")
public class PostComment extends Comment
{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    protected Post post;

    public PostComment() {}

    public PostComment(@NonNull User author, @NonNull Post post, @NonNull String comment)
    {
        this.author = author;
        this.post = post;
        this.comment = comment;

        entityToken = UUID.randomUUID().toString();
    }

    @Override
    protected void addAdditionalParameters(Map<String, Object> data)
    {
        data.put("author", author.getShortJSON());
    }
}
