package ua.project.forit.data.entities.models;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.project.forit.data.annotations.OpenForView;
import ua.project.forit.data.entities.enums.WritingReaction;
import ua.project.forit.data.entities.enums.WritingType;
import ua.project.forit.data.entities.interfaces.Writing;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonRootName(value = "post", namespace = "posts")
@Entity
@Table(name = "post")
public class Post extends Writing
{
    @OpenForView
    @Size(min = 150, max = 10000)
    @NotBlank
    protected String text;

    @OpenForView
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<PostReaction> postReactions;

    @OpenForView
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<PostComment> postComments;

    {
        postReactions = new ArrayList<>();
        postComments = new ArrayList<>();
    }

    public Post() {}

    public Post(Long id, String title, Date publishDate, boolean draft, @Size(min = 150, max = 10000) @NotBlank String text) {
        super(id, title, publishDate, draft, WritingType.POST);
        this.text = text;
    }

    @Override
    public int hashCode()
    {
        return id.intValue();
    }

    @Override
    public boolean equals(Object object)
    {
        if(!(object instanceof Post))
            return false;

        Post post = (Post) object;
        return id.equals(post.getId());
    }

    @Override
    protected void addAdditionalParameters(Map<String, Object> data)
    {
        final int[] likesAndDislikes = {0, 0};

        getPostReactions().forEach(reaction -> {
            if(reaction.getWritingReaction() == WritingReaction.LIKE)
                likesAndDislikes[0]++;
            else if(reaction.getWritingReaction() == WritingReaction.DISLIKE)
                likesAndDislikes[1]++;
        });

        Map<String, Integer> additionalData = new HashMap<>();
        additionalData.put("likes", likesAndDislikes[0]);
        additionalData.put("dislikes", likesAndDislikes[1]);
        additionalData.put("views", postReactions.size());

        data.put("postReactions", additionalData);
    }
}
